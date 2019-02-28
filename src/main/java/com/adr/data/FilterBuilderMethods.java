//     Data Access is a Java library to store data
//     Copyright (C) 2019 Adrián Romero Corchado.
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

import com.adr.data.record.Record;
import com.adr.data.var.Variant;
import java.util.Map;

public class FilterBuilderMethods {

    public static void build(FilterBuilder visitor, Record filter) {
        for (Map.Entry<String, Variant> e : filter.entrySet()) {
            add(visitor, e.getKey(), e.getValue());
        }
    }

    private static void add(FilterBuilder visitor, String key, Variant value) {

        if (key.endsWith("..EQUAL")) {
            visitor.filterEqual(key, key.substring(0, key.length() - 7));
        } else if (key.endsWith("..DISTINCT")) {
            visitor.filterDistinct(key, key.substring(0, key.length() - 10));
        } else if (key.endsWith("..GREATER")) {
            visitor.filterGreater(key, key.substring(0, key.length() - 9));
        } else if (key.endsWith("..GREATEROREQUAL")) {
            visitor.filterGreaterOrEqual(key, key.substring(0, key.length() - 16));
        } else if (key.endsWith("..LESS")) {
            visitor.filterLess(key, key.substring(0, key.length() - 6));
        } else if (key.endsWith("..LESSOREQUAL")) {
            visitor.filterLessOrEqual(key, key.substring(0, key.length() - 13));
        } else if (key.endsWith("..CONTAINS")) {
            visitor.filterContains(key, key.substring(0, key.length() - 10));
        } else if (key.endsWith("..STARTS")) {
            visitor.filterStarts(key, key.substring(0, key.length() - 8));
        } else if (key.endsWith("..ENDS")) {
            visitor.filterEnds(key, key.substring(0, key.length() - 6));
        } else if (!key.contains("..")) {
            String realname = key.endsWith(".KEY")
                    ? key.substring(0, key.length() - 4)
                    : key;
            if (!value.isNull()) {
                visitor.filterEqual(key, realname);
            }
            visitor.project(key, realname);
        }
    }
}
