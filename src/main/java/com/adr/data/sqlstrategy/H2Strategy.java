/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sqlstrategy;

import com.adr.data.DataException;
import com.adr.data.Record;
import com.adr.data.sql.CommandSQL;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Eva
 */
public class H2Strategy extends SQLStrategy {
    
    // Strategy Upsert
    @Override
    protected void executeUpsert(Connection c, Record keyval) throws DataException {
        int rows = execute(c, buildCommandMerge(keyval), keyval);
        if (rows != 1) {
            throw new DataException("MERGE must return 1 row");
        }
    }
    
    protected final CommandSQL buildCommandMerge(Record keyval) {

        StringBuilder sentence = new StringBuilder();
        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();

        sentence.append("MERGE INTO ");
        sentence.append(getTableName(keyval));
        
        boolean filter = false;
        for (String f : keyval.getKey().getNames()) {
            if (!"_ENTITY".equals(f)) {
                sentence.append(filter ? ", " : "(");
                sentence.append(f);
                keys.append(filter ? ", " : "(");
                keys.append(f);
                values.append(filter ? ", ?" : " (?");
                fieldslist.add(f);                
                filter = true;
            }
        }        

        for (String f : keyval.getValue().getNames()) {
            sentence.append(filter ? ", " : "(");
            sentence.append(f);
            values.append(filter ? ", ?" : " (?");
            fieldslist.add(f);                
            filter = true;
        }

        sentence.append(") KEY");
        sentence.append(keys);
        sentence.append(") VALUES");
        sentence.append(values);
        sentence.append(")");
        
        return new CommandSQL(sentence.toString(), fieldslist.stream().toArray(String[]::new));
    }    
}
