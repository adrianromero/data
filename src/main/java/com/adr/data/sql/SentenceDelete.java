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
public class SentenceDelete extends SentenceDML {

    @Override
    public String getName() {
        return "DELETE";
    }
    
    @Override
    protected final CommandSQL build(SQLEngine engine, Record record) throws DataException {

        StringBuilder sentence = new StringBuilder();
        ArrayList<String> keyfields = new ArrayList<>();

        sentence.append("DELETE FROM ");
        sentence.append(Records.getEntity(record));

        boolean filter = false;
        String realname;
        for (String f : record.getNames()) {
            if (!f.contains("__")) {            
                if (f.endsWith("$KEY")) {
                    realname = f.substring(0, f.length() - 4);
                    sentence.append(filter ? " AND " : " WHERE ");
                    sentence.append(realname);
                    sentence.append(" = ?");
                    keyfields.add(f);
                    filter = true;
                }                     
            }
        }
        return new CommandSQL(sentence.toString(), keyfields.toArray(new String[keyfields.size()]));
    } 
}
