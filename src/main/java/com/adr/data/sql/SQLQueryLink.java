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
import com.adr.data.QueryLink;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 *
 * @author adrian
 */
public class SQLQueryLink implements QueryLink {

    private DataSource ds = null;
    private final SQLEngine engine;
    private final Sentence table;
    private final Map<String, Sentence> queries = new HashMap<>();

    public SQLQueryLink(DataSource ds, SQLEngine engine, Sentence... queries) {
        this.ds = ds;
        this.engine = engine;
        this.table = new SentenceTable();
        for (Sentence v : queries) {
            this.queries.put(v.getName(), v);
        }
    }
    
    public SQLQueryLink(DataSource ds, Sentence... queries) {
        this(ds, SQLEngine.GENERIC, queries);
    }
    
    @Override
    public List<Record> query(Record headers, Record filter) throws DataException {
        try (Connection c = ds.getConnection()) {
            String entity = Records.getEntity(filter);
            Sentence s = queries.get(entity);
            if (s == null) {
                s = table;
            }              
            return s.query(c, engine, filter);
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
}
