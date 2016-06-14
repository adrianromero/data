/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sqlpg;

import com.adr.data.Record;
import com.adr.data.sql.CommandSQL;
import com.adr.data.sql.Sentence;
import com.adr.data.sql.SentenceDML;
import java.util.ArrayList;

/**
 *
 * @author adrian
 */
public class SentencePGMerge extends SentenceDML {
    
    
     @Override
    public String getName() {
        return "PG-MERGE";
    }

    @Override
    protected CommandSQL build(Record keyval) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        StringBuilder sentence = new StringBuilder();
        StringBuilder values = new StringBuilder();
        StringBuilder conflict = new StringBuilder();
        ArrayList<String> fieldslist = new ArrayList<>();

        sentence.append("INSERT INTO ");
        sentence.append(Sentence.getEntity(keyval));
        sentence.append("(");
        
        conflict.append(" ON CONFLICT ");

        boolean filter = false;
        for (String f : keyval.getKey().getNames()) {
            if (!"_ENTITY".equals(f)) {
                sentence.append(filter ? ", " : "");
                sentence.append(f);

                values.append(filter ? ", ?" : "?");
                fieldslist.add(f);
                
                conflict.append(filter ? ", " : "(");
                conflict.append(f);

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
//  INSERT INTO products (
//    upc, 
//    title, 
//    description, 
//    link) 
//VALUES (
//    123456789, 
//    ‘Figment #1 of 5’, 
//    ‘THE NEXT DISNEY ADVENTURE IS HERE - STARRING ONE OF DISNEY'S MOST POPULAR CHARACTERS! ’, 
//    ‘http://www.amazon.com/dp/B00KGJVRNE?tag=mypred-20’
//    )
//ON CONFLICT (upc) DO UPDATE SET description=excluded.description;
    } 

  
}
