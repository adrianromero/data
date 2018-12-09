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

package com.adr.data.mem;

import com.adr.data.record.Entry;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.adr.data.recordparser.RecordsSerializer;
import com.adr.data.var.Variant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

/**
 *
 * @author adrian
 */
public class Storage {
    
    private LinkedHashMap<String, Record> collection = new LinkedHashMap<String, Record>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();    
    
    private void put(Record record) throws IOException {
        List<Entry> entries = new ArrayList<>();
        List<Entry> key = new ArrayList<>();
        String realname;
        boolean isDelete = true;
        for (String n : record.getNames()) {
            if (!n.contains("__")) {
                Variant v = record.get(n);
                if (n.endsWith(".KEY")) {
                    realname = n.substring(0, n.length() - 4);
                    key.add(new Entry(realname, v));
                } else {
                    realname = n;
                    isDelete = false;
                }
                entries.add(new Entry(realname, v));
            }
        }
        if (isDelete) {
            collection.remove(RecordsSerializer.write(
                    new Record(key)));
        } else {
            collection.put(RecordsSerializer.write(
                    new Record(key)), 
                    new Record(entries));
        }
    }
     
    public void put(List<Record> records) throws IOException {  
        w.lock();
        try {
            for (Record r : records) {
                put(r);
            }            
        } finally {
            w.unlock();
        }
    }  
    
    public List<Record> query(Record filter) throws IOException {
        r.lock();
        int i = 0;
        int limit = Records.getLimit(filter);
        int offset = Records.getOffset(filter);       
        try {
            List<Record> result = new ArrayList<>();
            for (Record r : collection.values()) {
                Record p = project(filter, r);
                if (p != null) {
                    // Matches...
                    if (offset > 0) {
                        offset--;
                    } else {
                        result.add(p);
                        if (--limit <= 0) {
                            return result;
                        }
                    }
                    
                }
            }
            return result;
        } finally {
            r.unlock();
        }
    }
    
    private Record project(Record filter, Record val) {
        List<Entry> entries = new ArrayList<>();
        String realname;
        Predicate<Variant> criteria;
        for (String n : filter.getNames()) {
            Variant v = filter.get(n);
            
            if (n.endsWith("__EQUAL")) {
                realname = n.substring(0, n.length() - 7);
                criteria = x -> x.equals(v);
            } else if (n.endsWith("__DISTINCT")) {
                realname = n.substring(0, n.length() - 10);
                criteria = x -> !x.equals(v);
           } else if (n.endsWith("__GREATER")) {
                realname = n.substring(0, n.length() - 9);
                criteria = x -> !x.isNull() && x.asDouble() > v.asDouble();
           } else if (n.endsWith("__GREATEROREQUAL")) {
                realname = n.substring(0, n.length() - 16);
                criteria = x -> !x.isNull() && x.asDouble() >= v.asDouble();
           } else if (n.endsWith("__LESS")) {
                realname = n.substring(0, n.length() - 6);
                criteria = x -> !x.isNull() && x.asDouble() < v.asDouble();
           } else if (n.endsWith("__LESSOREQUAL")) {
                realname = n.substring(0, n.length() - 13);
                criteria = x -> !x.isNull() && x.asDouble() <= v.asDouble();
            } else if (n.endsWith("__CONTAINS")) {
                realname = n.substring(0, n.length() - 10);
                criteria = x -> !x.isNull() && x.asString().contains((v.asString()));
            } else if (n.endsWith("__STARTS")) {
                realname = n.substring(0, n.length() - 8);
                criteria = x -> !x.isNull() && x.asString().startsWith((v.asString()));
            } else if (n.endsWith("__ENDS")) {
                realname = n.substring(0, n.length() - 6);
                criteria = x -> !x.isNull() && x.asString().endsWith((v.asString()));
           } else if (n.endsWith(".KEY")) {
                realname = n.substring(0, n.length() - 4);
                criteria = x -> x.equals(v);
            } else {
                realname = n;
                criteria = x -> v.equals(x);
            }
            
            // PROJECTION
            if (!n.contains("__")) {
                entries.add(new Entry(n, val.get(realname)));
            }
            // FILTER
            if (!realname.contains("__") && !v.isNull()) {
                if (!criteria.test(val.get(realname))) {
                    return null; // out
                }
            }
        }
        return new Record(entries);
    }
}
