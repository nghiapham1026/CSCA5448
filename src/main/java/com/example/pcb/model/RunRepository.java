// RunRepository.java  – Model/DAO boundary
package com.example.pcb.model;

import java.util.List;

public interface RunRepository extends AutoCloseable {
    long save(PPCBFactory.Type type,int qty,Result r) throws Exception;
    Result findById(long id)                   throws Exception;
    List<RunSummary> list()                    throws Exception;
    @Override void close()                     throws Exception;
}
