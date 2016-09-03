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
import com.adr.data.QueryOptions;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.Values;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import com.adr.data.var.Variant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */

public abstract class Sentence {
    
    private static final Logger LOG = Logger.getLogger(Sentence.class.getName());
    
    public abstract String getName(); 
    
    public void execute(Connection c, SQLEngine engine, Record keyval) throws DataException {
        throw new UnsupportedOperationException();
    }    
    public List<Record> query(Connection c, SQLEngine engine, Record keyval, QueryOptions options) throws DataException {
        throw new UnsupportedOperationException();    
    }
    
    public static String getEntity(Record keyval) {
        return keyval.getKey().get("_ENTITY").asString();
    }  
    
    public static int execute(Connection c, CommandSQL command, Record keyval) throws DataException {
        String sql = command.getCommand();
        LOG.log(Level.INFO, "Executing SQL update: {0}", sql);
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            SQLParameters kindparams = new SQLParameters(stmt, command.getParamNames());
            write(kindparams, keyval.getKey());
            write(kindparams, keyval.getValue());
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    } 
    
    public static List<Record> query(Connection c, CommandSQL command, Record filter, QueryOptions options) throws DataException {
        String sql = command.getCommand();
        LOG.log(Level.INFO, "Executing SQL query: {0}", sql);
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            SQLParameters kindparams = new SQLParameters(stmt, command.getParamNames());
            write(kindparams, filter.getKey());
            write(kindparams, filter.getValue());
            
            int limit = options.getLimit();
            // int offset = options.getOffset(); // offset is applied in the CommandSQL

            try (ResultSet resultset = stmt.executeQuery()) {
                List<Record> r = new ArrayList<>();
                SQLResults kindresults = new SQLResults(resultset);
                
                int i = 0;
                while (i < limit && resultset.next()) {
                    r.add(new RecordMap(
                        read(kindresults, filter.getKey()), 
                        read(kindresults, filter.getValue())));
                    i++;
                }
                return r;
            }
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }   
    
    private static void write(SQLParameters kindparams, Values param) throws DataException {
        if (param == null) {
            return;
        }
        for (String name : param.getNames()) {
            param.get(name).write(kindparams, name);
        }
    } 
    
    private static ValuesMap read(SQLResults kindresults, Values param) throws DataException {
        if (param == null) {
            return new ValuesMap();
        }
        List<ValuesEntry> l = new ArrayList<>();
        for (String name : param.getNames()) {
            Variant p = param.get(name);
            if ("_ENTITY".equals(name)) {
                l.add(new ValuesEntry(name, p));
            } else if (!name.contains("::")) { // Is a field
                Variant  newv = p.getKind().read(kindresults, name);
                l.add(new ValuesEntry(name, newv));
            }
        }
        return new ValuesMap(l);
    }    
}
