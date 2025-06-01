// SQLiteRunRepository.java
package com.example.pcb.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class SQLiteRunRepository implements RunRepository {

    private static final String URL = "jdbc:sqlite:sim_runs.db";

    private final Connection        conn;
    private final PreparedStatement insertP;
    private final PreparedStatement findP;
    private final PreparedStatement listP;

    public SQLiteRunRepository() throws Exception {
        Class.forName("org.sqlite.JDBC");

        conn = DriverManager.getConnection(URL);
        conn.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS runs (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " pcb_type TEXT NOT NULL," +
            " qty INTEGER NOT NULL," +
            " produced INTEGER NOT NULL," +
            " failed INTEGER NOT NULL," +
            " payload TEXT NOT NULL," +
            " ts DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );

        insertP = conn.prepareStatement(
            "INSERT INTO runs(pcb_type,qty,produced,failed,payload) VALUES(?,?,?,?,?)",
            Statement.RETURN_GENERATED_KEYS
        );
        findP  = conn.prepareStatement("SELECT payload FROM runs WHERE id = ?");
        listP  = conn.prepareStatement(
            "SELECT id,pcb_type,qty,produced,failed,ts FROM runs ORDER BY id DESC"
        );
    }

    @Override
    public long save(PCBFactory.Type type, int qty, Result r) throws Exception {
        insertP.setString(1, type.name());
        insertP.setInt   (2, qty);
        insertP.setInt   (3, r.totalProduced);
        insertP.setInt   (4, r.totalFailed);
        insertP.setString(5, JsonUtil.toJson(r));
        insertP.executeUpdate();

        try (ResultSet rs = insertP.getGeneratedKeys()) {
            return rs.next() ? rs.getLong(1) : -1;
        }
    }

    @Override
    public String findById(long id) throws Exception {
        findP.setLong(1, id);
        try (ResultSet rs = findP.executeQuery()) {
            return rs.next() ? rs.getString(1) : null;
        }
    }

    @Override
    public List<RunSummary> list() throws Exception {
        List<RunSummary> out = new ArrayList<>();
        try (ResultSet rs = listP.executeQuery()) {
            while (rs.next()) {
                out.add(new RunSummary(
                    rs.getLong ("id"),
                    rs.getString("pcb_type"),
                    rs.getInt   ("qty"),
                    rs.getInt   ("produced"),
                    rs.getInt   ("failed"),
                    rs.getString("ts")
                ));
            }
        }
        return out;
    }

    @Override public void close() throws Exception {
        insertP.close(); findP.close(); listP.close(); conn.close();
    }
}
