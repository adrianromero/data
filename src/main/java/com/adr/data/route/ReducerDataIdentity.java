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
package com.adr.data.route;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import java.util.List;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class ReducerDataIdentity implements ReducerData {
    
    private final DataLink link;
    
    public ReducerDataIdentity(DataLink link) {
        this.link = link;
    }

    @Override
    public boolean execute(Record headers, List<Record> l) throws DataException {
        link.execute(headers, l);
        return true;
    }
}
