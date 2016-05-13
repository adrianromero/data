/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data;

/**
 *
 * @author adrian
 */
public class KeyValue {
    
    private final MapValue key;
    private final MapValue value;
    
    public KeyValue(MapValue key, MapValue value) {
        this.key = key;
        this.value = value;
    }
    
    public MapValue getKey() {
        return key;
    }

    public MapValue getValue() {
        return value;
    } 
    
    public final static KeyValue fromJSON(String json) {
        return JSONBuilder.INSTANCE.fromJSON(json, KeyValue.class);
    }
    
    public final String toJSON() {
        return JSONBuilder.INSTANCE.toJSON(this);
    }    
}
