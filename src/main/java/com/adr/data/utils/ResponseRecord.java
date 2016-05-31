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
public class ResponseRecord extends EnvelopeResponse {
    
    public static final String NAME = "RECORD";
    
    private final Record result;
    
    public ResponseRecord(Record result) {
        this.result = result;
    }
    
    public Record getResult() {
        return result;
    }

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public JsonElement dataToJSON() {
        return JSONSerializer.INSTANCE.toJsonElement(result);
    }
}
