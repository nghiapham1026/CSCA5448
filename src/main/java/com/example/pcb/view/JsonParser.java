// JsonParser.java  – 1-level parser reused by repository
package com.example.pcb.view;

import java.util.*;

public final class JsonParser {
    public static Map<String,Object> parse(String json){
        Map<String,Object> out=new LinkedHashMap<>();
        json=json.trim();
        if(json.startsWith("{")) json=json.substring(1,json.length()-1);
        List<String> pairs=split(json);
        for(String p:pairs) add(out,p);
        return out;
    }
    private static List<String> split(String s){
        List<String> lst=new ArrayList<>(); StringBuilder cur=new StringBuilder(); int d=0;
        for(char c:s.toCharArray()){
            if(c=='{') d++; else if(c=='}') d--;
            if(c==','&&d==0){ lst.add(cur.toString()); cur.setLength(0);} else cur.append(c);
        }
        if(cur.length()>0) lst.add(cur.toString());
        return lst;
    }
    private static void add(Map<String,Object> m,String part){
        int c=part.indexOf(':'); if(c<0) return;
        String k=part.substring(0,c).replaceAll("[\" ]",""); String v=part.substring(c+1).trim();
        m.put(k,v);
    }
    private JsonParser(){}
}
