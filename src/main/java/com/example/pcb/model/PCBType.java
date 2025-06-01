// PCBType.java  (Strategy interface)
package com.example.pcb.model;

public interface PCBType {
    String  getName();
    double  getDefectChance(String station);   // 0 ‒ 1
}
