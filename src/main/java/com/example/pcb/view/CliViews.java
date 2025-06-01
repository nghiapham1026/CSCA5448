// CliViews.java – three small views in one file
package com.example.pcb.view;

import com.example.pcb.model.RunSummary;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public final class CliViews {
    private static final Scanner in=new Scanner(System.in);

    public static int askQuantity(){
        System.out.print("Number of boards to run  : "); return Integer.parseInt(in.nextLine());
    }
    public static String askType(){
        System.out.print("PCB type (TEST|SENSOR|GATEWAY): "); return in.nextLine().trim().toUpperCase();
    }
    public static void listRuns(List<RunSummary> lst){
        System.out.println("\nID | Date/Time           | Type    | Qty | Prod | Fail");
        System.out.println("---+---------------------+---------+-----+------+-----");
        for(RunSummary r:lst){
            System.out.printf("%-3d| %-19s | %-7s | %4d | %4d | %4d%n",
                r.id(),r.ts(),r.pcbType(),r.qty(),r.produced(),r.failed());
        }
    }
    public static long askRunId(){
        System.out.print("Enter run ID to view     : "); return Long.parseLong(in.nextLine());
    }
    public static void showReport(Map<String,Object> json){
        System.out.println(ReportFormatter.format(json));
    }
    public static String mainMenu(){
        System.out.println("\n--- Main Menu ---");
        System.out.println("1) Run simulation");
        System.out.println("2) List past runs");
        System.out.println("3) View run report");
        System.out.println("q) Quit");
        System.out.print("> ");
        return in.nextLine().trim();
    }
}
