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
import com.adr.data.Record;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author adrian
 */
public class SQLQueryLink implements QueryLink {

    private DataSource ds = null;
    private final Sentence table;
    private final Map<String, Sentence> queries = new HashMap<>();

    public SQLQueryLink(DataSource ds, Sentence... queries) {
        this.ds = ds;
        this.table = new SentenceTable();
        for (Sentence v : queries) {
            this.queries.put(v.getName(), v);
        }
    }

    @Override
    public List<Record> query(Record filter, Map<String, String> options) throws DataException {
        try (Connection c = ds.getConnection()) {
            String entity = Sentence.getEntity(filter);
            Sentence s = queries.get(entity);
            if (s == null) {
                s = table;
            }              
            return s.query(c, filter, options);
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
}
