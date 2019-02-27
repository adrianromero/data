//     Data Access is a Java library to store data
//     Copyright (C) 2016-2019 Adrián Romero Corchado.
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
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantDouble;
import com.adr.data.var.VariantInt;
import com.adr.data.var.VariantString;
import com.adr.data.var.VariantVoid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author adrian
 */
public final class Record {

    public final static Record EMPTY = new Record();
    
    private ImmutableMap<String, Variant> map;
    
    public Record(ImmutableMap<String, Variant> record) { 
        map = record;
    }
    public Record(Record.Entry ... record) {
        this(ImmutableMap.<String, Variant>builder()
            .putAll(Arrays.asList(record))
            .build());
    }
    
    public ImmutableSet<String> keySet() {
        return map.keySet();
    }
    public ImmutableSet<Map.Entry<String, Variant>> entrySet() {
        return map.entrySet();
    }
    public Variant get(String name) {
        return map.getOrDefault(name, VariantVoid.INSTANCE);
    }
    public String getString(String name) {
        return get(name).asString();    
    }
    public int getInteger(String name) {
        return get(name).asInteger(); 
    }
    public long getLong(String name) {
        return get(name).asLong(); 
    }
    public float getFloat(String name) {
        return get(name).asFloat();
    }
    public double getDouble(String name) {
        return get(name).asDouble();
    }
    public BigDecimal getBigDecimal(String name) {
        return get(name).asBigDecimal();
    }
    public Boolean getBoolean(String name) {
        return get(name).asBoolean();
    }
    public byte[] getBytes(String name) {
        return get(name).asBytes();
    }    
    
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Record)) {
            return false;
        }
        return map.equals(((Record) o).map);
    }
    
    @Override
    public int hashCode() {
        return map.hashCode() + 2;
    }
    
    public final static Record.Entry entry(String name, String value) {
        return new Record.Entry(name, new VariantString(value));
    }
    
    public final static Record.Entry entry(String name, Integer value) {
        return new Record.Entry(name, new VariantInt(value));
    }
    
    public final static Record.Entry entry(String name, Boolean value) {
        return new Record.Entry(name, new VariantBoolean(value));
    }
    
    public final static Record.Entry entry(String name, Double value) {
        return new Record.Entry(name, new VariantDouble(value));
    }
    
    public final static Record.Entry entry(String name, Variant value) {
        return new Record.Entry(name, value);
    }    
    
    public static class Entry implements Map.Entry<String, Variant> {
        
        private final String key;
        private final Variant value;
        
        private Entry(String key, Variant value) {
            this.key = key;
            this.value = value;
        } 

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Variant getValue() {
            return value;
        }

        @Override
        public Variant setValue(Variant value) {
            throw new UnsupportedOperationException(); 
        }    
    }
}
