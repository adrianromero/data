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
import com.adr.data.record.Entry;
import com.adr.data.var.Variant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import java.util.Collections;

/**
 *
 * @author adrian
 */
public abstract class Sentence {

    private static final Logger LOG = Logger.getLogger(Sentence.class.getName());

    public abstract String getName();

    public void execute(Connection c, SQLEngine engine, Record val) throws DataException {
        throw new UnsupportedOperationException();
    }

    public List<Record> query(Connection c, SQLEngine engine, Record val) throws DataException {
        throw new UnsupportedOperationException();
    }

    public static int execute(Connection c, CommandSQL command, Record val) throws DataException {
        String sql = command.getCommand();
        LOG.log(Level.CONFIG, "Executing SQL update: {0}", sql);
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            write(stmt, command.getParamNames(), val);
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    public static List<Record> query(Connection c, CommandSQL command, Record filter) throws DataException {
        String sql = command.getCommand();
        LOG.log(Level.CONFIG, "Executing SQL query: {0}", sql);
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            write(stmt, command.getParamNames(), filter);

            int limit = Records.getLimit(filter);
            // int offset = options.getOffset(); // offset is applied in the CommandSQL

            try (ResultSet resultset = stmt.executeQuery()) {
                List<Record> r = new ArrayList<>();

                int i = 0;
                while (i < limit && resultset.next()) {
                    r.add(new Record(read(resultset, filter)));
                    i++;
                }
                return r;
            }
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    protected static void write(PreparedStatement stmt, String[] params, Record param) throws DataException {
        if (param == null) {
            return;
        }
        for (String name : param.getNames()) {
            SQLParameters sqlparams = new SQLParameters(stmt, params, name);
            Variant v = param.get(name);
            v.getKind().write(sqlparams, v);
        }
    }

    protected static List<Entry> read(ResultSet resultset, Record param) throws DataException {
        if (param == null) {
            return Collections.emptyList();
        }
        List<Entry> l = new ArrayList<>();
        for (String name : param.getNames()) {
            Variant p = param.get(name);
            if ("COLLECTION.KEY".equals(name)) {
                l.add(new Entry(name, p));
            } else if (!name.contains("..")) { // Is a field
                SQLResults sqlresults = new SQLResults(resultset, name);
                Variant newv = p.getKind().read(sqlresults);
                l.add(new Entry(name, newv));
            }
        }
        return l;
    }   
}
