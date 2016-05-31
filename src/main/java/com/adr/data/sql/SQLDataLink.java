/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.DataLink;
import com.adr.data.DataException;
import com.adr.data.DataList;
import com.adr.data.Record;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author adrian
 */
public class SQLDataLink implements DataLink {

    private final DataSource ds;
    private final Sentence put;
    private final Map<String, Sentence> sentences = new HashMap<>();
    
    public SQLDataLink(DataSource ds, Sentence put, Sentence... sentences) {
        this.ds = ds;
        this.put = put;
        for (Sentence s : sentences) {
            this.sentences.put(s.getName(), s);
        }
    }

    public SQLDataLink(DataSource ds) {
        this(ds, new SentencePut());
    }

    @Override
    public void execute(DataList l) throws DataException {
        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            for (Record keyval : l.getList()) {
                Sentence s = sentences.get(Sentence.getEntity(keyval));
                if (s == null) {  
                    s = put;
                }   
                s.execute(c, keyval);
            }
            c.commit();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
}
