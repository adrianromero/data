/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.utils;

import com.adr.data.DataList;
import com.google.gson.JsonElement;

/**
 *
 * @author adrian
 */
public class RequestExecute extends EnvelopeRequest {
    
    public static final String NAME = "EXECUTE";
    
    private final DataList datalist;
    
    public RequestExecute(DataList datalist) {
        this.datalist = datalist;
    }
    
    public DataList getDataList() {
        return datalist;
    }

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public EnvelopeResponse process(ProcessRequest proc) {
        return proc.execute(this);
    }  

    @Override
    public JsonElement dataToJSON() {
        return JSONSerializer.INSTANCE.toJsonElement(datalist);
    }     
}
