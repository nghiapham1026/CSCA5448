// MainApp.java

package com.example.pcb;

import com.example.pcb.controller.MainController;
import com.example.pcb.model.SQLiteRunRepository;

public final class MainApp {
    public static void main(String[] args) throws Exception{
        try(MainController c = new MainController(new SQLiteRunRepository())){
            c.runLoop();
        }
        System.out.println("Bye.");
    }
}
