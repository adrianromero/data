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

package com.adr.data.sqlpg;

import com.adr.data.Record;
import com.adr.data.sql.CommandSQL;
import com.adr.data.sql.Sentence;
import com.adr.data.sql.SentenceDML;
import java.util.ArrayList;

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
    protected CommandSQL build(Record keyval) {

        StringBuilder sentence = new StringBuilder();
        StringBuilder values = new StringBuilder();
        StringBuilder conflict = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();

        sentence.append("INSERT INTO ");
        sentence.append(Sentence.getEntity(keyval));
        sentence.append("(");

        boolean filter = false;
        for (String f : keyval.getKey().getNames()) {
            if (!"_ENTITY".equals(f)) {
                sentence.append(filter ? ", " : "");
                sentence.append(f);

                values.append(filter ? ", ?" : "?");
                fieldslist.add(f);
                
                conflict.append(filter ? ", " : "(");
                conflict.append(f);

                filter = true;
            }
        }
             
        boolean filterconflict =false;
        for (String f : keyval.getValue().getNames()) {
            sentence.append(filter ? ", " : "");
            sentence.append(f);

            values.append(filter ? ", ?" : "?");
            fieldslist.add(f);
            
            conflict.append(filterconflict ? ", " : ") DO UPDATE SET ");
            conflict.append(f);
            conflict.append(" = excluded.");
            conflict.append(f);

            filter = true;
            filterconflict = true;
        }
        
        sentence.append(") VALUES (");
        sentence.append(values);
        sentence.append(")  ON CONFLICT ");
        sentence.append(conflict);

        return new CommandSQL(sentence.toString(), fieldslist.toArray(new String[fieldslist.size()]));   
    }
}
