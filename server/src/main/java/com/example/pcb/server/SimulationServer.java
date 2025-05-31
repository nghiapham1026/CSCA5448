package com.example.pcb.server;

import com.example.pcb.server.persistence.*;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Runs one 1 000-board simulation per PCB type, stores the JSON
 * result in SQLite, then exposes them via
 *
 *   GET /results?type=TEST|SENSOR|GATEWAY
 *
 * Patterns kept intact:
 *   • Strategy  – PCBType hierarchy
 *   • Factory   – PCBFactory
 *   • DI        – Simulation receives PCBType in its constructor
 */
public class SimulationServer {

    private static final int PORT = 8000;
    private static ResultStore store;

    public static void main(String[] args) throws Exception {
        /* Choose persistence backend (SQLite here, Redis later if desired). */
        store = new SQLiteResultStore();

        /* Graceful shutdown – close DB connection. */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { store.close(); } catch (Exception ignored) {}
        }));

        runSimulations();   // generates and stores JSON for all board types
        startHttp();        // exposes REST endpoint
    }

    /** Executes simulations and writes each JSON payload to the DB. */
    private static void runSimulations() throws Exception {
        for (PCBFactory.Type t : PCBFactory.Type.values()) {
            PCBType board   = PCBFactory.create(t);     // Factory → Strategy
            Simulation sim  = new Simulation(board);    // DI in action
            String json     = JsonUtil.toJson(sim.run(1000));

            store.save(t, json);

            System.out.printf("[Server] Stored %s result in DB%n", board.getName());
        }
    }

    /** Minimal JDK-HttpServer wrapper. */
    private static void startHttp() throws IOException {
        HttpServer s = HttpServer.create(new InetSocketAddress(PORT), 0);
        s.createContext("/results", SimulationServer::handleResults);
        s.setExecutor(null);
        s.start();
        System.out.println("HTTP endpoint ready at http://localhost:" + PORT + "/results");
    }

    private static void handleResults(HttpExchange ex) throws IOException {
        Map<String, String> qp = queryParams(ex.getRequestURI().getQuery());
        String typeStr = qp.getOrDefault("type", "").toUpperCase();

        PCBFactory.Type type = null;
        try { type = PCBFactory.Type.valueOf(typeStr); } catch (Exception ignored) {}

        String body;
        int code;

        try {
            if (type == null) {
                body = "{\"error\":\"missing or invalid type parameter\"}";
                code = 400;
            } else {
                body = store.load(type);
                if (body == null) {
                    body = "{\"error\":\"no data for " + type + "\"}";
                    code = 404;
                } else {
                    code = 200;
                }
            }
        } catch (Exception e) {
            body = "{\"error\":\"server exception: " + e.getMessage() + "\"}";
            code = 500;
        }

        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(code, body.getBytes().length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(body.getBytes());
        }
    }

    /* ---------- tiny helpers ---------- */

    private static Map<String, String> queryParams(String q) {
        Map<String, String> m = new HashMap<>();
        if (q == null || q.isEmpty()) return m;

        for (String p : q.split("&")) {
            int eq = p.indexOf('=');
            if (eq > 0) {
                m.put(
                    URLDecoder.decode(p.substring(0, eq), java.nio.charset.StandardCharsets.UTF_8),
                    URLDecoder.decode(p.substring(eq + 1), java.nio.charset.StandardCharsets.UTF_8)
                );
            }
        }
        return m;
    }
}
