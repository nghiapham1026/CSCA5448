package com.example.pcb.client;

import java.util.*;

/**
 * Ultra-light JSON helper that understands one level of nesting –
 * enough for { "stationFailures": { … }, … }.
 */
public class JsonUtil {

    /** Parses the server’s Result JSON into Map&lt;String,Object&gt;. */
    public static Map<String,Object> parse(String json) {
        Map<String,Object> out = new LinkedHashMap<>();

        json = json.trim();
        if (json.startsWith("{"))
            json = json.substring(1, json.length() - 1);   // strip outer braces

        List<String> pairs = topLevelSplit(json);
        for (String p : pairs) addPair(out, p);

        return out;
    }

    /* ---------- helpers ---------- */

    /** Split on commas that are *not* inside a {...} block. */
    private static List<String> topLevelSplit(String s) {
        List<String> list = new ArrayList<>();
        StringBuilder curr = new StringBuilder();
        int depth = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '{') depth++;
            else if (c == '}') depth--;

            if (c == ',' && depth == 0) {       // top-level comma
                list.add(curr.toString());
                curr.setLength(0);
            } else {
                curr.append(c);
            }
        }
        if (curr.length() > 0) list.add(curr.toString());
        return list;
    }

    private static void addPair(Map<String,Object> m, String part) {
        int colon = part.indexOf(':');
        if (colon < 0) return;
        String key = part.substring(0, colon).replaceAll("[\" ]", "");
        String val = part.substring(colon + 1).trim();
        m.put(key, val);                         // value kept as raw JSON string
    }
}
