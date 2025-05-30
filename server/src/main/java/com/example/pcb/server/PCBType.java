// server/src/main/java/com/example/pcb/server/PCBType.java
package com.example.pcb.server;

/** Strategy interface (OO Pattern #1) */
public interface PCBType {
    String getName();
    /** Probability of PCB‑specific defect at the given station (0‒1). */
    double getDefectChance(String station);
}