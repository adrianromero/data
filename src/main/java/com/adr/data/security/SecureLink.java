//     Data Access is a Java library to store data
//     Copyright (C) 2016 Adrián Romero Corchado.
//
//     This file is part of Data Access
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//     
//         http://www.apache.org/licenses/LICENSE-2.0
//     
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.

package com.adr.data.security;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import com.adr.data.http.AssignableSession;
import com.adr.data.utils.CryptUtils;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantBytes;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author adrian
 */
public class SecureLink implements QueryLink, DataLink, AssignableSession {
    
    public final static String ADMIN = "admin";
    public final static String AUTHENTICATION_REQUEST = "AUTHENTICATION_REQUEST";
    public final static String AUTHENTICATION_CURRENT = "AUTHENTICATION_CURRENT";
    public final static String AUTHENTICATION_SAVE = "AUTHENTICATION_SAVE";
    public final static String AUTHORIZATION_REQUEST = "AUTHORIZATION_REQUEST";
    public final static String AUTHORIZATIONS_QUERY = "AUTHORIZATIONS_QUERY";
    
    public final static String ACTION_QUERY = "_QUERY";
    public final static String ACTION_EXECUTE = "_EXECUTE";

    private final QueryLink querylink;
    private final DataLink datalink;
    private final Set<String> anonymousresources; // resources everybody logged or not has access
    private final Set<String> authenticatedresources; // resources everybody logged has access
    
    private Record currentuser = null;
    private List<Record> currentsession = null;
    private Set<String> currentsessionset = null;
    
    public SecureLink(QueryLink querylink, DataLink datalink, Set<String> anonymousresources, Set<String> authenticatedresources) {
        this.querylink = querylink;
        this.datalink = datalink;
        this.anonymousresources = Collections.unmodifiableSet(anonymousresources);
        this.authenticatedresources = Collections.unmodifiableSet(authenticatedresources);
    }
    
    @Override
    public void readSerializedSession(DataInput session) throws IOException {
        currentuser = JSONSerializer.INSTANCE.fromJSONRecord(session.readUTF());
        boolean hascurrentsession = session.readBoolean();
        if (hascurrentsession) {
            int size = session.readInt();
            currentsession = new ArrayList<>();
            for(int i = 0; i < size; i++) {
                currentsession.add(JSONSerializer.INSTANCE.fromJSONRecord(session.readUTF()));
            }
            currentsessionset = currentsession.stream().map(r -> r.getString("code")).collect(Collectors.toSet());
        } else {
            currentsession = null;
            currentsessionset = null;
        }
    }
    
    @Override
    public void writeSerializedSession(DataOutput session) throws IOException {
        session.writeUTF(JSONSerializer.INSTANCE.toJSON(currentuser));
        if (currentsession == null) {
            session.writeBoolean(false);
        } else {
            session.writeBoolean(true);
            session.writeInt(currentsession.size());
            for (Record r : currentsession) {
                session.writeUTF(JSONSerializer.INSTANCE.toJSON(currentuser));
            }
        }       
    }
            
    private boolean hasAuthorization(String resource, String action) throws DataException {
        return hasAuthorizationForString(resource) 
            || (action != null && !action.isEmpty() && hasAuthorizationForString(resource + action));
    }
    
    private boolean hasAuthorizationForString(String resource) throws DataException {
        
        // Everybody logged or not has access to anonymous resources
        if (anonymousresources.contains(resource)) {
            return true;
        }

        // if not logged it does not have permissions to anything else
        if (currentuser == null) {
            return false;
        }

        // Everybody logged has permissions to authenticated resources
        if (authenticatedresources.contains(resource)) {
            return true;
        }
        
        // Admin has permissions to everything
        if (ADMIN.equals(currentuser.getString("name"))) {
            return true;
        }
        
        loadCurrentSession();
        return currentsessionset.contains(resource);
    }
    
