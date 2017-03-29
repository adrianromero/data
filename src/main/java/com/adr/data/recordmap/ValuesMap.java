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

import com.adr.data.record.Values;
import com.adr.data.var.Variant;
import com.adr.data.var.VariantVoid;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author adrian
 */
public class ValuesMap implements Values {
    
    public final static ValuesMap EMPTY = new ValuesMap(new LinkedHashMap<>());

    private String[] names = null;
    private final Map<String, Variant> entries;

    public ValuesMap(Entry... entries) {
        LinkedHashMap<String, Variant> entriesmap = new LinkedHashMap<>();
        for (Entry e: entries) {
            entriesmap.put(e.getName(), e.getValue());
        }       
        this.entries = Collections.unmodifiableMap(entriesmap);
    }

    public ValuesMap(LinkedHashMap<String, Variant> entries) {
        this.entries = Collections.unmodifiableMap(entries);
    }

    @Override
    public String[] getNames() {
        if (names == null) {
            names = entries.keySet().stream().toArray(String[]::new);
        }
        return names;
    }

    @Override
    public Variant get(String name) {
        return entries.getOrDefault(name, VariantVoid.INSTANCE);
    }
}
