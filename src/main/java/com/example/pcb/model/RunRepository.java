// RunRepository.java  – Model/DAO boundary
package com.example.pcb.model;

import java.util.List;

/** DAO boundary for persisting and retrieving simulation runs. */
public interface RunRepository extends AutoCloseable {

    long save(PCBFactory.Type type, int qty, Result r) throws Exception;

    /** Returns the stored JSON payload for a run (or {@code null} if not found). */
    String findById(long id) throws Exception;

    List<RunSummary> list() throws Exception;

    @Override void close() throws Exception;
}
