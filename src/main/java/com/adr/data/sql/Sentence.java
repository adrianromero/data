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
import com.adr.data.DataList;
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

/**
 *
 * @author adrian
 */

public abstract class Sentence {
    
    public abstract String getName(); 
    
    public void execute(Connection c, Record keyval) throws DataException {
        throw new UnsupportedOperationException();
    }    
    public DataList query(Connection c, Record keyval) throws DataException {
        throw new UnsupportedOperationException();    
    }
    public Record find(Connection c, Record keyval) throws DataException {
        throw new UnsupportedOperationException();    
    }
    
    public static String getEntity(Record keyval) {
        return keyval.getKey().getValue("_ENTITY").asString();
    }  
    
    public static int execute(Connection c, CommandSQL command, Record keyval) throws DataException {
        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLParameters kindparams = new SQLParameters(stmt, command.getParamNames());
            write(kindparams, keyval.getKey());
            write(kindparams, keyval.getValue());
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    } 
    
    public static DataList query(Connection c, CommandSQL command, Record filter) throws DataException {
        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLParameters kindparams = new SQLParameters(stmt, command.getParamNames());
            write(kindparams, filter.getKey());
            write(kindparams, filter.getValue());

            try (ResultSet resultset = stmt.executeQuery()) {
                List<RecordMap> r = new ArrayList<>();
                SQLResults kindresults = new SQLResults(resultset);
                while (resultset.next()) {
                    r.add(new RecordMap(
                        read(kindresults, filter.getKey()), 
                        read(kindresults, filter.getValue())));
                }
                return new DataList(r);
            }
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }   
    
    public static Record find(Connection c, CommandSQL command, Record filter) throws DataException {
        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLParameters kindparams = new SQLParameters(stmt, command.getParamNames());
            write(kindparams, filter.getKey());
            write(kindparams, filter.getValue());

            try (ResultSet resultset = stmt.executeQuery()) {
                List<RecordMap> r = new ArrayList<>();
                SQLResults kindresults = new SQLResults(resultset);
                if (resultset.next()) {
                    return new RecordMap(
                        read(kindresults, filter.getKey()), 
                        read(kindresults, filter.getValue()));
                } else {
                    return null;
                }
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
            param.getValue(name).write(kindparams, name);
        }
    } 
    
    private static ValuesMap read(SQLResults kindresults, Values param) throws DataException {
        if (param == null) {
            return new ValuesMap();
        }
        List<ValuesEntry> l = new ArrayList<>();
        for (String name : param.getNames()) {
            Variant p = param.getValue(name);
            if ("_ENTITY".equals(name)) {
                l.add(new ValuesEntry(name, p));
            } else if (!name.contains("::")) { // Is a field
                Variant  newv = p.getKind().buildRead(kindresults, name);
                l.add(new ValuesEntry(name, newv));
            }
        }
        return new ValuesMap(l);
    }    
}
