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

package com.adr.data;

import com.adr.data.record.Header;
import java.util.Arrays;
import java.util.List;
import com.adr.data.record.Record;

public interface Link {
    
    public List<Record> process(Header headers, List<Record> records) throws DataException;    
    
    // Execute default methods...
    
    public default void execute(Header headers, List<Record> records) throws DataException {
        process(headers, records);
    }
    
    public default void execute(Record... records) throws DataException {
        execute(Header.EMPTY, Arrays.asList(records));
    }
    
    public default void execute(Header headers, Record... records) throws DataException {
        execute(headers, Arrays.asList(records));
    }
    
    // Query default methods...
    
    public default List<Record> query(Header headers, Record filter) throws DataException {
        return process(headers, Arrays.asList(filter));
    } 
    
    public default List<Record> query(Record filter) throws DataException {
        return query(Header.EMPTY, filter);
    }
    
    public default Record find(Header headers, Record filter) throws DataException {
        List<Record> l = query(headers, filter);
        return l.isEmpty() ? null : l.get(0);
    }
    
    public default Record find(Record filter) throws DataException {
        return find(Header.EMPTY, filter);
    }    
}
