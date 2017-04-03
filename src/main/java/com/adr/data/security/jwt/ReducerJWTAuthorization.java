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
    
    public final static String AUTHORIZATION_REQUEST = "AUTHORIZATION_REQUEST";
    
    private final Set<String> anonymousresources; // resources everybody logged or not has access
    private final Set<String> authenticatedresources; // resources everybody logged has access
    
    public ReducerJWTAuthorization(Set<String> anonymousresources, Set<String> authenticatedresources) {
        this.anonymousresources = anonymousresources;
        this.authenticatedresources = authenticatedresources;
    }
        
    @Override
    public List<Record> query(QueryLink link, Values headers, Record filter) throws DataException {

        String entity = filter.getKey().get("__ENTITY").asString();
        if (!AUTHORIZATION_REQUEST.equals(entity)) {          
            return null;
        }
        
        String resource = filter.getString("RESOURCE");
        String action = filter.getString("ACTION");
            
        Variant token = headers.get("token");        
        String role;
        
        if (token.isNull()) {
            role = "ANONYMOUS";
        } else {
            JWT jwttoken = JWT.decode(token.asString());         
            role = jwttoken.getClaim("role").asString();
        }
        Record response = new RecordMap(
                new ValuesMap(
                    new Entry("__ENTITY", AUTHORIZATION_REQUEST)),
                new ValuesMap(
                    new Entry("RESOURCE", resource),
                    new Entry("ACTION", action),
                    new Entry("ROLE", role),
                    new Entry("RESULT", new VariantBoolean(hasAuthorization(link, role, resource, action)))));
        return Collections.singletonList(response);            
    }
    
    private boolean hasAuthorization(QueryLink link, String role, String resource, String action) throws DataException {
        return hasAuthorizationForString(link, role, resource)
                || (action != null && !action.isEmpty() && hasAuthorizationForString(link, role, resource + action));
    }    
    
    private boolean hasAuthorizationForString(QueryLink link, String role, String resource) throws DataException {

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
                    new Entry("__ENTITY", "SUBJECT_BYROLE")},
                new Entry[]{
                    new Entry("ROLE__PARAM", role),
                    new Entry("CODE__PARAM", resource),
                    new Entry("HASPERMISSION", VariantBoolean.NULL),
                    new Entry("NAME", VariantString.NULL)});
        return link.find(subjectsquery) != null;    
    }    
}
