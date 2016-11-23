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

package com.adr.data.utils;

import com.adr.data.record.Entry;
import com.adr.data.record.Record;
import com.adr.data.record.Values;
import com.adr.data.recordmap.RecordMap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrian
 */
public class Records {
    
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
        
        return new RecordMap(getEntries(record.getKey()), getEntries(record.getValue()));  
    }
    
    public static Record merge(Record base, Record record) {
        assert base != null;
        assert record != null;
        
        return new RecordMap(
                mergeEntries(getEntries(base.getKey()), getEntries(record.getKey())), 
                mergeEntries(getEntries(base.getValue()), getEntries(record.getValue())));
    }
    
    public static Record mergeValues(Record base, Values values) {
        return mergeValues(base, getEntries(values));
    }
    
    public static Record mergeValues(Record base, Entry... values) {
        assert base != null;
        assert values != null;
        
        return new RecordMap(getEntries(base.getKey()), mergeEntries(getEntries(base.getValue()), values));
    }
    
    public static Entry[] mergeEntries(Entry[] basevalue, Entry... values) {
        assert values != null;
        if (basevalue == null) {
            return values;
        } else {
            Entry[] valuesmerged = new Entry[basevalue.length + values.length];
            System.arraycopy(basevalue, 0, valuesmerged, 0, basevalue.length);
            System.arraycopy(values, 0, valuesmerged, basevalue.length, values.length); 
            return valuesmerged;
        }
    }

    public static Entry[] getEntries(Values values) {
        if (values == null) {
            return null;
        }        
        String[] names = values.getNames();
        Entry[] result = new Entry[names.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Entry(names[i], values.get(names[i]));
        }
        return result;
    }
}
