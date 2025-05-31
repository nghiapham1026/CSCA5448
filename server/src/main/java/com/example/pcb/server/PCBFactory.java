// PCBFactory.java  (Factory Pattern – OO Pattern #2)

package com.example.pcb.server;

public class PCBFactory {
    public enum Type { TEST, SENSOR, GATEWAY }

    /** Factory method returns the correct concrete strategy. */
    public static PCBType create(Type type) {
        switch (type) {
            case TEST:    return new TestBoard();
            case SENSOR:  return new SensorBoard();
            case GATEWAY: return new GatewayBoard();
            default: throw new IllegalArgumentException("Unknown PCB type: " + type);
        }
    }
}