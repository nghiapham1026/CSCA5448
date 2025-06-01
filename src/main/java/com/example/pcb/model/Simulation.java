// Simulation.java  – pure domain logic
package com.example.pcb.model;

import java.util.*;

public final class Simulation {
    private static final String[] STATIONS = {
        "Apply Solder Paste","Place Components","Reflow Solder","Optical Inspection",
        "Hand Soldering/Assembly","Cleaning","Depanelization","Test (ICT or Flying Probe)"
    };
    private static final double STATION_FAIL_PROB = 0.002;   // 0.2 %
    private final PCBType pcb;
    private final Random  rnd = new Random();
    public Simulation(PCBType pcb){ this.pcb = pcb; }

    public Result run(int qty){
        Map<String,Integer> stFail = new LinkedHashMap<>();
        Map<String,Integer> defFail= new LinkedHashMap<>();
        for(String s:STATIONS){
            stFail.put(s,0);
            if(pcb.getDefectChance(s)>0) defFail.put(s,0);
        }
        int produced=0;
        pcbLoop:
        for(int i=0;i<qty;i++){
            for(String s:STATIONS){
                if(rnd.nextDouble()<STATION_FAIL_PROB){
                    stFail.compute(s,(k,v)->v+1); continue pcbLoop;
                }
                double d=pcb.getDefectChance(s);
                if(d>0 && rnd.nextDouble()<d){
                    defFail.compute(s,(k,v)->v+1); continue pcbLoop;
                }
            }
            produced++;
        }
        return new Result(pcb.getName(),qty,stFail,defFail,qty-produced,produced);
    }
}
