/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sqlstrategy;

import com.adr.data.DataException;
import com.adr.data.Record;
import com.adr.data.sql.Sentence;
import com.adr.data.sql.SentenceDelete;
import java.sql.Connection;

/**
 *
 * @author adrian
 */
public class SentenceH2Put implements Sentence {
    
    private final SentenceDelete delete = new SentenceDelete();
    private final SentenceH2Merge h2merge = new SentenceH2Merge();

    @Override
    public String getName() {
        return "H2-PUT";
    }

    @Override
    public void execute(Connection c, Record keyval) throws DataException {        
        if (keyval.getValue() == null) {
            delete.execute(c, keyval);
        } else {
            h2merge.execute(c, keyval);
        }
    }
}
