//     Data Access is a Java library to store data
//     Copyright (C) 2016-2017 Adrián Romero Corchado.
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

import com.adr.data.record.Record;
import com.adr.data.record.Values;

/**
 *
 * @author adrian
 */
public class RecordMap implements Record {
    
    public final static RecordMap EMPTY = new RecordMap(ValuesMap.EMPTY, ValuesMap.EMPTY);
    public final static RecordMap KEY = new RecordMap(ValuesMap.EMPTY);
    
    private final Values key;
    private final Values value;
    
    public RecordMap(Entry[] keys, Entry[] values) {
        assert keys != null;
        
        this.key = new ValuesMap(keys);
        this.value = values == null ? null : new ValuesMap(values);
    }
    
    public RecordMap(Entry[] keys) {
        this(keys, null);
    }
    
    public RecordMap(Values keys, Values values) {
        assert keys != null;
        
        this.key = keys;
        this.value = values;
    }
    
    public RecordMap(Values keys) {
        this(keys, null);
    }
    
    @Override
    public Values getKey() {
        return key;
    }

    @Override
    public Values getValue() {
        return value;
    }  
}
