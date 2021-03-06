//     Data Access is a Java library to store data
//     Copyright (C) 2016-2019 Adrián Romero Corchado.
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
import com.adr.data.record.Header;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.google.common.collect.ImmutableList;
import com.adr.data.Link;

/**
 *
 * @author adrian
 */
public class SQLCommandLink implements Link {

    private final DataSource ds;
    private final SQLEngine engine;
    private final Map<String, Sentence> sentences = new HashMap<>();

    public SQLCommandLink(DataSource ds, SQLEngine engine, Sentence... sentences) {
        this.ds = ds;
        this.engine = engine;
        for (Sentence s : sentences) {
            this.sentences.put(s.getName(), s);
        }
    }

    public SQLCommandLink(DataSource ds, Sentence... sentences) {
        this(ds, SQLEngine.GENERIC, sentences);
    }

    @Override
    public List<Record> process(Header headers, List<Record> l) throws DataException {
        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            for (Record r : l) {
                Sentence s = sentences.get(Records.getCollection(r));
                if (s == null) {
                    s = engine.getPutSentence();
                }
                s.execute(c, engine, r);
            }
            c.commit();
            return ImmutableList.of(new Record("PROCESSED", l.size()));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
}
