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

package com.adr.data.sql;

import com.adr.data.DataException;
import java.util.ArrayList;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 *
 * @author adrian
 */
public class SentenceInsert extends SentenceDML {

    @Override
    public String getName() {
        return "INSERT";
    }
    
    @Override
    protected final CommandSQL build(SQLEngine engine, Record record) throws DataException {

        StringBuilder sentence = new StringBuilder();
        StringBuilder values = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();

        sentence.append("INSERT INTO ");
        sentence.append(Records.getCollection(record));
        sentence.append("(");

        boolean filter = false;
        String realname;
        for (String f : record.getNames()) {
            if (!f.equals("COLLECTION.KEY") && !f.contains("..")) {
                if (f.endsWith(".KEY")) {
                    realname = f.substring(0, f.length() - 4);
                } else {
                    realname = f;
                }            
                sentence.append(filter ? ", " : "");
                sentence.append(realname);
                values.append(filter ? ", ?" : "?");
                fieldslist.add(f);
                filter = true;
            }
        }
        sentence.append(") VALUES (");
        sentence.append(values);
        sentence.append(")");

        return new CommandSQL(sentence.toString(), fieldslist.toArray(new String[fieldslist.size()]));
    }  
}
