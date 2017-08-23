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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 *
 * @author adrian
 */
public class PredicateSelectorList implements Predicate<Record> {
    
    private final Set<String> names;
    
    public PredicateSelectorList(String... names) {
        this.names = new HashSet<>(Arrays.asList(names));
    }
    
    public PredicateSelectorList(Set<String> names) {
        this.names = names;
    }

    @Override
    public boolean test(Record v) {
        return names.contains(Records.getEntity(v));
    }   
}
