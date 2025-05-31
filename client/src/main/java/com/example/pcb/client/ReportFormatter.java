// ReportFormatter.java

package com.example.pcb.client;

import java.util.*;

public class ReportFormatter {
    public static void print(Map<String,Object> data){
        System.out.println("PCB type:    " + stripQuotes(data.get("pcbType")));
        System.out.println("PCBs run:    " + data.get("pcbsRun"));
        System.out.println();

        System.out.println("Station Failures");
        printSection((String)data.get("stationFailures"));
        System.out.println();
        System.out.println("PCB Defect Failures");
        printSection((String)data.get("pcbDefectFailures"));
        System.out.println();
        System.out.println("Final Results");
        System.out.printf("%-25s %s%n","Total failed PCBs:", data.get("totalFailed"));
        System.out.printf("%-25s %s%n","Total PCBs produced:", data.get("totalProduced"));
    }

    private static void printSection(String jsonSection){
        jsonSection = jsonSection.replaceAll("[{}]","");
        String[] kv = jsonSection.split(",");
        for(String pair: kv){
            String[] p = pair.split(":");
            String k = stripQuotes(p[0]);
            String v = p[1];
            System.out.printf("%-25s %s%n", k + ":", v);
        }
    }

    private static String stripQuotes(Object s){
        return String.valueOf(s).replace("\"",""");
    }
}