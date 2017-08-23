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

package com.adr.data.record;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrian
 */
public class Records {
   
    public static String getEntity(Record record) {
        return record.get("__ENTITY").asString();
    }    
    
    public static List<Record> clone(List<Record> records) {
        assert records != null;
        
        List<Record> clone = new ArrayList<>(records.size());
        for (Record r: records) {
            clone.add(clone(r));
        }        
        return clone;  
    }
    
    public static Record clone(Record record) {
        assert record != null;
        
        return new RecordMap(getEntries(record));  
    }
    
    public static Record merge(Record base, Record record) {
        assert base != null;
        assert record != null;
        
        return new RecordMap(
                mergeEntries(getEntries(base), getEntries(record)));
    }
    
    public static Record merge(Record base, Entry... records) {
        assert base != null;
        assert records != null;
        
        return new RecordMap(mergeEntries(getEntries(base), records));
    }
    
    public static Entry[] mergeEntries(Entry[] base, Entry... records) {
        assert records != null;
        if (base == null) {
            return records;
        } else {
            Entry[] recordmerged = new Entry[base.length + records.length];
            System.arraycopy(base, 0, recordmerged, 0, base.length);
            System.arraycopy(records, 0, recordmerged, base.length, records.length); 
            return recordmerged;
        }
    }

    public static Entry[] getEntries(Record record) {
        if (record == null) {
            return null;
        }        
        String[] names = record.getNames();
        Entry[] result = new Entry[names.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Entry(names[i], record.get(names[i]));
        }
        return result;
    }
}
