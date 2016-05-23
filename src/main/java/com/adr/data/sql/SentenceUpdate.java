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
public class SentenceUpdate extends SentenceDML {

    @Override
    public String getName() {
        return "UPDATE";
    }
    
    @Override
    protected final CommandSQL build(Record keyval) {

        StringBuilder sentence = new StringBuilder();
        ArrayList<String> keyfields = new ArrayList<>();

        sentence.append("UPDATE ");
        sentence.append(Sentence.getEntity(keyval));

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
}
