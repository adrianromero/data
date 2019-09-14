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
import java.util.function.Predicate;
import com.adr.data.record.Record;
import com.google.common.collect.ImmutableList;
import com.adr.data.Link;

/**
 *
 * @author adrian
 */
public class AnyMatchLink implements Link {

    private final Link link;
    private final Predicate<? super Record> p;
    private final boolean iffailex;

    public AnyMatchLink(Link link, Predicate<? super Record> p, boolean iffailex) {
        this.link = link;
        this.p = p;
        this.iffailex = iffailex;
    }

    public AnyMatchLink(Link link, Predicate<? super Record> p) {
        this.link = link;
        this.p = p;
        this.iffailex = false;
    }

    @Override
    public List<Record> process(Header headers, List<Record> l) throws DataException {
        if (l.stream().anyMatch(p)) {
            return link.process(headers, l);
        }
        if (iffailex) {
            throw new DataException("Not matched condition");
        }
        return ImmutableList.of(new Record("PROCESSED", 0));
    }
}
