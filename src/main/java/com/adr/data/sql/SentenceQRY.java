/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.DataException;
import com.adr.data.DataList;
import com.adr.data.Record;
import java.sql.Connection;

/**
 *
 * @author adrian
 */
public abstract class SentenceQRY  extends Sentence {

    protected abstract CommandSQL build(Record keyval);
    
    @Override
    public DataList query(Connection c, Record keyval) throws DataException {
        return Sentence.query(c, build(keyval), keyval);
    }
    @Override
    public Record find(Connection c, Record keyval) throws DataException {
        return Sentence.find(c, build(keyval), keyval);
    }
}
