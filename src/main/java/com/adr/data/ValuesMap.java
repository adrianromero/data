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

package com.adr.data;

import com.adr.data.var.Variant;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author adrian
 */
public class ValuesMap implements Values {

    private final Map<String, Variant> entries;

    public ValuesMap(ValuesEntry... entries) {
        this(Stream.of(entries));
    }

    public ValuesMap(Collection<ValuesEntry> entries) {
        this(entries.stream());
    }

    public ValuesMap(Stream<ValuesEntry> entries) {
        this.entries = entries.collect(Collectors.toMap(ValuesEntry::getName, ValuesEntry::getValue));
    }

    @Override
    public String[] getNames() {
        return entries.keySet().stream().toArray(String[]::new);
    }

    @Override
    public Variant get(String name) {
        return entries.get(name);
    }
}
