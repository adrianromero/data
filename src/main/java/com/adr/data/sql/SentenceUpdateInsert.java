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
public class SentenceUpdateInsert implements Sentence {
    
    private final SentenceInsert insert = new SentenceInsert();
    private final SentenceUpdate update = new SentenceUpdate();

    @Override
    public String getName() {
        return "UPDATE-INSERT";
    }

    @Override
    public void execute(Connection c, Record keyval) throws DataException {
        
        int rows = Sentence.execute(c, update.build(keyval), keyval);
        if (rows == 0) {
            if (Sentence.execute(c, insert.build(keyval), keyval) != 1) {
                throw new DataException("Sentence \"" + insert.getName() + "\" in \"" + getName() + "\" must return 1 row.");
            }
        } else if (rows != 1) {
            throw new DataException("Sentence \"" + update.getName() + "\" in \"" + getName() + "\" must return 0 or 1 row.");
        }
    }
}
