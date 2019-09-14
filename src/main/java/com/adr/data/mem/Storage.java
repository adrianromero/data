//     Data Access is a Java library to store data
//     Copyright (C) 2019 Adrián Romero Corchado.
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
package com.adr.data.mem;

import com.adr.data.FilterBuilderMethods;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.adr.data.var.Variant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.adr.data.FilterBuilder;
import com.google.common.collect.ImmutableList;
import java.util.Map;

public class Storage {

    private final LinkedHashMap<Record, Record> collection = new LinkedHashMap<>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    private void put(Record record) throws IOException {
        Record.Builder fields = Record.builder();
        Record.Builder key = Record.builder();
        String realname;
        boolean isDelete = true;
        for (Map.Entry<String, Variant> e : record.entrySet()) {
            if (!e.getKey().contains("..")) {
                if (e.getKey().endsWith(".KEY")) {
                    realname = e.getKey().substring(0, e.getKey().length() - 4);
                    key.entry(realname, e.getValue());
                } else {
                    realname = e.getKey();
                    isDelete = false;
                }
                fields.entry(realname, e.getValue());
            }
        }
        if (isDelete) {
            collection.remove(key.build());
        } else {
            collection.put(key.build(), fields.build());
        }
    }

    public void put(List<Record> records) throws IOException {
        w.lock();
        try {
            for (Record record : records) {
                put(record);
            }
        } finally {
            w.unlock();
        }
    }

    public void query(Record filter, ImmutableList.Builder<Record> result) throws IOException {
        r.lock();
        int i = 0;
        int limit = Records.getLimit(filter);
        int offset = Records.getOffset(filter);
        try {

            StorageBuilder builder = new StorageBuilder();
            FilterBuilderMethods.build(builder, filter);

            for (Record record : collection.values()) {
                // Record p = project(filter, r);
                Record p = builder.filterAndProject(record, filter);
                if (p != null) {
                    // Matches...
                    if (offset > 0) {
                        offset--;
                    } else {
                        result.add(p);
                        if (--limit <= 0) {
                            return;
                        }
                    }

                }
            }
        } finally {
            r.unlock();
        }
    }

    private static class StorageName {

        public final String name;
        public final String realname;

        public StorageName(String name, String realname) {
            this.name = name;
            this.realname = realname;
        }
    }

    private static class StorageBuilder implements FilterBuilder {

        private List<StorageName> names = new ArrayList<>();
        private PredicateFilter p = (r, filter) -> true;

        public Record filterAndProject(Record r, Record filter) {
            if (p.test(r, filter)) {
                Record.Builder fields = Record.builder();
                for (StorageName n : names) {
                    fields.entry(n.name, r.get(n.realname));
                }
                return fields.build();
            } else {
                return null;
            }
        }

        @Override
        public void project(String name, String realname) {
            names.add(new StorageName(name, realname));
        }

        @Override
        public void filterEqual(String name, String realname) {
            p = p.and((r, filter) -> r.get(realname).equals(filter.get(name)));
        }

        @Override
        public void filterDistinct(String name, String realname) {
            p = p.and((r, filter) -> !r.get(realname).equals(filter.get(name)));
        }

        @Override
        public void filterGreater(String name, String realname) {
            p = p.and((r, filter) -> {
                Variant v = filter.get(name);
                Variant x = r.get(realname);
                return !x.isNull() && x.asDouble() > v.asDouble();
            });
        }

        @Override
        public void filterGreaterOrEqual(String name, String realname) {
            p = p.and((r, filter) -> {
                Variant v = filter.get(name);
                Variant x = r.get(realname);
                return !x.isNull() && x.asDouble() >= v.asDouble();
            });
        }

        @Override
        public void filterLess(String name, String realname) {
            p = p.and((r, filter) -> {
                Variant v = filter.get(name);
                Variant x = r.get(realname);
                return !x.isNull() && x.asDouble() < v.asDouble();
            });
        }

        @Override
        public void filterLessOrEqual(String name, String realname) {
            p = p.and((r, filter) -> {
                Variant v = filter.get(name);
                Variant x = r.get(realname);
                return !x.isNull() && x.asDouble() <= v.asDouble();
            });
        }

        @Override
        public void filterContains(String name, String realname) {
            p = p.and((r, filter) -> {
                Variant v = filter.get(name);
                Variant x = r.get(realname);
                return !x.isNull() && x.asString().contains((v.asString()));
            });
        }

        @Override
        public void filterStarts(String name, String realname) {
            p = p.and((r, filter) -> {
                Variant v = filter.get(name);
                Variant x = r.get(realname);
                return !x.isNull() && x.asString().startsWith((v.asString()));
            });
        }

        @Override
        public void filterEnds(String name, String realname) {
            p = p.and((r, filter) -> {
                Variant v = filter.get(name);
                Variant x = r.get(realname);
                return !x.isNull() && x.asString().endsWith((v.asString()));
            });
        }
    }
}
