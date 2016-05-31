/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.utils;

import com.google.gson.JsonElement;

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
        return JSONSerializer.INSTANCE.toJsonElement(ex);
    }    
}