    private void loadCurrentSession() throws DataException {
        // precondition currentuser != null
        
        if (currentsession == null) {
            Record subjectsquery = new RecordMap(
                new ValuesMap(
                    new ValuesEntry("_ENTITY", "subject_byrole"),
                    new ValuesEntry("role_id::PARAM", currentuser.getString("role_id"))),
                new ValuesMap(
                    new ValuesEntry("code"),
                    new ValuesEntry("name")));
            currentsession = querylink.query(subjectsquery);
            currentsessionset = currentsession.stream().map(r -> r.getString("code")).collect(Collectors.toSet());           
        }
    }

    @Override
    public List<Record> query(Record filter, Map<String, String> options) throws DataException {
        
        String entity = filter.getKey().get("_ENTITY").asString();
        if (AUTHENTICATION_REQUEST.equals(entity)) {
            
            if (filter.getValue() == null) {
                // logout
                currentuser = null;
                currentsession = null;
                currentsessionset = null;
            } else {
                // login
                String username = filter.getString("name");
                String password = filter.getString("password");

                Record usernamequery = new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "username_byname"),
                        new ValuesEntry("id")),
                    new ValuesMap(
                        new ValuesEntry("name", username),
                        new ValuesEntry("displayname"),
                        new ValuesEntry("password"),
                        new ValuesEntry("codecard"),
                        new ValuesEntry("role_id"),
                        new ValuesEntry("role"),
                        new ValuesEntry("visible", VariantBoolean.NULL),
                        new ValuesEntry("image", VariantBytes.NULL)));

                List<Record> userrecord = querylink.query(usernamequery);

                if (userrecord.isEmpty() || !CryptUtils.validatePassword(password, userrecord.get(0).getString("password"))) {
                    currentuser = null;
                } else {
                    currentuser = userrecord.get(0);
                }
                currentsession = null;
                currentsessionset = null;
            }
            // Return current user
            if (currentuser == null) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList(JSONSerializer.INSTANCE.clone(currentuser));
            }             
        } else if (AUTHENTICATION_CURRENT.equals(entity)) {
            // Return current user
            if (currentuser == null) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList(JSONSerializer.INSTANCE.clone(currentuser));
            } 
        } else if ("username_byname".equals(entity)) {
            // Saves current user
            if (currentuser == null) {
                throw new SecurityDataException("No authenticated user to save.");
            }
            if (!filter.getKey().get("id").asString().equals(currentuser.getKey().get("id").asString())) {
                throw new SecurityDataException("Trying to save a user different than authenticated user.");
            }
            
            Record saveduser = JSONSerializer.INSTANCE.clone(currentuser); 
            saveduser.getValue().set("displayname", filter.getValue().get("displayname"));
            saveduser.getValue().set("password", filter.getValue().get("password"));
            saveduser.getValue().set("visible", filter.getValue().get("visible"));
            saveduser.getValue().set("image", filter.getValue().get("image"));
            
            datalink.execute(saveduser);
            
            currentuser = saveduser;
            return Collections.singletonList(JSONSerializer.INSTANCE.clone(currentuser));            
        } else if (AUTHORIZATION_REQUEST.equals(entity)) {
            String resource = filter.getString("resource"); 
            String action = filter.getString("action"); 
            Record response = JSONSerializer.INSTANCE.clone(filter);
            response.getValue().set("result", new VariantBoolean(hasAuthorization(resource, action)));
            return Collections.singletonList(response);
        } else if (AUTHORIZATIONS_QUERY.equals(entity)) {
            if (currentuser == null) {
                return Collections.emptyList();
            } else {
                loadCurrentSession();
                return JSONSerializer.INSTANCE.clone(currentsession);
            }
        } else {   
            // Normal query
            if (hasAuthorization(entity, ACTION_QUERY)) {
                return querylink.query(filter);
            } else {
                throw new SecurityDataException("No authorization to query resource: " + entity);
            }  
        }
    }

    @Override
    public void execute(List<Record> l) throws DataException {
        
        for (Record r : l) {
            String entity = r.getKey().get("_ENTITY").asString();
            if (!hasAuthorization(entity, ACTION_EXECUTE)) {
                throw new SecurityDataException("No authorization to execute resource: " + entity);
            }
        }
        
        datalink.execute(l);
    }
}
