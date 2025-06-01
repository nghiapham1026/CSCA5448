// Result.java  – immutable DTO (kept JSON‐friendly)
package com.example.pcb.model;

import java.util.Map;
public class Result {
    public final String pcbType;
    public final int    boardsRequested;
    public final Map<String,Integer> stationFailures;
    public final Map<String,Integer> pcbDefectFailures;
    public final int    totalFailed;
    public final int    totalProduced;          // == boardsRequested – totalFailed
    public Result(String pcbType,int qty,Map<String,Integer> st,
                  Map<String,Integer> def,int failed,int produced){
        this.pcbType = pcbType;
        this.boardsRequested = qty;
        this.stationFailures = st;
        this.pcbDefectFailures = def;
        this.totalFailed = failed;
        this.totalProduced = produced;
    }
}
