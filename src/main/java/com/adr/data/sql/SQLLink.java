/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.DataLink;
import com.adr.data.DataException;
import com.adr.data.DataList;
import com.adr.data.RecordMap;
import com.adr.data.ValuesMap;
import com.adr.data.QueryLink;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author adrian
 */
public class SQLLink implements DataLink, QueryLink {
    
    private DataSource ds = null;
    
    private final Map<String, SQLView> viewsmap = new HashMap<>();
    
    public SQLLink(DataSource ds, SQLView ... views) {
        this.ds = ds;
        for (SQLView v : views) {
            addView(v);
        } 
    }
    
    protected void addView(SQLView v) {
        viewsmap.put(v.getName(), v);
    }

    @Override
    public void execute(DataList l) throws DataException {
        // TODO: check commit /rollback
        
        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            for (RecordMap keyval: l.getData()) {                
                execute(c, keyval);               
            }
            c.commit();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }     
    }
    
    private void execute(Connection c, RecordMap keyval) throws DataException {
        if (keyval.getValue() == null) {
            executeDelete(c, keyval);
        } else {
            executeUpsert(c, keyval);
        }
    }
    
    // Strategy Insert
    private void executeInsert(Connection c, RecordMap keyval) throws DataException {
        if (execute(c, buildCommandInsert(keyval), keyval) != 1) {
            throw new DataException("INSERT command must return 1 row");
        }
    }
    
    // Strategy Upsert
    private void executeUpsert(Connection c, RecordMap keyval) throws DataException {
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
    private void executeDelete(Connection c, RecordMap keyval) throws DataException {
        if (execute(c, buildCommandDelete(keyval), keyval) != 1) {
            throw new DataException("DELETE command must return 1 row");
        }
    }
    
    private int execute(Connection c, CommandSQL command, RecordMap keyval) throws DataException {
        
        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLKindParameters kindparams = new SQLKindParameters(stmt, command.getParamNames());
            write(kindparams, keyval.getKey());
            write(kindparams, keyval.getValue());       
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }              
    }  
    
    private void write(SQLKindParameters kindparams, ValuesMap param) throws DataException {
        
        if (param == null) {
            return;
        }
        for(String name : param.getNames()) {
            param.getKind(name).set(kindparams, name, param.getValue(name));
        }           
    }
    
    private String getTableName(RecordMap keyval) {
        return keyval.getKey().getValue("_ENTITY").toString();
    }
    
    private CommandSQL buildCommandInsert(RecordMap keyval) {
        
        StringBuilder sentence = new StringBuilder();
        StringBuilder values = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();
        
        sentence.append("INSERT INTO ");
        sentence.append(getTableName(keyval));
        sentence.append("(");
               
        boolean filter = false;
        for (String f: keyval.getKey().getNames()) {
            if (!"_ENTITY".equals(f)) {
                sentence.append(filter ? ", " : "");
                sentence.append(f);

                values.append(filter ? ", ?": "?");
                fieldslist.add(f);

                filter = true;
            }
        }  
        for (String f: keyval.getValue().getNames()) {
            sentence.append(filter ? ", " : "");
            sentence.append(f);

            values.append(filter ? ", ?": "?");
            fieldslist.add(f);

            filter = true;
        }          
        sentence.append(") VALUES (");
        sentence.append(values);
        sentence.append(")");
            
        return new CommandSQL(sentence.toString(), fieldslist.toArray(new String[fieldslist.size()]));        
    }
    
    private CommandSQL buildCommandUpdate(RecordMap keyval) {
        
        StringBuilder sentence = new StringBuilder();
        ArrayList<String> keyfields = new ArrayList<>();
        
        sentence.append("UPDATE ");
        sentence.append(getTableName(keyval));
               
        boolean filter = false;
        for (String f: keyval.getValue().getNames()) {
            sentence.append(filter ? ", " : " SET ");
            sentence.append(f);
            sentence.append(" = ?");    
            keyfields.add(f);
            filter = true;
        }  
        
        filter = false;
        for (String f: keyval.getKey().getNames()) {
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
    
    private CommandSQL buildCommandDelete(RecordMap keyval) {
        
        StringBuilder sentence = new StringBuilder();
        ArrayList<String> keyfields = new ArrayList<>();
        
        sentence.append("DELETE FROM ");
        sentence.append(getTableName(keyval));
        
        boolean filter = false;
        for (String f: keyval.getKey().getNames()) {
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

    @Override
    public RecordMap find(RecordMap filter) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataList query(RecordMap filter) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
