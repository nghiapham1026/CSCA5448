package com.example.pcb.client;

import java.util.*;

/** Formats the JSON result map into the text table shown in the spec. */
public class ReportFormatter {

    public static String format(Map<String, Object> json) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=== ")
          .append(json.get("pcbType"))
          .append("  –  ")
          .append(json.get("pcbsRun"))
          .append(" boards ===\n");

        addSection(sb, "Station failures",      (String) json.get("stationFailures"));
        addSection(sb, "Defect-specific fails", (String) json.get("pcbDefectFailures"));

        sb.append("\nTOTAL FAILED   : ").append(json.get("totalFailed"));
        sb.append("\nTOTAL PRODUCED : ").append(json.get("totalProduced")).append('\n');

        return sb.toString();
    }

    /* ------------ helpers ------------ */

    private static void addSection(StringBuilder sb, String title, String rawJson) {
        sb.append('\n').append(title).append(":\n");
        LinkedHashMap<String, Integer> map = toMap(rawJson);
        map.forEach((k, v) ->
            sb.append(String.format("  %-30s %5d%n", k, v)));
    }

    private static LinkedHashMap<String, Integer> toMap(String j) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        if (j == null || j.length() <= 2) return map; // "{}"
        j = j.substring(1, j.length() - 1);           // strip {}
        for (String kv : j.split(",")) {
            int i = kv.indexOf(':');
            String key = kv.substring(0, i).replace("\"", "");
            int val = Integer.parseInt(kv.substring(i + 1));
            map.put(key, val);
        }
        return map;
    }
}
