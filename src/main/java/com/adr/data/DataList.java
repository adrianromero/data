/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author adrian
 */
public class DataList {
    
    private final List<KeyValue> data = new ArrayList<>();
       
    public DataList(KeyValue ... keyval) {
        data.addAll(Arrays.asList(keyval));
    }
    
    public List<KeyValue> getData() {
        return data;
    }
    
    public final static DataList fromJSON(String json) {
        return JSONBuilder.INSTANCE.fromJSON(json, DataList.class);
    }
    
    public final String toJSON() {
        return JSONBuilder.INSTANCE.toJSON(this);
    }
}
