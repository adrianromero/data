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
import com.adr.data.var.VariantDecimal;
import com.adr.data.var.VariantDouble;
import com.adr.data.var.VariantFloat;
import com.adr.data.var.VariantInt;
import com.adr.data.var.VariantLong;
import com.adr.data.var.VariantString;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.math.BigDecimal;
import java.util.Map;

public final class Record {

    public final static Record EMPTY = new Record(ImmutableMap.of());

    private final ImmutableMap<String, Variant> map;

    private Record(ImmutableMap<String, Variant> record) {
        map = record;
    }

    public Record(String key, Variant value) {
        map = ImmutableMap.of(key, value);
    }

    public Record(String key, String value) {
        map = ImmutableMap.of(key, new VariantString(value));
    }

    public Record(String key, Integer value) {
        map = ImmutableMap.of(key, new VariantInt(value));
    }

    public ImmutableSet<String> keySet() {
        return map.keySet();
    }

    public ImmutableSet<Map.Entry<String, Variant>> entrySet() {
        return map.entrySet();
    }

    public Variant get(String name) {
        return map.get(name);
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

    public boolean getBoolean(String name) {
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final ImmutableMap.Builder<String, Variant> builder = ImmutableMap.builder();

        private Builder() {
        }

        public Builder entry(String name, Variant value) {
            builder.put(name, value);
            return this;
        }

        public Builder entry(String name, String value) {
            builder.put(name, new VariantString(value));
            return this;
        }

        public Builder entry(String name, Boolean value) {
            builder.put(name, new VariantBoolean(value));
            return this;
        }

        public Builder entry(String name, Integer value) {
            builder.put(name, new VariantInt(value));
            return this;
        }

        public Builder entry(String name, Long value) {
            builder.put(name, new VariantLong(value));
            return this;
        }

        public Builder entry(String name, Float value) {
            builder.put(name, new VariantFloat(value));
            return this;
        }

        public Builder entry(String name, Double value) {
            builder.put(name, new VariantDouble(value));
            return this;
        }

        public Builder entry(String name, BigDecimal value) {
            builder.put(name, new VariantDecimal(value));
            return this;
        }

        public Record build() {
            return new Record(builder.build());
        }
    }
}
