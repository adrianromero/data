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
import com.adr.data.FilterBuilder;
import com.adr.data.FilterBuilderMethods;
import com.adr.data.record.Header;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.adr.data.var.Variant;
import com.google.common.collect.ImmutableList;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.adr.data.Link;
import com.adr.data.varrw.Variants;

/**
 *
 * @author adrian
 */
public class MongoQueryLink implements Link {

    private final MongoDatabase database;
    private final String defaultcollection;

    public MongoQueryLink(MongoDatabase database) {
        this(database, "default_collection");
    }
    public MongoQueryLink(MongoDatabase database, String defaultcollection) {
        this.database = database;
        this.defaultcollection = defaultcollection;
    }

    @Override
    public List<Record> process(Header headers, List<Record> records) throws DataException {

        ImmutableList.Builder<Record> result = ImmutableList.<Record>builder();
        for (Record filter: records) { 
            int limit = Records.getLimit(filter);
            int offset = Records.getOffset(filter);
            String entity = Records.getCollection(filter, defaultcollection);
            MongoCollection<Document> collection = database.getCollection(entity);

            MongoBuilder builder = new MongoBuilder();
            FilterBuilderMethods.build(builder, filter);
            FindIterable<Document> iter = collection
                    .find(builder.getFilterFor(filter))
                    .sort(orderBy(filter))
                    .skip(offset)
                    .limit(limit)
                    .projection(builder.getProjection());

            try (MongoCursor<Document> cursor = iter.iterator()) {
                while (cursor.hasNext()) {
                    result.add(read(cursor.next(), filter));
                }
            }
        }
        return result.build();
        
    }
    
    protected static Bson orderBy(Record record) throws DataException {
        String[] orderby = Records.getOrderBy(record);
        if (orderby.length > 0) {
            List<Bson> sorts = new ArrayList<>();
            for (int i = 0; i < orderby.length; i++) {
                String s = orderby[i];
                if (s.endsWith("$ASC")) {
                    sorts.add(Sorts.ascending(s.substring(0, s.length() - 4)));
                } else if (s.endsWith("$DESC")) {
                    sorts.add(Sorts.descending(s.substring(0, s.length() - 5)));                
                } else {
                    sorts.add(Sorts.ascending(s));    
                }
            }
            return Sorts.orderBy(sorts);
        } else {
            return new BsonDocument();
        }      
    }

    protected static Record read(Document d, Record param) throws DataException {
        if (param == null) {
            return Record.EMPTY;
        }
        Record.Builder fields = Record.builder();
        for (Map.Entry<String, Variant> e : param.entrySet()) {
            if ("COLLECTION.KEY".equals(e.getKey())) {
                fields.entry(e.getKey(), e.getValue());
            } else if (!e.getKey().contains("..")) { // Is a field
                DocumentResults results = new DocumentResults(d, e.getKey());
                Variant newv = Variants.read(results, e.getValue().getKind());
                fields.entry(e.getKey(), newv);
            }
        }
        return fields.build();
    }

    private static class MongoName {

        public final String name;
        public final String realname;

        public MongoName(String name, String realname) {
            this.name = name;
            this.realname = realname;
        }
    }

    private static class MongoBuilder implements FilterBuilder {

        private final List<MongoName> names = new ArrayList<>();
        private final List<FunctionFilter> builders = new ArrayList<>();

        public Bson getProjection() {
            return Projections.include(names.stream().map(n -> n.realname).toArray(String[]::new));
        }

        public Bson getFilterFor(Record filter) throws DataException {
            if (builders.isEmpty()) {
                return new BsonDocument();
            } else {
                List<Bson> filterslist = new ArrayList<>();
                for (FunctionFilter b : builders) {
                    b.apply(filterslist, filter);
                }
                return Filters.and(filterslist);
            }
        }

        private void filterCriteria(String name, String realname, FunctionFilter f) {
            if (name.equals("COLLECTION.KEY")) {
                return;
            }
            builders.add(f);
        }

        @Override
        public void project(String name, String realname) {
            if (name.equals("COLLECTION.KEY")) {
                return;
            }
            names.add(new MongoName(name, realname));
        }

        @Override
        public void filterEqual(String name, String realname) {
            filterCriteria(name, realname, (filters, r) -> {
                filters.add(Filters.eq(realname, r.get(name).asObject()));
            });
        }

        @Override
        public void filterDistinct(String name, String realname) {
            filterCriteria(name, realname, (filters, r) -> {
                filters.add(Filters.ne(realname, r.get(name).asObject()));
            });
        }

        @Override
        public void filterGreater(String name, String realname) {
            filterCriteria(name, realname, (filters, r) -> {
                filters.add(Filters.gt(realname, r.get(name).asDouble()));
            });
        }

        @Override
        public void filterGreaterOrEqual(String name, String realname) {
            filterCriteria(name, realname, (filters, r) -> {
                filters.add(Filters.gte(realname, r.get(name).asDouble()));
            });
        }

        @Override
        public void filterLess(String name, String realname) {
            filterCriteria(name, realname, (filters, r) -> {
                filters.add(Filters.lt(realname, r.get(name).asDouble()));
            });
        }

        @Override
        public void filterLessOrEqual(String name, String realname) {
            filterCriteria(name, realname, (filters, r) -> {
                filters.add(Filters.lte(realname, r.get(name).asDouble()));
            });
        }

        @Override
        public void filterContains(String name, String realname) {
            filterCriteria(name, realname, (filters, r) -> {
                filters.add(Filters.regex(realname, ".*" + Pattern.quote(r.get(name).asString()) + ".*"));
            });
        }

        @Override
        public void filterStarts(String name, String realname) {
            filterCriteria(name, realname, (filters, r) -> {
                filters.add(Filters.regex(realname, Pattern.quote(r.get(name).asString()) + ".*"));
            });
        }

        @Override
        public void filterEnds(String name, String realname) {
            filterCriteria(name, realname, (filters, r) -> {
                filters.add(Filters.regex(realname, ".*" + Pattern.quote(r.get(name).asString())));
            });
        }
    }
}
