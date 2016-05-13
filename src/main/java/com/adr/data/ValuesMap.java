/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author adrian
 */
public class ValuesMap {
    
    private Map<String, ValuesEntry> entries;
    
    public ValuesMap(ValuesEntry... entries) {
         this.entries = Stream.of(entries).collect(Collectors.toMap(ValuesEntry::getName, Function.identity()));
    }
        
    public String [] getNames() {
        return entries.keySet().stream().toArray(String[]::new);
    }
    
    public Object getValue(String name) {
        return entries.get(name).getValue();
    }
    
    public Kind getKind(String name) {
        return entries.get(name).getKind();
    }  
    
    public ValuesEntry[] getEntries() {
        return entries.values().stream().toArray(ValuesEntry[]::new);
    }
    
    public final static ValuesMap fromJSON(String json) {
        return JSONBuilder.INSTANCE.fromJSON(json, ValuesMap.class);
    }
    
    public final String toJSON() {
        return JSONBuilder.INSTANCE.toJSON(this);
    }   
}
