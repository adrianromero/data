/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.utils;

/**
 *
 * @author adrian
 */
public abstract class ProcessRequest {
    
    public EnvelopeResponse find(RequestFind req) {
        return other(req);
    }
    
    public EnvelopeResponse query(RequestQuery req) {
        return other(req);
    }  
    
    public EnvelopeResponse execute(RequestExecute req) {
        return other(req);
    }  
    
    public EnvelopeResponse other(EnvelopeRequest env) {
        throw new UnsupportedOperationException("Request type not supported: " + env.getType());    
    }
}
