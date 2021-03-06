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
import com.adr.data.record.Record;
import com.adr.data.Link;

/**
 *
 * @author adrian
 */
public class FailOverLink implements Link {

    private final Link[] commandlinks;

    public FailOverLink(Link... commandlinks) {
        this.commandlinks = commandlinks;
    }
    
    @Override
    public List<Record> process(Header headers, List<Record> l) throws DataException {
        for(Link d : commandlinks) {
            try {
                return d.process(headers, l);
            } catch (DataException e) {
                // Ignore and go to next
            }
        }
        throw new DataException("Failed all CommandLinks");
    }
}
