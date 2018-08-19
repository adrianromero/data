//     Data Access is a Java library to store data
//     Copyright (C) 2016-2018 Adrián Romero Corchado.
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
import com.adr.data.DataLink;
import com.adr.data.record.Header;
import java.util.List;
import java.util.function.Predicate;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class AllMatchDataLink implements DataLink {
    
    private final DataLink datalink;
    private final Predicate<? super Record> p;
    private final boolean iffailex;

    public AllMatchDataLink(DataLink datalink,  Predicate<? super Record> p, boolean iffailex) {
        this.datalink = datalink;
        this.p = p;
        this.iffailex = iffailex;
    }
    
    public AllMatchDataLink(DataLink datalink,  Predicate<? super Record> p) {
        this.datalink = datalink;
        this.p = p;
        this.iffailex = false;
    }

    @Override
    public void execute(Header headers, List<Record> l) throws DataException {
        if (l.stream().allMatch(p)) {
            datalink.execute(headers, l);
        } else if (iffailex) {
            throw new DataException("Not matched condition");
        }
    }
}
