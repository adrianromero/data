//     Data Access is a Java library to store data
//     Copyright (C) 2016-2018 Adrián Romero Corchado.
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
import java.sql.Connection;
import java.util.List;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 *
 * @author adrian
 */
public abstract class SentenceQRY extends Sentence {

    protected abstract CommandSQL build(SQLEngine engine, Record record) throws DataException;
    
    @Override
    public List<Record> query(Connection c, SQLEngine engine, Record record) throws DataException {
        return Sentence.query(c, build(engine, record), record);
    }
        
    public static void addQueryOptions(StringBuilder sqlsent, SQLEngine engine, Record record) {
        
        // order by       
        String[] orderby = Records.getOrderBy(record);
        if (orderby.length > 0) {
            for (int i = 0; i < orderby.length; i++) {
                sqlsent.append(i == 0 ? " ORDER BY " : ", ");
                String s = orderby[i];
                if (s.endsWith("$ASC")) {
                    sqlsent.append(s.substring(0, s.length() - 4));
                    sqlsent.append(" ASC");
                } else if (s.endsWith("$DESC")) {
                    sqlsent.append(s.substring(0, s.length() - 5));
                    sqlsent.append(" DESC");                    
                } else {
                    sqlsent.append(s);
                    sqlsent.append(" ASC");                    
                }
            }
        }
        
        // limit
        int limit = Records.getLimit(record);
        if (limit < Integer.MAX_VALUE) {
            sqlsent.append(" LIMIT ");
            sqlsent.append(limit);
        }
        
        // offset
        int offset = Records.getOffset(record);
        if (offset > 0) {
            sqlsent.append(" OFFSET ");
            sqlsent.append(offset);
        }         
    }
}
