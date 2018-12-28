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
//
package com.adr.data.mongo;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.record.Header;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.adr.data.var.Parameters;
import com.adr.data.var.Variant;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.util.ArrayList;

import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author adrian
 */
public class MongoDataLink implements DataLink {

    private final static ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    private final MongoDatabase database;

    public MongoDataLink(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public void execute(Header headers, List<Record> records) throws DataException {

        for (Record r : records) {
            String entity = Records.getCollection(r);
            MongoCollection<Document> collection = database.getCollection(entity);

            Document doc = new Document();
            List<Bson> filters = new ArrayList<>();
            for (String f : r.getNames()) {
                String realname;
                if (!f.equals("COLLECTION.KEY") && !f.contains("..")) {
                    Variant v = r.get(f);
                    if (f.endsWith(".KEY")) {
                        realname = f.substring(0, f.length() - 4);
                        filters.add(Filters.eq(realname, v.asObject()));
                    } else {
                        realname = f;
                    }
                    Parameters p = new DocumentParameters(doc, realname);
                    v.getKind().write(p, v);
                }
            }
            collection.replaceOne(Filters.and(filters), doc, UPSERT);
        }
    }
}
