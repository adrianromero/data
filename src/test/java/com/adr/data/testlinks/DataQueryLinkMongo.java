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

import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.adr.data.mongo.MongoDataLink;
import com.adr.data.mongo.MongoQueryLink;
import com.adr.data.route.ReducerDataIdentity;
import com.adr.data.route.ReducerDataLink;
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

/**
 *
 * @author adrian
 */
public class DataQueryLinkMongo implements DataQueryLinkBuilder {
    
    private static final Logger LOG = Logger.getLogger(DataQueryLinkMongo.class.getName());  

    private final MongoClient mongoclient;

    private QueryLink querylink;
    private DataLink datalink;
    
    public DataQueryLinkMongo() {
        mongoclient = MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost"))
                .credential(MongoCredential.createCredential("myuser", "myuser", "mysecret".toCharArray())).build());       
    }

    @Override
    public void create() {    
        MongoDatabase database = mongoclient.getDatabase("myuser");
        
        QueryLink mongoquerylink = new MongoQueryLink(database);
        DataLink mongodatalink = new MongoDataLink(database);

        querylink = new ReducerQueryLink(
                new ReducerQueryJWTVerify("secret".getBytes(StandardCharsets.UTF_8)),
                new ReducerJWTLogin(mongoquerylink, "secret".getBytes(StandardCharsets.UTF_8), 5000),
                new ReducerJWTCurrentUser(),
                new ReducerQueryJWTAuthorization(mongoquerylink, new HashSet<>(Arrays.asList("ANONYMOUS_VISIBLE_QUERY")), new HashSet<>(Arrays.asList("AUTHENTICATED_VISIBLE_QUERY"))),
                new ReducerQueryIdentity(mongoquerylink));
        
        datalink = new ReducerDataLink(
                new ReducerDataJWTVerify("secret".getBytes(StandardCharsets.UTF_8)),
                new ReducerDataJWTAuthorization(mongoquerylink, new HashSet<>(Arrays.asList("ANONYMOUS_VISIBLE_QUERY")), new HashSet<>(Arrays.asList("AUTHENTICATED_VISIBLE_QUERY"))),
                new ReducerDataIdentity(mongodatalink));       
    }
    
    @Override
    public void destroy() {
        datalink = null;
        querylink = null;       
    }    

    @Override
    public QueryLink getQueryLink() {
        return querylink;
    }

    @Override
    public DataLink getDataLink() {
        return datalink;
    }
}
