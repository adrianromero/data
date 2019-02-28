//     Data Access is a Java library to store data
//     Copyright (C) 2017-2018 Adrián Romero Corchado.
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
import com.adr.data.record.Header;
import java.util.List;
import com.adr.data.record.Record;
import com.adr.data.CommandLink;

/**
 *
 * @author adrian
 */
public class ReducerCommandIdentity implements ReducerCommand {
    
    private final CommandLink commandlink;
    
    public ReducerCommandIdentity(CommandLink commandlink) {
        this.commandlink = commandlink;
    }

    @Override
    public boolean execute(Header headers, List<Record> l) throws DataException {
        commandlink.execute(headers, l);
        return true;
    }
}