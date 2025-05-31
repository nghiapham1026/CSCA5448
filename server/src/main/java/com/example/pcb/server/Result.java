// Result.java (simple DTO)

package com.example.pcb.server;

import java.util.Map;

public class Result {
    public final String pcbType;
    public final int pcbsRun;
    public final Map<String,Integer> stationFailures;
    public final Map<String,Integer> pcbDefectFailures;
    public final int totalFailed;
    public final int totalProduced;

    public Result(String pcbType,int pcbsRun,Map<String,Integer> stationFailures,
                  Map<String,Integer> pcbDefectFailures,int totalFailed,int totalProduced){
        this.pcbType = pcbType;
        this.pcbsRun = pcbsRun;
        this.stationFailures = stationFailures;
        this.pcbDefectFailures = pcbDefectFailures;
        this.totalFailed = totalFailed;
        this.totalProduced = totalProduced;
    }
}