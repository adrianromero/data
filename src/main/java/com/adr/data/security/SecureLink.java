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
import com.adr.data.QueryOptions;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import com.adr.data.DataQueryLink;
import com.adr.data.utils.CryptUtils;
import com.adr.data.utils.JSON;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantBytes;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author adrian
 */
public class SecureLink implements DataQueryLink {
    
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
    
    private final UserSession usersession;   
    
    public SecureLink(QueryLink querylink, DataLink datalink, Set<String> anonymousresources, Set<String> authenticatedresources, UserSession usersession) {
        this.querylink = querylink;
        this.datalink = datalink;
        this.anonymousresources = Collections.unmodifiableSet(anonymousresources);
        this.authenticatedresources = Collections.unmodifiableSet(authenticatedresources);
        
        this.usersession = usersession;
    }
    public SecureLink(QueryLink querylink, DataLink datalink, Set<String> anonymousresources, Set<String> authenticatedresources) {
        this(querylink, datalink, anonymousresources, authenticatedresources, new UserSession());
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
        if (usersession.getUser() == null) {
            return false;
        }

        // Everybody logged has permissions to authenticated resources
        if (authenticatedresources.contains(resource)) {
            return true;
        }
        
        // Admin has permissions to everything
        if (ADMIN.equals(usersession.getUser().getString("name"))) {
            return true;
        }

        return usersession.containsResource(resource, querylink);
    }
    
    @Override
    public List<Record> query(Record filter, QueryOptions options) throws DataException {
        
        String entity = filter.getKey().get("_ENTITY").asString();
        if (AUTHENTICATION_REQUEST.equals(entity)) {
            
            if (filter.getValue() == null) {
                // logout
                usersession.newUser(null);
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

                Record userrecord = querylink.find(usernamequery);

                if (userrecord == null || !CryptUtils.validatePassword(password, userrecord.getString("password"))) {
                    usersession.newUser(null);
                } else {
                    usersession.newUser(userrecord);
                }
            }
            // Return usersession user
            if (usersession.getUser() == null) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList(JSON.INSTANCE.clone(usersession.getUser()));
            }             
        } else if (AUTHENTICATION_CURRENT.equals(entity)) {
            // Return usersession user
            if (usersession.getUser() == null) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList(JSON.INSTANCE.clone(usersession.getUser()));
            } 
        } else if ("username_byname".equals(entity)) {
            // Saves usersession user
            if (usersession.getUser() == null) {
                throw new SecurityDataException("No authenticated user to save.");
            }
            if (!filter.getKey().get("id").asString().equals(usersession.getUser().getKey().get("id").asString())) {
                throw new SecurityDataException("Trying to save a user different than authenticated user.");
            }
            
            Record saveduser = JSON.INSTANCE.clone(usersession.getUser()); 
            saveduser.getValue().set("displayname", filter.getValue().get("displayname"));
            saveduser.getValue().set("password", filter.getValue().get("password"));
            saveduser.getValue().set("visible", filter.getValue().get("visible"));
            saveduser.getValue().set("image", filter.getValue().get("image"));
            
            datalink.execute(saveduser);
            
            usersession.updateUser(saveduser);
            return Collections.singletonList(JSON.INSTANCE.clone(usersession.getUser()));            
        } else if (AUTHORIZATION_REQUEST.equals(entity)) {
            String resource = filter.getString("resource"); 
            String action = filter.getString("action"); 
            Record response = JSON.INSTANCE.clone(filter);
            response.getValue().set("result", new VariantBoolean(hasAuthorization(resource, action)));
            return Collections.singletonList(response);
        } else if (AUTHORIZATIONS_QUERY.equals(entity)) {
            if (usersession.getUser() == null) {
                return Collections.emptyList();
            } else {
                return JSON.INSTANCE.clone(usersession.getSession(querylink));
            }
        } else {   
            // Normal query
            if (hasAuthorization(entity, ACTION_QUERY)) {
                return querylink.query(filter, options);
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

    @Override
    public void close() throws DataException {
        try {
            querylink.close();
        } finally {
            datalink.close();
        }
    }
    
    public static class UserSession {
        private Record user = null;
        private List<Record> session = null;
        private Set<String> sessionset = null; 
        
        public UserSession() {
        }
        
        public void read(DataInput data) throws IOException {
            user = JSON.INSTANCE.fromJSONRecord(readString(data));
            boolean hascurrentsession = data.readBoolean();
            if (hascurrentsession) {
                int size = data.readInt();
                List<Record> l = new ArrayList<>();
                for(int i = 0; i < size; i++) {
                    l.add(JSON.INSTANCE.fromJSONRecord(readString(data)));
                }
                session = l;
                sessionset = session.stream().map(r -> r.getString("code")).collect(Collectors.toSet());
            } else {
                session = null;
                sessionset = null;
            }
        }
        
        public void write(DataOutput data) throws IOException {
            writeString(data, JSON.INSTANCE.toJSON(user));
            if (session == null) {
                data.writeBoolean(false);
            } else {
                data.writeBoolean(true);
                data.writeInt(session.size());
                for (Record r : session) {
                    writeString(data, JSON.INSTANCE.toJSON(r));
                }
            }
        }

        private static void writeString(DataOutput out, String str) throws IOException {
            byte[] data=str.getBytes("UTF-8");
            out.writeInt(data.length);
            out.write(data);            
        }
        
        private static String readString(DataInput in) throws IOException {
            int length = in.readInt();
            byte[] data = new byte[length];
            in.readFully(data);
            return new String(data,"UTF-8");            
        }
        
        public void setData(byte[] session) throws IOException {
            try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(session))) {
                read(in);
            }       
        }
        
        public byte[] getData() throws IOException {
            ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
            try (DataOutputStream out = new DataOutputStream(bytearray)) {
                write(out);
            }  
            return bytearray.toByteArray();
        }


        private void newUser(Record user) {
            this.user = user;
            this.session = null;
            this.sessionset = null;
        }
        
        private void updateUser(Record user) {
            this.user = user;
        }
        
        private Record getUser() {
            return user;
        }
        
        private List<Record> getSession(QueryLink link) throws DataException {
            loadCurrentSession(link);
            return session;
        }
        
        private boolean containsResource(String resource, QueryLink link) throws DataException {
            loadCurrentSession(link);
            return sessionset == null 
                ? false 
                : sessionset.contains(resource);
        }
        
        private void loadCurrentSession(QueryLink link) throws DataException {
            // precondition currentuser != null

            if (user != null && session == null) {
                Record subjectsquery = new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "subject_byrole"),
                        new ValuesEntry("role_id::PARAM", user.getString("role_id"))),
                    new ValuesMap(
                        new ValuesEntry("code"),
                        new ValuesEntry("name")));
                session = link.query(subjectsquery);    
                sessionset = session.stream().map(r -> r.getString("code")).collect(Collectors.toSet());
            }
        }
    }    
}
