// ReportFormatter.java (unchanged except for package)
package com.example.pcb.view;

import java.util.*;

public final class ReportFormatter {
    public static String format(Map<String,Object> json){
        StringBuilder sb=new StringBuilder();
        sb.append("\n=== ").append(json.get("pcbType"))
          .append(" – ").append(json.get("pcbsRun")).append(" boards ===\n");
        add(sb,"Station failures",(String)json.get("stationFailures"));
        add(sb,"Defect-specific fails",(String)json.get("pcbDefectFailures"));
        sb.append("\nTOTAL FAILED   : ").append(json.get("totalFailed"));
        sb.append("\nTOTAL PRODUCED : ").append(json.get("totalProduced")).append('\n');
        return sb.toString();
    }
    private static void add(StringBuilder sb,String title,String raw){
        sb.append('\n').append(title).append(":\n");
        LinkedHashMap<String,Integer> m=toMap(raw);
        m.forEach((k,v)->sb.append(String.format("  %-30s %5d%n",k,v)));
    }
    private static LinkedHashMap<String,Integer> toMap(String j){
        LinkedHashMap<String,Integer> m=new LinkedHashMap<>();
        if(j==null||j.length()<=2) return m;
        j=j.substring(1,j.length()-1);
        for(String kv:j.split(",")){
            int i=kv.indexOf(':');
            m.put(kv.substring(0,i).replace("\"",""),
                  Integer.parseInt(kv.substring(i+1)));
        }
        return m;
    }
}
