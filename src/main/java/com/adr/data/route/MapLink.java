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
package com.adr.data.route;

import com.adr.data.DataException;
import com.adr.data.record.Header;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.adr.data.record.Record;
import com.google.common.collect.ImmutableList;
import com.adr.data.Link;

/**
 *
 * @author adrian
 */
public class MapLink implements Link {

    private final Link link;
    private final Function<? super Record, ? extends Stream<? extends Record>> mapper;
    private final boolean ifemptyex;

    public MapLink(Link link, Function<? super Record, ? extends Stream<? extends Record>> mapper, boolean ifemptyex) {
        this.link = link;
        this.mapper = mapper;
        this.ifemptyex = ifemptyex;
    }

    @Override
    public List<Record> process(Header headers, List<Record> l) throws DataException {
        List<Record> l2 = l.stream().flatMap(mapper).collect(Collectors.toList());
        if (!l2.isEmpty()) {
            return link.process(headers, l2);
        }
        if (ifemptyex) {
            throw new DataException("Empty List to execute.");
        }
        return ImmutableList.of(new Record("PROCESSED", 0));        
    }
}
