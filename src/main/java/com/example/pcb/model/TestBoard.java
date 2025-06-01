// TestBoard.java
package com.example.pcb.model;
public class TestBoard implements PCBType {
    @Override public String getName() { return "Test Board"; }
    @Override public double getDefectChance(String s){
        switch (s){
            case "Place Components":           return 0.05;
            case "Optical Inspection":          return 0.10;
            case "Hand Soldering/Assembly":     return 0.05;
            case "Test (ICT or Flying Probe)":  return 0.10;
            default: return 0;
        }
    }
}
