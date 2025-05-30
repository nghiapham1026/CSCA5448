// SensorBoard.java
package com.example.pcb.server;

public class SensorBoard implements PCBType {
    @Override public String getName() { return "Sensor Board"; }
    @Override public double getDefectChance(String station) {
        switch (station) {
            case "Place Components":          return 0.002;
            case "Optical Inspection":         return 0.002;
            case "Hand Soldering/Assembly":    return 0.004;
            case "Test (ICT or Flying Probe)": return 0.004;
            default:                            return 0.0;
        }
    }
}