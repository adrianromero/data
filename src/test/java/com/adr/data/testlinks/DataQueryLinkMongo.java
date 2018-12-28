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
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class DataQueryLinkMongo implements DataQueryLinkBuilder {
    
    private static final Logger LOG = Logger.getLogger(DataQueryLinkMongo.class.getName());  

    private MongoClient mongoclient;
    private QueryLink querylink;
    private DataLink datalink;

    @Override
    public void create() {
        mongoclient = MongoClients.create(MongoClientSettings.builder().applyConnectionString(new ConnectionString("mongodb://localhost")).build());
        
        MongoDatabase database = mongoclient.getDatabase("testdb");       
        datalink = new MongoDataLink(database);
        querylink = new MongoQueryLink(database);
    }
    
    @Override
    public void destroy() {
        datalink = null;
        querylink = null;       
        mongoclient.close();
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
