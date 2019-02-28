//     Data Access is a Java library to store data
//     Copyright (C) 2018-2019 Adrián Romero Corchado.
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
package com.adr.data.testlinks;

import com.adr.data.mongo.MongoCommandLink;
import com.adr.data.mongo.MongoQueryLink;
import com.adr.data.route.ReducerIdentity;
import com.adr.data.route.ReducerLink;
import com.adr.data.security.jwt.ReducerJWTVerify;
import com.adr.data.security.jwt.ReducerJWTCurrentUser;
import com.adr.data.security.jwt.ReducerJWTLogin;
import com.adr.data.security.jwt.ReducerJWTAuthorization;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;
import com.adr.data.Link;

/**
 *
 * @author adrian
 */
public class CommandQueryLinkMongo implements CommandQueryLinkBuilder {
    
    private static final Logger LOG = Logger.getLogger(CommandQueryLinkMongo.class.getName());  

    private final MongoClient mongoclient;

    private Link querylink;
    private Link commandlink;
    
    public CommandQueryLinkMongo() {
        mongoclient = MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost"))
                .credential(MongoCredential.createCredential("myuser", "myuser", "mysecret".toCharArray())).build());       
    }

    @Override
    public void create() {    
        MongoDatabase database = mongoclient.getDatabase("myuser");
        
        Link mongoquerylink = new MongoQueryLink(database);
        Link mongocommandlink = new MongoCommandLink(database);

        querylink = new ReducerLink(
                new ReducerJWTVerify("secret".getBytes(StandardCharsets.UTF_8)),
                new ReducerJWTLogin(mongoquerylink, "secret".getBytes(StandardCharsets.UTF_8), 5000),
                new ReducerJWTCurrentUser(),
                new ReducerJWTAuthorization(mongoquerylink, "QUERY", new HashSet<>(Arrays.asList("ANONYMOUS_VISIBLE_QUERY")), new HashSet<>(Arrays.asList("AUTHENTICATED_VISIBLE_QUERY"))),
                new ReducerIdentity(mongoquerylink));
        
        commandlink = new ReducerLink(
                new ReducerJWTVerify("secret".getBytes(StandardCharsets.UTF_8)),
                new ReducerJWTAuthorization(mongoquerylink, "EXECUTE", new HashSet<>(Arrays.asList("ANONYMOUS_VISIBLE_QUERY")), new HashSet<>(Arrays.asList("AUTHENTICATED_VISIBLE_QUERY"))),
                new ReducerIdentity(mongocommandlink));       
    }
    
    @Override
    public void destroy() {
        commandlink = null;
        querylink = null;       
    }    

    @Override
    public Link getQueryLink() {
        return querylink;
    }

    @Override
    public Link getCommandLink() {
        return commandlink;
    }
}
