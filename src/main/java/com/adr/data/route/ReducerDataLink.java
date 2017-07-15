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
//     See the License for the specific 
package com.adr.data.route;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.record.Record;
import com.adr.data.record.Values;
import java.util.List;

/**
 *
 * @author adrian
 */
public class ReducerDataLink implements DataLink {
    private final DataLink link;
    private final ReducerData[] reducers;

    public ReducerDataLink(DataLink link, ReducerData... reducers) {
        this.link = link;
        this.reducers = reducers;
    }
   
    @Override
    public void execute(Values headers, List<Record> l) throws DataException {
        for (ReducerData r: reducers) {
            if (r.execute(link, headers, l)) {
                return;
            }
        }
        throw new DataException("Data cannot be found.");
    }
    
}
