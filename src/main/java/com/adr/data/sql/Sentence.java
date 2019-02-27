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
import com.adr.data.var.Variant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

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

    public void query(Connection c, SQLEngine engine, Record val, ImmutableList.Builder<Record> result) throws DataException {
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

    public static void query(Connection c, CommandSQL command, Record filter, ImmutableList.Builder<Record> result) throws DataException {
        String sql = command.getCommand();
        LOG.log(Level.CONFIG, "Executing SQL query: {0}", sql);
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            write(stmt, command.getParamNames(), filter);

            int limit = Records.getLimit(filter);
            // int offset = options.getOffset(); // offset is applied in the CommandSQL

            try (ResultSet resultset = stmt.executeQuery()) {
                int i = 0;
                while (i < limit && resultset.next()) {
                    result.add(read(resultset, filter));
                    i++;
                }
            }
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    protected static void write(PreparedStatement stmt, String[] params, Record param) throws DataException {
        if (param == null) {
            return;
        }
        for (Map.Entry<String, Variant> entry : param.entrySet()) {
            SQLParameters sqlparams = new SQLParameters(stmt, params, entry.getKey());
            entry.getValue().getKind().write(sqlparams, entry.getValue());
        }
    }

    protected static Record read(ResultSet resultset, Record param) throws DataException {
        if (param == null) {
            return Record.EMPTY;
        }
        ImmutableMap.Builder<String, Variant> map = ImmutableMap.<String, Variant>builder();
        for (Map.Entry<String, Variant> entry : param.entrySet()) {
            if ("COLLECTION.KEY".equals(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            } else if (!entry.getKey().contains("..")) { // Is a field
                SQLResults sqlresults = new SQLResults(resultset, entry.getKey());
                Variant newv = entry.getValue().getKind().read(sqlresults);
                map.put(entry.getKey(), newv);
            }
        }
        return new Record(map.build());
    }   
}
