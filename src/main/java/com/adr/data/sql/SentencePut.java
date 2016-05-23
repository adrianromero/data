/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.DataException;
import com.adr.data.Record;
import java.sql.Connection;

/**
 *
 * @author adrian
 */
public class SentencePut implements Sentence {
    
    private final SentenceDelete delete = new SentenceDelete();
    private final SentenceUpdateInsert updateinsert = new SentenceUpdateInsert();

    @Override
    public String getName() {
        return "PUT";
    }

    @Override
    public void execute(Connection c, Record keyval) throws DataException {        
        if (keyval.getValue() == null) {
            delete.execute(c, keyval);
        } else {
            updateinsert.execute(c, keyval);
        }
    }
}
