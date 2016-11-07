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

import com.adr.data.record.Record;
import java.util.List;

/**
 *
 * @author adrian
 */
public interface QueryLink extends AutoCloseable {
    
    public List<Record> query(Record filter, QueryOptions options) throws DataException;       
    public default List<Record> query(Record filter) throws DataException {
        return query(filter, QueryOptions.DEFAULT);
    }
    public default Record find(Record filter) throws DataException {
        List<Record> l = query(filter, QueryOptions.FIND);
        return l.isEmpty() ? null : l.get(0);
    } 
    
    @Override
    public void close() throws DataException;
}
