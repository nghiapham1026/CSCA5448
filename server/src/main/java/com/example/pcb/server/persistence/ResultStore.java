package com.example.pcb.server.persistence;

import com.example.pcb.server.PCBFactory;

/**
 * DAO abstraction for saving and loading a simulation result
 * in JSON form.  Lets us swap SQLite for Redis (or anything else)
 * without touching the rest of the code-base.
 */
public interface ResultStore {

    /** Persist—or update—the JSON payload for the given PCB type. */
    void save(PCBFactory.Type type, String json) throws Exception;

    /** Retrieve the stored JSON for the given PCB type, or null if absent. */
    String load(PCBFactory.Type type) throws Exception;

    /** Close database connections / free resources. */
    void close() throws Exception;
}
