/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.DataException;
import com.adr.data.Record;
import com.adr.data.Values;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author adrian
 */
public interface Sentence {
    public String getName();
    public void execute(Connection c, Record keyval) throws DataException;  
    
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
    
    public static void write(SQLKindParameters kindparams, Values param) throws DataException {
        if (param == null) {
            return;
        }
        for (String name : param.getNames()) {
            param.getKind(name).set(kindparams, name, param.getValue(name));
        }
    }     
}
