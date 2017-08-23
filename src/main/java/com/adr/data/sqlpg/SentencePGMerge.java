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

package com.adr.data.sqlpg;

import com.adr.data.sql.CommandSQL;
import com.adr.data.sql.SQLEngine;
import com.adr.data.sql.SentenceDML;
import java.util.ArrayList;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 * Merge supported since PosgreSQL 9.5
 * @author adrian
 */
public class SentencePGMerge extends SentenceDML {
    
     @Override
    public String getName() {
        return "PG-MERGE";
    }

    @Override
    protected CommandSQL build(SQLEngine engine, Record record) {

        StringBuilder sentence = new StringBuilder();
        StringBuilder values = new StringBuilder();
        StringBuilder conflict1 = new StringBuilder();
        StringBuilder conflict2 = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();

        sentence.append("INSERT INTO ");
        sentence.append(Records.getEntity(record));
        sentence.append("(");

        boolean filter = false;
        boolean filterconflict1 = false;
        boolean filterconflict2 = false;
        String realname;
        for (String f : record.getNames()) {
            if (!f.contains("__")) {
                if (f.endsWith("$KEY")) {
                    realname = f.substring(0, f.length() - 4);                  
                    conflict1.append(filterconflict1 ? ", " : "");
                    conflict1.append(realname);
                    filterconflict1 = true;
                } else {
                    realname = f;
                    conflict2.append(filterconflict2 ? ", " : " DO UPDATE SET ");
                    conflict2.append(f);
                    conflict2.append(" = excluded.");
                    conflict2.append(f);
                    filterconflict2 = true;                    
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
        sentence.append(")  ON CONFLICT (");
        sentence.append(conflict1);
        sentence.append(")");
        sentence.append(conflict2);

        return new CommandSQL(sentence.toString(), fieldslist.toArray(new String[fieldslist.size()]));   
    }
}
