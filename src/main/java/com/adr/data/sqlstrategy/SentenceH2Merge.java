/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sqlstrategy;

import com.adr.data.Record;
import com.adr.data.sql.CommandSQL;
import com.adr.data.sql.SentenceDML;
import java.util.ArrayList;

/**
 *
 * @author adrian
 */
public class SentenceH2Merge extends SentenceDML {

    @Override
    public String getName() {
        return "H2-MERGE";
    }
    
    @Override
    protected final CommandSQL build(Record keyval) {

        StringBuilder sentence = new StringBuilder();
        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();

        sentence.append("MERGE INTO ");
        sentence.append(getEntity(keyval));
        
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
