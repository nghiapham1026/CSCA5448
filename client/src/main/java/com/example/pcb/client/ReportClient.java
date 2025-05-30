// ReportClient.java

package com.example.pcb.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ReportClient {
    private static final String SERVER = "http://localhost:8000/results?type=";

    public static void main(String[] args) throws Exception {
        for(String t: new String[]{"TEST","SENSOR","GATEWAY"}){
            String json = fetch(t);
            Map<String,Object> data = JsonUtil.parse(json);
            ReportFormatter.print(data);
            System.out.println("\n-------------------------------\n");
        }
    }

    private static String fetch(String type) throws Exception {
        URL url = new URL(SERVER + type);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine())!=null) sb.append(line);
            return sb.toString();
        }
    }
}