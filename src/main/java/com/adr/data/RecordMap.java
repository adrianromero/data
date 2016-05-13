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
public class RecordMap {
    
    private final ValuesMap key;
    private final ValuesMap value;
    
    public RecordMap(ValuesMap key, ValuesMap value) {
        this.key = key;
        this.value = value;
    }
    
    public ValuesMap getKey() {
        return key;
    }

    public ValuesMap getValue() {
        return value;
    } 
    
    public final static RecordMap fromJSON(String json) {
        return JSONBuilder.INSTANCE.fromJSON(json, RecordMap.class);
    }
    
    public final String toJSON() {
        return JSONBuilder.INSTANCE.toJSON(this);
    }    
}
