// GatewayBoard.java
package com.example.pcb.server;

public class GatewayBoard implements PCBType {
    @Override public String getName() { return "Gateway Board"; }
    @Override public double getDefectChance(String station) {
        switch (station) {
            case "Place Components":          return 0.004;
            case "Optical Inspection":         return 0.004;
            case "Hand Soldering/Assembly":    return 0.008;
            case "Test (ICT or Flying Probe)": return 0.008;
            default:                            return 0.0;
        }
    }
}