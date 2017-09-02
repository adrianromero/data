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

package com.adr.data.sqlh2;

import com.adr.data.DataException;
import com.adr.data.sql.CommandSQL;
import com.adr.data.sql.SQLEngine;
import com.adr.data.sql.SentenceDML;
import java.util.ArrayList;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 *
 * @author adrian
 */
public class SentenceH2Merge extends SentenceDML {

    @Override
    public String getName() {
        return "H2-MERGE";
    }
    
    @Override
    protected final CommandSQL build(SQLEngine engine, Record record) throws DataException {

        StringBuilder sentence = new StringBuilder();
        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();

        sentence.append("MERGE INTO ");
        sentence.append(Records.getEntity(record));
        sentence.append(" (");
        
        boolean filter = false;
        boolean filterkeys = false;
        String realname;
        for (String f : record.getNames()) {
            if (!f.contains("__")) {
                if (f.endsWith(".KEY")) {
                    realname = f.substring(0, f.length() - 4);  
                    keys.append(filterkeys ? ", " : "");
                    keys.append(realname); 
                    filterkeys = true;              
                } else {
                    realname = f;
                }
                sentence.append(filter ? ", " : "");
                sentence.append(realname);
                values.append(filter ? ", ?" : "?");
                filter = true;     
                fieldslist.add(f);                    
            }
        }        

        sentence.append(") KEY (");
        sentence.append(keys);
        sentence.append(") VALUES (");
        sentence.append(values);
        sentence.append(")");
        
        return new CommandSQL(sentence.toString(), fieldslist.stream().toArray(String[]::new));
    }  
}
