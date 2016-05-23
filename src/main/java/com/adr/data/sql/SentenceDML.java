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
public abstract class SentenceDML implements Sentence {

    protected abstract CommandSQL build(Record keyval);

    @Override
    public final void execute(Connection c, Record keyval) throws DataException {
        if (Sentence.execute(c, build(keyval), keyval) != 1) {
            throw new DataException("Sentence \"" + getName() + "\" must return 1 row.");
        }
    }         
}
