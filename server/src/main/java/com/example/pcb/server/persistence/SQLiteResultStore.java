package com.example.pcb.server.persistence;

import com.example.pcb.server.PCBFactory;

import java.sql.*;

/**
 * Plain-JDBC implementation backed by a single-file SQLite DB.
 * No ORM – keeps things lightweight as required by the spec.
 */
public class SQLiteResultStore implements ResultStore {

    private static final String URL = "jdbc:sqlite:sim_results.db";
    private static final String DDL =
        "CREATE TABLE IF NOT EXISTS results (" +
        "  pcb_key TEXT PRIMARY KEY," +
        "  payload TEXT NOT NULL" +
        ')';

    private final Connection conn;
    private final PreparedStatement upsert;
    private final PreparedStatement select;

    public SQLiteResultStore() throws Exception {
        // Ensure driver is on the class-path
        Class.forName("org.sqlite.JDBC");

        conn = DriverManager.getConnection(URL);
        conn.createStatement().execute(DDL);

        upsert = conn.prepareStatement(
            "INSERT OR REPLACE INTO results(pcb_key, payload) VALUES(?,?)"
        );
        select = conn.prepareStatement(
            "SELECT payload FROM results WHERE pcb_key = ?"
        );
    }

    @Override
    public void save(PCBFactory.Type type, String json) throws SQLException {
        upsert.setString(1, type.name());
        upsert.setString(2, json);
        upsert.executeUpdate();
    }

    @Override
    public String load(PCBFactory.Type type) throws SQLException {
        select.setString(1, type.name());
        try (ResultSet rs = select.executeQuery()) {
            return rs.next() ? rs.getString(1) : null;
        }
    }

    @Override
    public void close() throws Exception {
        upsert.close();
        select.close();
        conn.close();
    }
}
