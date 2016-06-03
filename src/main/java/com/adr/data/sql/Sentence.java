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
import com.adr.data.Kind;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.Values;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
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
        return keyval.getKey().getValue("_ENTITY").toString();
    }  
    
    public static int execute(Connection c, CommandSQL command, Record keyval) throws DataException {
        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLKindParameters kindparams = new SQLKindParameters(stmt, command.getParamNames());
            write(kindparams, keyval.getKey());
            write(kindparams, keyval.getValue());
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    } 
    
    public static DataList query(Connection c, CommandSQL command, Record filter) throws DataException {
        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLKindParameters kindparams = new SQLKindParameters(stmt, command.getParamNames());
            write(kindparams, filter.getKey());
            write(kindparams, filter.getValue());

            try (ResultSet resultset = stmt.executeQuery()) {
                List<RecordMap> r = new ArrayList<>();
                SQLKindResults kindresults = new SQLKindResults(resultset);
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
            SQLKindParameters kindparams = new SQLKindParameters(stmt, command.getParamNames());
            write(kindparams, filter.getKey());
            write(kindparams, filter.getValue());

            try (ResultSet resultset = stmt.executeQuery()) {
                List<RecordMap> r = new ArrayList<>();
                SQLKindResults kindresults = new SQLKindResults(resultset);
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
    
    private static void write(SQLKindParameters kindparams, Values param) throws DataException {
        if (param == null) {
            return;
        }
        for (String name : param.getNames()) {
            param.getKind(name).set(kindparams, name, param.getValue(name));
        }
    } 
    
    private static ValuesMap read(SQLKindResults kindresults, Values param) throws DataException {

        if (param == null) {
            return new ValuesMap();
        }
        List<ValuesEntry> l = new ArrayList<>();
        for (String name : param.getNames()) {
            if ("_ENTITY".equals(name)) {
                l.add(new ValuesEntry(name, param.getKind(name), param.getValue(name)));
            } else if (!name.contains("::")) { // Is a field
                Kind k = param.getKind(name);
                l.add(new ValuesEntry(name, k, k.get(kindresults, name)));
            }
        }
        return new ValuesMap(l);
    }    
}
