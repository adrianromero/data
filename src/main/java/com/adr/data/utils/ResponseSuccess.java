/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.utils;

import com.adr.data.DataException;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

/**
 *
 * @author adrian
 */
public class ResponseSuccess extends EnvelopeResponse {
     
    public static final String NAME = "SUCCESS";

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public JsonElement dataToJSON() {
        return JsonNull.INSTANCE;
    }    
    
    @Override
    public void asSuccess() throws DataException {
        // Success, do nothing
    }     
}
