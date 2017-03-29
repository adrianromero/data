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

import com.adr.data.record.Record;
import com.adr.data.sql.CommandSQL;
import com.adr.data.sql.SQLEngine;
import com.adr.data.sql.SentenceDML;
import java.util.ArrayList;

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
    protected final CommandSQL build(SQLEngine engine, Record keyval) {

        StringBuilder sentence = new StringBuilder();
        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();

        sentence.append("MERGE INTO ");
        sentence.append(getEntity(keyval));
        
        boolean filter = false;
        for (String f : keyval.getKey().getNames()) {
            if (!f.contains("__")) {
                sentence.append(filter ? ", " : "(");
                sentence.append(f);
                keys.append(filter ? ", " : "(");
                keys.append(f);
                values.append(filter ? ", ?" : " (?");
                fieldslist.add(f);                
                filter = true;
            }
        }        

        for (String f : keyval.getValue().getNames()) {
            sentence.append(filter ? ", " : "(");
            sentence.append(f);
            values.append(filter ? ", ?" : " (?");
            fieldslist.add(f);                
            filter = true;
        }

        sentence.append(") KEY");
        sentence.append(keys);
        sentence.append(") VALUES");
        sentence.append(values);
        sentence.append(")");
        
        return new CommandSQL(sentence.toString(), fieldslist.stream().toArray(String[]::new));
    }  
}
