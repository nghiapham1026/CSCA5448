// Simulation.java (uses constructor‑injected `PCBType`)

package com.example.pcb.server;

import java.util.*;

public class Simulation {
    private static final String[] STATIONS = {
        "Apply Solder Paste","Place Components","Reflow Solder","Optical Inspection",
        "Hand Soldering/Assembly","Cleaning","Depanelization","Test (ICT or Flying Probe)"
    };
    private static final double STATION_FAILURE_PROB = 0.002; // 0.2 %

    private final PCBType pcbType; // DI
    private final Random random = new Random();

    public Simulation(PCBType pcbType){ this.pcbType = pcbType; }

    public Result run(int boards){
        Map<String,Integer> stationFail = new LinkedHashMap<>();
        Map<String,Integer> pcbFail = new LinkedHashMap<>();
        for(String s:STATIONS){ stationFail.put(s,0);
            if(pcbType.getDefectChance(s)>0) pcbFail.put(s,0);
        }
        int produced=0;

        pcbLoop:
        for(int i=0;i<boards;i++){
            for(String station:STATIONS){
                if(random.nextDouble()<STATION_FAILURE_PROB){
                    stationFail.compute(station,(k,v)->v+1);
                    continue pcbLoop;
                }
                double defect = pcbType.getDefectChance(station);
                if(defect>0 && random.nextDouble()<defect){
                    pcbFail.compute(station,(k,v)->v+1);
                    continue pcbLoop;
                }
            }
            produced++;
        }
        int failed = boards - produced;
        return new Result(pcbType.getName(),boards,stationFail,pcbFail,failed,produced);
    }
}