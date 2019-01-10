//     Data Access is a Java library to store data
//     Copyright (C) 2018 Adrián Romero Corchado.
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

import com.adr.data.QueryLink;
import com.adr.data.mongo.MongoCommandLink;
import com.adr.data.mongo.MongoQueryLink;
import com.adr.data.route.ReducerCommandIdentity;
import com.adr.data.route.ReducerCommandLink;
import com.adr.data.route.ReducerQueryIdentity;
import com.adr.data.route.ReducerQueryLink;
import com.adr.data.security.jwt.ReducerDataJWTAuthorization;
import com.adr.data.security.jwt.ReducerDataJWTVerify;
import com.adr.data.security.jwt.ReducerJWTCurrentUser;
import com.adr.data.security.jwt.ReducerJWTLogin;
import com.adr.data.security.jwt.ReducerQueryJWTAuthorization;
import com.adr.data.security.jwt.ReducerQueryJWTVerify;
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
import com.adr.data.CommandLink;

/**
 *
 * @author adrian
 */
public class CommandQueryLinkMongo implements CommandQueryLinkBuilder {
    
    private static final Logger LOG = Logger.getLogger(CommandQueryLinkMongo.class.getName());  

    private final MongoClient mongoclient;

    private QueryLink querylink;
    private CommandLink commandlink;
    
    public CommandQueryLinkMongo() {
        mongoclient = MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost"))
                .credential(MongoCredential.createCredential("myuser", "myuser", "mysecret".toCharArray())).build());       
    }

    @Override
    public void create() {    
        MongoDatabase database = mongoclient.getDatabase("myuser");
        
        QueryLink mongoquerylink = new MongoQueryLink(database);
        CommandLink mongocommandlink = new MongoCommandLink(database);

        querylink = new ReducerQueryLink(
                new ReducerQueryJWTVerify("secret".getBytes(StandardCharsets.UTF_8)),
                new ReducerJWTLogin(mongoquerylink, "secret".getBytes(StandardCharsets.UTF_8), 5000),
                new ReducerJWTCurrentUser(),
                new ReducerQueryJWTAuthorization(mongoquerylink, new HashSet<>(Arrays.asList("ANONYMOUS_VISIBLE_QUERY")), new HashSet<>(Arrays.asList("AUTHENTICATED_VISIBLE_QUERY"))),
                new ReducerQueryIdentity(mongoquerylink));
        
        commandlink = new ReducerCommandLink(
                new ReducerDataJWTVerify("secret".getBytes(StandardCharsets.UTF_8)),
                new ReducerDataJWTAuthorization(mongoquerylink, new HashSet<>(Arrays.asList("ANONYMOUS_VISIBLE_QUERY")), new HashSet<>(Arrays.asList("AUTHENTICATED_VISIBLE_QUERY"))),
                new ReducerCommandIdentity(mongocommandlink));       
    }
    
    @Override
    public void destroy() {
        commandlink = null;
        querylink = null;       
    }    

    @Override
    public QueryLink getQueryLink() {
        return querylink;
    }

    @Override
    public CommandLink getCommandLink() {
        return commandlink;
    }
}
