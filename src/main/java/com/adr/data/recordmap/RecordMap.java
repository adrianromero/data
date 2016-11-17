//     Data Access is a Java library to store data
//     Copyright (C) 2016 Adrián Romero Corchado.
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

package com.adr.data.recordmap;

import com.adr.data.record.Entry;
import com.adr.data.record.Record;
import com.adr.data.record.Values;
import com.adr.data.var.Variant;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author adrian
 */
public class RecordMap implements Record {
    
    public final static RecordMap EMPTY = new RecordMap(new Entry[0], new Entry[0]);
    public final static RecordMap KEY = new RecordMap(new Entry[0]);
    
    private final Values key;
    private final Values value;
    
    public RecordMap(Entry[] keys, Entry[] values) {
        assert keys != null;
        
        this.key = new ValuesMap(entriesToMap(keys));
        this.value = values == null ? null : new ValuesMap(entriesToMap(values));
    }
    
    public RecordMap(Entry[] keys) {
        assert keys != null;
        
        this.key = new ValuesMap(entriesToMap(keys));
        this.value = null;
    }

    @Override
    public Values getKey() {
        return key;
    }

    @Override
    public Values getValue() {
        return value;
    }  
    
    private Map<String, Variant> entriesToMap(Entry[] entries) {
        Map<String, Variant> result = new HashMap<>();
        for (Entry e: entries) {
            result.put(e.getName(), e.getValue());
        }
        return result;
    }
}
