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
public class MapValue {
    
    private Map<String, MapValueEntry> entries;
    
    public MapValue(MapValueEntry... entries) {
         this.entries = Stream.of(entries).collect(Collectors.toMap(MapValueEntry::getName, Function.identity()));
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
    
    public MapValueEntry[] getEntries() {
        return entries.values().stream().toArray(MapValueEntry[]::new);
    }
    
    public final static MapValue fromJSON(String json) {
        return JSONBuilder.INSTANCE.fromJSON(json, MapValue.class);
    }
    
    public final String toJSON() {
        return JSONBuilder.INSTANCE.toJSON(this);
    }   
}
