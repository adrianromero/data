//     Data Access is a Java library to store data
//     Copyright (C) 2016-2018 Adrián Romero Corchado.
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

package com.adr.data.record;

import com.adr.data.var.Variant;
import com.adr.data.var.VariantVoid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author adrian
 */
public final class Record {

    public final static Record EMPTY = new Record();
    
    private final String[] names;
    private final Map<String, Variant> entries;  
    
    
    public Record(List<Entry> record) {
        Map<String, Variant> entriesmap = new HashMap<>();
        this.names = new String[record.size()];
        this.entries = Collections.unmodifiableMap(entriesmap);
        int i = 0;
        for (Entry e : record) {
            names[i++] = e.getName();
            entriesmap.put(e.getName(), e.getValue());
        } 
    }
      
    public Record(Entry... record) {
        Map<String, Variant> entriesmap = new HashMap<>();
        this.names = new String[record.length];
        this.entries = Collections.unmodifiableMap(entriesmap);
        int i = 0;
        for (Entry e : record) {
            names[i++] = e.getName();
            entriesmap.put(e.getName(), e.getValue());
        }    
    }
    public String[] getNames() {
        return names;
    }
    public Variant get(String name) {
        return entries.getOrDefault(name, VariantVoid.INSTANCE);
    }
    public String getString(String name) {
        return get(name).asString();    
    }
    public int getInteger(String name) {
        return get(name).asInteger(); 
    }
    public double getDouble(String name) {
        return get(name).asDouble();
    }
    public Boolean getBoolean(String name) {
        return get(name).asBoolean();
    }
    public byte[] getBytes(String name) {
        return get(name).asBytes();
    }      
}
