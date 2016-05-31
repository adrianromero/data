/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.utils;

import com.adr.data.Record;

/**
 *
 * @author adrian
 */
public class RequestQuery extends EnvelopeRequest {
    
    public static final String NAME = "QUERY";
    
    private final Record filter;
    
    public RequestQuery(Record filter) {
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
        return proc.query(this);
    }    
}
