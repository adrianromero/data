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
public class SentenceUpdate extends SentenceDML {

    @Override
    public String getName() {
        return "UPDATE";
    }
    
    @Override
    protected final CommandSQL build(SQLEngine engine, Record record) throws DataException {

        StringBuilder sentencefields = new StringBuilder();
        StringBuilder sentencefilters = new StringBuilder();
        ArrayList<String> namefields = new ArrayList<>();
        ArrayList<String> namefilters = new ArrayList<>();

        sentencefields.append("UPDATE ");
        sentencefields.append(Records.getEntity(record));
        
        boolean fields = false;
        boolean filters = false;
        String realname;
        for (String f : record.getNames()) {
            if (!f.contains("__")) {
                if (f.endsWith("$KEY")) {
                    realname = f.substring(0, f.length() - 4);
                    sentencefilters.append(filters ? " AND " : " WHERE ");
                    sentencefilters.append(realname);
                    sentencefilters.append(" = ?");
                    namefilters.add(f);
                    filters = true;                
                } else {
                    sentencefields.append(fields ? ", " : " SET ");
                    sentencefields.append(f);
                    sentencefields.append(" = ?");
                    namefields.add(f);
                    fields = true;
                }
            }
        }
        
        sentencefields.append(sentencefilters);
        namefields.addAll(namefilters);
        
        return new CommandSQL(sentencefields.toString(), namefields.toArray(new String[namefields.size()]));
    }  
}
