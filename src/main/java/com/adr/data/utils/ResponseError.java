/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.utils;

import com.adr.data.DataException;
import com.adr.data.DataList;
import com.adr.data.Record;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 *
 * @author adrian
 */
public class ResponseError extends EnvelopeResponse {
     
    public static final String NAME = "ERROR";
    
    private final Throwable ex;
    
    public ResponseError(Throwable ex) {
        this.ex = ex;
    }
    
    public Throwable getException() {
        return ex;
    }

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public JsonElement dataToJSON() {
        return new JsonPrimitive(ex.toString());
    }
    
    @Override
    public Record getAsRecord() throws DataException {
        throw new DataException(ex);
    }
    
    @Override
    public DataList getAsDataList() throws DataException {
        throw new DataException(ex);
    }  
    
    @Override
    public void asSuccess() throws DataException {
        throw new DataException(ex);
    }     
}
