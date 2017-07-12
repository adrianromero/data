/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.security.jwt;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.record.Record;
import com.adr.data.record.Values;
import com.adr.data.recordmap.Entry;
import com.adr.data.recordmap.RecordMap;
import com.adr.data.recordmap.ValuesMap;
import com.adr.data.route.ReducerQuery;
import com.adr.data.security.ReducerLogin;
import com.adr.data.security.SecurityDataException;
import com.adr.data.var.Variant;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantString;
import com.auth0.jwt.JWT;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 * @author adrian
 */
public class ReducerJWTAuthorization implements ReducerQuery {
    
    public final static String ACTION_QUERY = "_QUERY";
    public final static String ACTION_EXECUTE = "_EXECUTE";

    
    private final Set<String> anonymousresources; // resources everybody logged or not has access
    private final Set<String> authenticatedresources; // resources everybody logged has access
    
    public ReducerJWTAuthorization(Set<String> anonymousresources, Set<String> authenticatedresources) {
        this.anonymousresources = anonymousresources;
        this.authenticatedresources = authenticatedresources;
    }
        
    @Override
    public List<Record> query(QueryLink link, Values headers, Record filter) throws DataException {
            
        Variant authorization = headers.get("Authorization");        
        String role;
        String roledisplay;
        if (authorization.isNull()) {
            role = "ANONYMOUS";
            roledisplay = "Anonymous";
        } else {
            JWT jwtauthorization = JWT.decode(authorization.asString());         
            role = jwtauthorization.getClaim("role").asString();
            roledisplay = jwtauthorization.getClaim("roledisplay").asString();
        }
        
        String entity = filter.getKey().get("__ENTITY").asString();
        if (ReducerLogin.AUTHORIZATION_REQUEST.equals(entity)) {        
            // Request authorization
            String resource = filter.getString("RESOURCE");
            Record response = new RecordMap(
                    new ValuesMap(
                        new Entry("__ENTITY", ReducerLogin.AUTHORIZATION_REQUEST)),
                    new ValuesMap(
                        new Entry("RESOURCE", resource),
                        new Entry("ROLE", role),
                        new Entry("RESULT", new VariantBoolean(hasAuthorization(link, role, resource)))));
            return Collections.singletonList(response);             
        } else {
            // Normal query
            if (hasAuthorization(link, role, entity + ACTION_QUERY)) {
                return null;
            } else {
                throw new SecurityDataException("Role " + roledisplay + " does not have authorization to query the resource: " + entity);
            }            
        }          
    }  
    
    private boolean hasAuthorization(QueryLink link, String role, String resource) throws DataException {
        
        // Everybody logged or not has access to anonymous resources
        if (anonymousresources.contains(resource)) {
            return true;
        }

        // if not logged it does not have permissions to anything else
        if ("ANONYMOUS".equals(role)) {
            return false;
        }

        // Everybody logged has permissions to authenticated resources
        if (authenticatedresources.contains(resource)) {
            return true;
        }

        // Admin has permissions to everything
        if ("ADMIN".equals(role)) {
            return true;
        }

        // Valid login, load user details.
        Record subjectsquery = new RecordMap(
                new Entry[]{
                    new Entry("__ENTITY", "ROLE_SUBJECT"),
                    new Entry("ROLE__PARAM", role),
                    new Entry("SUBJECT__PARAM", resource)},
                new Entry[]{
                    new Entry("NAME", VariantString.NULL),
                    new Entry("DISPLAYNAME", VariantString.NULL)});
        return link.find(subjectsquery) != null;    
    }    
}
