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
//
package com.adr.data.mongo;

import com.adr.data.DataException;
import com.adr.data.record.Header;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.adr.data.varrw.Parameters;
import com.adr.data.var.Variant;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.util.ArrayList;

import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.adr.data.var.VariantInt;
import com.google.common.collect.ImmutableList;
import java.util.Map;
import com.adr.data.Link;
import com.adr.data.varrw.Variants;

/**
 *
 * @author adrian
 */
public class MongoCommandLink implements Link {

    private final static ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    private final MongoDatabase database;
    private final String defaultcollection;

    public MongoCommandLink(MongoDatabase database) {
        this(database, "default_collection");
    }
    
    public MongoCommandLink(MongoDatabase database, String defaultcollection) {
        this.database = database;
        this.defaultcollection = defaultcollection;
    }

    @Override
    public List<Record> process(Header headers, List<Record> records) throws DataException {

        for (Record r : records) {
            String entity = Records.getCollection(r, defaultcollection);
            MongoCollection<Document> collection = database.getCollection(entity);
            
            List<Bson> filters = new ArrayList<>();
            if (Records.isDeleteSentence(r)) {
                for (Map.Entry<String, Variant> entry : r.entrySet()) {
                    String realname;
                    if (!entry.getKey().equals("COLLECTION.KEY") && !entry.getKey().contains("..")) {
                        if (entry.getKey().endsWith(".KEY")) {
                            realname = entry.getKey().substring(0, entry.getKey().length() - 4);
                            filters.add(Filters.eq(realname, entry.getValue().asObject()));
                        }
                    }
                }                
                collection.deleteOne(Filters.and(filters));
            } else {
                Document doc = new Document();            
                for (Map.Entry<String, Variant> entry : r.entrySet()) {
                    String realname;
                    if (!entry.getKey().equals("COLLECTION.KEY") && !entry.getKey().contains("..")) {
                        if (entry.getKey().endsWith(".KEY")) {
                            realname = entry.getKey().substring(0, entry.getKey().length() - 4);
                            filters.add(Filters.eq(realname, entry.getValue().asObject()));
                        } else {
                            realname = entry.getKey();
                        }
                        Parameters p = new DocumentParameters(doc, realname);
                        Variants.write(p, entry.getValue().getKind(), entry.getValue());
                    }
                }
                collection.replaceOne(Filters.and(filters), doc, UPSERT);
            } 
        }
        return ImmutableList.of(new Record(
                Record.entry("PROCESSED", new VariantInt(records.size()))));        
    }
}
