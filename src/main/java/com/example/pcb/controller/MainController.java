// MainController.java
package com.example.pcb.controller;

import com.example.pcb.model.*;
import com.example.pcb.view.*;

import java.util.List;
import java.util.Map;

public final class MainController implements AutoCloseable {
    private final RunRepository repo;
    public MainController(RunRepository repo){ this.repo=repo; }

    public void runLoop() throws Exception{
        outer:
        while(true){
            switch(CliViews.mainMenu()){
                case "1": runSimulation();     break;
                case "2": listRuns();          break;
                case "3": viewReport();        break;
                case "q": break outer;
                default : System.out.println("Unknown choice.");
            }
        }
    }

    private void runSimulation() throws Exception{
        PCBFactory.Type type = PCBFactory.Type.valueOf(CliViews.askType());
        int qty = CliViews.askQuantity();
        Simulation sim = new Simulation(PCBFactory.create(type));
        Result res     = sim.run(qty);
        long id        = repo.save(type,qty,res);
        System.out.printf("Run saved with id %d (%d produced, %d failed).%n",
                          id,res.totalProduced,res.totalFailed);
    }

    private void listRuns() throws Exception{
        List<RunSummary> lst = repo.list();
        if(lst.isEmpty()) System.out.println("No runs in database yet.");
        else              CliViews.listRuns(lst);
    }

    private void viewReport() throws Exception {
        long id   = CliViews.askRunId();
        String js = repo.findById(id);          // now returns JSON directly
        if (js == null) {
            System.out.println("Run not found.");
            return;
        }
        Map<String,Object> map = JsonParser.parse(js);
        CliViews.showReport(map);
    }

    @Override public void close() throws Exception{ repo.close(); }
}
