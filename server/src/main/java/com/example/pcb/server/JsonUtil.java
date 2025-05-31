// JsonUtil.java (very light JSON build/parse, avoids extra libs)

package com.example.pcb.server;

import java.util.Map;
import java.util.StringJoiner;

public class JsonUtil {
    public static String toJson(Result r){
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"pcbType\":\"").append(r.pcbType).append("\",");
        sb.append("\"pcbsRun\":").append(r.pcbsRun).append(',');
        sb.append("\"stationFailures\":").append(mapToJson(r.stationFailures)).append(',');
        sb.append("\"pcbDefectFailures\":").append(mapToJson(r.pcbDefectFailures)).append(',');
        sb.append("\"totalFailed\":").append(r.totalFailed).append(',');
        sb.append("\"totalProduced\":").append(r.totalProduced);
        sb.append('}');
        return sb.toString();
    }

    private static String mapToJson(Map<String,Integer> map){
        StringJoiner sj = new StringJoiner(",","{","}");
        map.forEach((k,v)-> sj.add("\""+k+"\":"+v));
        return sj.toString();
    }
}