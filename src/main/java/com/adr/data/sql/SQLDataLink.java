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

import com.adr.data.DataLink;
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

/**
 *
 * @author adrian
 */
public class SQLDataLink implements DataLink {

    private final DataSource ds;
    private final SQLEngine engine;
    private final Map<String, Sentence> sentences = new HashMap<>();
    
    public SQLDataLink(DataSource ds, SQLEngine engine, Sentence... sentences) {
        this.ds = ds;
        this.engine = engine;
        for (Sentence s : sentences) {
            this.sentences.put(s.getName(), s);
        }
        
        this.sentences.put("SQLDDL", new SentenceDDL());        
    }

    public SQLDataLink(DataSource ds, Sentence... sentences) {
        this(ds, SQLEngine.GENERIC, sentences);
    }

    @Override
    public void execute(Header headers, List<Record> l) throws DataException {
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
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
}
