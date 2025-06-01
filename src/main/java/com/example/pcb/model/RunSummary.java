// RunSummary.java  – minimal listing DTO
package com.example.pcb.model;
public record RunSummary(long id,String pcbType,int qty,int produced,int failed,String ts){}
