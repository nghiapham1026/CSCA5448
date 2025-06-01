// PCBFactory.java  (Factory pattern)
package com.example.pcb.model;
public final class PCBFactory {
    public enum Type { TEST, SENSOR, GATEWAY }
    public static PCBType create(Type t){
        switch (t){
            case TEST:    return new TestBoard();
            case SENSOR:  return new SensorBoard();
            case GATEWAY: return new GatewayBoard();
            default: throw new IllegalArgumentException(t.name());
        }
    }
    private PCBFactory(){} // utility
}
