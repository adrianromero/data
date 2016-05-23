/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sqlstrategy;

import com.adr.data.sql.SQLKindParameters;
import com.adr.data.DataException;
import com.adr.data.Record;
import com.adr.data.Values;
import com.adr.data.sql.CommandSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Eva
 */
public class SQLStrategy {
    
    // Strategy execute
    public void execute(Connection c, Record keyval) throws DataException {
        
        
        if (keyval.getValue() == null) {
            executeDelete(c, keyval);
        } else {
            executeUpsert(c, keyval);
        }
    }
    
    // Strategy Command/Procedure
    protected void executeProcedure(Connection c, Record keyval) throws DataException {
//        if (execute(c, buildCommandInsert(keyval), keyval) != 1) {
//            throw new DataException("PROCEDURE command must return 1 row");
//        }
    }
    
    // Strategy Insert
    protected void executeInsert(Connection c, Record keyval) throws DataException {
        if (execute(c, buildCommandInsert(keyval), keyval) != 1) {
            throw new DataException("INSERT command must return 1 row");
        }
    }

    // Strategy Upsert
    protected void executeUpsert(Connection c, Record keyval) throws DataException {
        int rows = execute(c, buildCommandUpdate(keyval), keyval);
        if (rows == 0) {
            if (execute(c, buildCommandInsert(keyval), keyval) != 1) {
                throw new DataException("INSERT in UPSERT command must return 1 row");
            }
        } else if (rows != 1) {
            throw new DataException("UPDATE in UPSERT command must return 0 or 1 row");
        }
    }

    // Strategy Delete
    protected void executeDelete(Connection c, Record keyval) throws DataException {
        if (execute(c, buildCommandDelete(keyval), keyval) != 1) {
            throw new DataException("DELETE command must return 1 row");
        }
    }

    protected final int execute(Connection c, CommandSQL command, Record keyval) throws DataException {

        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLKindParameters kindparams = new SQLKindParameters(stmt, command.getParamNames());
            write(kindparams, keyval.getKey());
            write(kindparams, keyval.getValue());
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    private void write(SQLKindParameters kindparams, Values param) throws DataException {

        if (param == null) {
            return;
        }
        for (String name : param.getNames()) {
            param.getKind(name).set(kindparams, name, param.getValue(name));
        }
    }
    
    protected final String getTableName(Record keyval) {
        return keyval.getKey().getValue("_ENTITY").toString();
    }

    protected final CommandSQL buildCommandInsert(Record keyval) {

        StringBuilder sentence = new StringBuilder();
        StringBuilder values = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();

        sentence.append("INSERT INTO ");
        sentence.append(getTableName(keyval));
        sentence.append("(");

        boolean filter = false;
        for (String f : keyval.getKey().getNames()) {
            if (!"_ENTITY".equals(f)) {
                sentence.append(filter ? ", " : "");
                sentence.append(f);

                values.append(filter ? ", ?" : "?");
                fieldslist.add(f);

                filter = true;
            }
        }
        for (String f : keyval.getValue().getNames()) {
            sentence.append(filter ? ", " : "");
            sentence.append(f);

            values.append(filter ? ", ?" : "?");
            fieldslist.add(f);

            filter = true;
        }
        sentence.append(") VALUES (");
        sentence.append(values);
        sentence.append(")");

        return new CommandSQL(sentence.toString(), fieldslist.toArray(new String[fieldslist.size()]));
    }

    protected final CommandSQL buildCommandUpdate(Record keyval) {

        StringBuilder sentence = new StringBuilder();
        ArrayList<String> keyfields = new ArrayList<>();

        sentence.append("UPDATE ");
        sentence.append(getTableName(keyval));

        boolean filter = false;
        for (String f : keyval.getValue().getNames()) {
            sentence.append(filter ? ", " : " SET ");
            sentence.append(f);
            sentence.append(" = ?");
            keyfields.add(f);
            filter = true;
        }

        filter = false;
        for (String f : keyval.getKey().getNames()) {
            if (!"_ENTITY".equals(f)) {
                sentence.append(filter ? " AND " : " WHERE ");
                sentence.append(f);
                sentence.append(" = ?");
                keyfields.add(f);
                filter = true;
            }
        }

        return new CommandSQL(sentence.toString(), keyfields.toArray(new String[keyfields.size()]));
    }

    protected final CommandSQL buildCommandDelete(Record keyval) {

        StringBuilder sentence = new StringBuilder();
        ArrayList<String> keyfields = new ArrayList<>();

        sentence.append("DELETE FROM ");
        sentence.append(getTableName(keyval));

        boolean filter = false;
        for (String f : keyval.getKey().getNames()) {
            if (!"_ENTITY".equals(f)) {
                sentence.append(filter ? " AND " : " WHERE ");
                sentence.append(f);
                sentence.append(" = ?");
                keyfields.add(f);
                filter = true;
            }
        }
        return new CommandSQL(sentence.toString(), keyfields.toArray(new String[keyfields.size()]));
    }
}
