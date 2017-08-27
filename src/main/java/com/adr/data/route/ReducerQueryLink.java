//     Data Access is a Java library to store data
//     Copyright (C) 2017 Adrián Romero Corchado.
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
import com.adr.data.QueryLink;
import java.util.List;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class ReducerQueryLink implements QueryLink {

    private final ReducerQuery[] reducers;
    
    public ReducerQueryLink(ReducerQuery... reducers) {
        this.reducers = reducers;
    }

    @Override
    public List<Record> query(Record headers, Record filter) throws DataException {
        List<Record> result;
        for (ReducerQuery r : reducers) {
            result = r.query(headers, filter);
            if (result != null) {
                return result;
            }
        }
        throw new DataException("Query cannot be found.");
    }
}