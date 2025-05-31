// SimulationServer.java (REST server using JDK `HttpServer`)

package com.example.pcb.server;

import com.sun.net.httpserver.*; // JDK‑built‑in minimal HTTP server
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.EnumMap;
import java.util.Map;

/** Simulation server that runs 3 simulations on start‑up then exposes the
 *  results via a simple REST endpoint:  GET /results?type=TEST|SENSOR|GATEWAY */
public class SimulationServer {
    private static final int PORT = 8000;
    private static final Map<PCBFactory.Type,Result> cache = new EnumMap<>(PCBFactory.Type.class);

    public static void main(String[] args) throws IOException {
        runSimulations();
        startHttp();
    }

    private static void runSimulations(){
        for(PCBFactory.Type t: PCBFactory.Type.values()){
            PCBType board = PCBFactory.create(t); // Strategy object produced by Factory
            Simulation sim = new Simulation(board); // DI here
            Result res = sim.run(1000);
            cache.put(t,res);
            System.out.println("[Server] Finished " + board.getName());
        }
    }

    private static void startHttp() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT),0);
        server.createContext("/results", SimulationServer::handleResults);
        server.setExecutor(null);
        server.start();
        System.out.println("Simulation server listening on http://localhost:"+PORT+"/results");
    }

    private static void handleResults(HttpExchange ex) throws IOException {
        String query = ex.getRequestURI().getQuery();
        String typeParam = null;
        if(query!=null){
            for(String p: query.split("&")){
                if(p.startsWith("type=")){ typeParam = p.substring(5).toUpperCase(); }
            }
        }
        PCBFactory.Type type=null;
        try{ type = PCBFactory.Type.valueOf(typeParam); }catch(Exception ignored){}
        String body;
        int code;
        if(type==null){
            body = "{\"error\":\"missing or invalid type parameter\"}";
            code = 400;
        }else{
            body = JsonUtil.toJson(cache.get(type));
            code = 200;
        }
        ex.getResponseHeaders().add("Content-Type","application/json");
        ex.sendResponseHeaders(code, body.getBytes().length);
        try(OutputStream os = ex.getResponseBody()){ os.write(body.getBytes()); }
    }
}