/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.security;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.recordmap.Entry;
import com.adr.data.record.Record;
import com.adr.data.record.Values;
import com.adr.data.recordmap.RecordMap;
import com.adr.data.route.ReducerQuery;
import com.adr.data.var.Variant;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author adrian
 */
public abstract class ReducerLogin implements ReducerQuery {
    
    public final static String AUTHENTICATION_REQUEST = "AUTHENTICATION_REQUEST";
    public final static String AUTHENTICATION_RESPONSE = "AUTHENTICATION_RESPONSE";
    
    protected abstract Variant createToken(QueryLink link, String user, String password) throws DataException;
    
    @Override
    public List<Record> query(QueryLink link, Values headers, Record filter) throws DataException {
        
        String entity = filter.getKey().get("__ENTITY").asString();
        if (!AUTHENTICATION_REQUEST.equals(entity)) {          
            return null;
        }
        
        // Filter Authentication requests logins
        String username = filter.getString("NAME");
        String password = filter.getString("PASSWORD");     

        Variant token = createToken(link, username, password);

        Record result = new RecordMap(
                        new Entry[]{
                            new Entry("__ENTITY", AUTHENTICATION_RESPONSE)},
                        new Entry[]{
                            new Entry("TOKEN", token)});            
        return Collections.singletonList(result);          
    }    
}
