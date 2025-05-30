// client/JsonUtil.java (rudimentary JSON parser)

package com.example.pcb.client;

import java.util.*;

public class JsonUtil {
    /** VERY thin parser for the specific Result JSON structure. */
    public static Map<String,Object> parse(String json){
        Map<String,Object> out = new HashMap<>();
        json = json.trim().replaceAll("[{}]","");
        String[] parts = json.split(",(?=\\\")");
        for(String part: parts){
            int colon = part.indexOf(':');
            String key = part.substring(0,colon).replaceAll("[\" ]","");
            String val = part.substring(colon+1);
            out.put(key,val);
        }
        return out;
    }
}