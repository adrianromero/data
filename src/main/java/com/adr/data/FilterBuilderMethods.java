//     Data Access is a Java library to store data
//     Copyright (C) 2018 Adrián Romero Corchado.
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

public class FilterBuilderMethods {

    public static void build(FilterBuilder visitor, Record r) {
        for (String n : r.getNames()) {
            add(visitor, r, n);
        }
    }

    private static void add(FilterBuilder visitor, Record filter, String n) {

        if (n.endsWith("..EQUAL")) {
            visitor.filterEqual(n, n.substring(0, n.length() - 7));
        } else if (n.endsWith("..DISTINCT")) {
            visitor.filterDistinct(n, n.substring(0, n.length() - 10));
        } else if (n.endsWith("..GREATER")) {
            visitor.filterGreater(n, n.substring(0, n.length() - 9));
        } else if (n.endsWith("..GREATEROREQUAL")) {
            visitor.filterGreaterOrEqual(n, n.substring(0, n.length() - 16));
        } else if (n.endsWith("..LESS")) {
            visitor.filterLess(n, n.substring(0, n.length() - 6));
        } else if (n.endsWith("..LESSOREQUAL")) {
            visitor.filterLessOrEqual(n, n.substring(0, n.length() - 13));
        } else if (n.endsWith("..CONTAINS")) {
            visitor.filterContains(n, n.substring(0, n.length() - 10));
        } else if (n.endsWith("..STARTS")) {
            visitor.filterStarts(n, n.substring(0, n.length() - 8));
        } else if (n.endsWith("..ENDS")) {
            visitor.filterEnds(n, n.substring(0, n.length() - 6));
        } else if (!n.contains("..")) {
            String realname = n.endsWith(".KEY")
                    ? n.substring(0, n.length() - 4)
                    : n;
            if (!filter.get(n).isNull()) {
                visitor.filterEqual(n, realname);
            }
            visitor.project(n, realname);
        }
    }
}
