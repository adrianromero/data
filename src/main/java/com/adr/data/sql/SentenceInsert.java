/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.Record;
import java.util.ArrayList;

/**
 *
 * @author adrian
 */
public class SentenceInsert extends SentenceDML {

    @Override
    public String getName() {
        return "INSERT";
    }
    
    @Override
    protected final CommandSQL build(Record keyval) {

        StringBuilder sentence = new StringBuilder();
        StringBuilder values = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();

        sentence.append("INSERT INTO ");
        sentence.append(Sentence.getEntity(keyval));
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
}
