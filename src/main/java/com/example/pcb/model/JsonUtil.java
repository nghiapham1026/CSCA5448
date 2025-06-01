// JsonUtil.java  – ultra-light JSON (same spirit as before, but in model pkg)
package com.example.pcb.model;

import java.util.Map;
import java.util.StringJoiner;

public final class JsonUtil {
    public static String toJson(Result r){
        StringJoiner sb = new StringJoiner(",","{","}");
        sb.add(quote("pcbType")+":"+quote(r.pcbType));
        sb.add(quote("pcbsRun")+":"+r.boardsRequested);
        sb.add(quote("stationFailures")+":"+map(r.stationFailures));
        sb.add(quote("pcbDefectFailures")+":"+map(r.pcbDefectFailures));
        sb.add(quote("totalFailed")+":"+r.totalFailed);
        sb.add(quote("totalProduced")+":"+r.totalProduced);
        return sb.toString();
    }
    private static String map(Map<String,Integer> m){
        StringJoiner sj=new StringJoiner(",","{","}");
        m.forEach((k,v)->sj.add(quote(k)+":"+v));
        return sj.toString();
    }
    private static String quote(String s){ return "\""+s+"\""; }
    private JsonUtil(){}
}
