/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.utils;

import com.adr.data.Record;
import com.google.gson.JsonElement;

/**
 *
 * @author adrian
 */
public class RequestFind extends EnvelopeRequest {
    
    public static final String NAME = "FIND";
    
    private final Record filter;
    
    public RequestFind(Record filter) {
        this.filter = filter;
    }
    
    public Record getFilter() {
        return filter;
    }
   
    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public EnvelopeResponse process(ProcessRequest proc) {
        return proc.find(this);
    }

    @Override
    public JsonElement dataToJSON() {
        return JSONSerializer.INSTANCE.toJsonElement(filter);
    }
}
