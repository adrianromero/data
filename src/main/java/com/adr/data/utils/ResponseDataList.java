/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.utils;

import com.adr.data.DataException;
import com.adr.data.DataList;
import com.google.gson.JsonElement;

/**
 *
 * @author adrian
 */
public class ResponseDataList extends EnvelopeResponse {
    
    public static final String NAME = "DATALIST";
    
    private final DataList result;
    
    public ResponseDataList(DataList result) {
        this.result = result;
    }
    
    public DataList getResult() {
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

    @Override
    public DataList getAsDataList() throws DataException {
        return result;
    }   
}
