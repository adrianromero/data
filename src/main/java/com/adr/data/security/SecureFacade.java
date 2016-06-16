/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.security;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import com.adr.data.var.VariantString;
import java.util.List;

/**
 *
 * @author adrian
 */
public class SecureFacade {
    
    private QueryLink securelink;
    
    public SecureFacade(QueryLink securelink) {
        this.securelink = securelink;
    }
    
    public Record login(String username, String password) throws DataException {
        // Convenience login method
        return securelink.find(new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", SecureLink.AUTHENTICATION_REQUEST)),
            new ValuesMap(
                new ValuesEntry("name", new VariantString(username)),
                new ValuesEntry("password", new VariantString(password)))));
    }
    
    public void logout() throws DataException {
        // Convenience logout method
        securelink.find(new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", SecureLink.AUTHENTICATION_REQUEST))));        
    }
    
    public Record current() throws DataException {
        // Convenience logout method
        return securelink.find(new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", SecureLink.AUTHENTICATION_CURRENT))));                
    }
    
    public Record saveCurrent(Record login) throws DataException {
        return securelink.find(login);
    } 
    
    public boolean hasAuthorization(String resource, String action) throws DataException {
        Record result = securelink.find(new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", SecureLink.AUTHORIZATION_REQUEST)),
            new ValuesMap(
                new ValuesEntry("resource", new VariantString(resource)),
                new ValuesEntry("action", new VariantString(action)))));
        
        return result.getBoolean("result");            
    } 
    
    public boolean hasAuthorization(String resource) throws DataException {
        return hasAuthorization(resource, null);
    }
    
    public List<Record> getCurrentRoleAuthorizations() throws DataException {
        // Convenience logout method
        return securelink.query(new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", SecureLink.AUTHORIZATIONS_QUERY))));           
    }   
}
