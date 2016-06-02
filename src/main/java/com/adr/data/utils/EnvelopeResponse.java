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

/**
 *
 * @author adrian
 */
public abstract class EnvelopeResponse {
    public abstract String getType();  
    public abstract JsonElement dataToJSON();
    
    public Record getAsRecord() throws DataException {
        throw new DataException("Record response not supported.");
    }
    public DataList getAsDataList() throws DataException {
        throw new DataException("DataList response not supported.");
    }
    public void asSuccess() throws DataException {
        throw new DataException("DataList response not supported.");
    }    
}
