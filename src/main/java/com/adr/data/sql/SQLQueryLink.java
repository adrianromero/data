/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.DataException;
import com.adr.data.DataList;
import com.adr.data.QueryLink;
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
public class SQLQueryLink implements QueryLink {

    private DataSource ds = null;
    private final Sentence table;
    private final Map<String, Sentence> queries = new HashMap<>();

    public SQLQueryLink(DataSource ds, Sentence... queries) {
        this.ds = ds;
        this.table = new SentenceTable();
        for (Sentence v : queries) {
            this.queries.put(v.getName(), v);
        }
    }

    @Override
    public Record find(Record filter) throws DataException {
        try (Connection c = ds.getConnection()) {
            String entity = Sentence.getEntity(filter);
            Sentence s = queries.get(entity);
            if (s == null) {
                s = table;
            }
            return s.find(c, filter);
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    
    @Override
    public DataList query(Record filter) throws DataException {
        try (Connection c = ds.getConnection()) {
            String entity = Sentence.getEntity(filter);
            Sentence s = queries.get(entity);
            if (s == null) {
                s = table;
            }              
            return s.query(c, filter);
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
}
