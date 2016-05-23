/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.DataException;
import com.adr.data.DataList;
import com.adr.data.Kind;
import com.adr.data.QueryLink;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.Values;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author adrian
 */
public class SQLQueryLink implements QueryLink {

    private DataSource ds = null;
    private final Map<String, Query> queries = new HashMap<>();

    public SQLQueryLink(DataSource ds, Query... queries) {
        this.ds = ds;
        for (Query v : queries) {
            this.queries.put(v.getName(), v);
        }
    }

    @Override
    public Record find(Record filter) throws DataException {
        try (Connection c = ds.getConnection()) {
            return find(c, filter);
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    
    private Record find(Connection c, Record filter) throws DataException {
        Query q = findQuery(filter);
        CommandSQL command = q.buildSQLCommand(filter);
        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLKindParameters kindparams = new SQLKindParameters(stmt, command.getParamNames());
            write(kindparams, filter.getKey());
            write(kindparams, filter.getValue());

            try (ResultSet resultset = stmt.executeQuery()) {
                List<RecordMap> r = new ArrayList<>();
                SQLKindResults kindresults = new SQLKindResults(resultset);
                if (resultset.next()) {
                    return new RecordMap(
                        read(q, kindresults, filter.getKey()), 
                        read(q, kindresults, filter.getValue()));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    
    @Override
    public DataList query(Record filter) throws DataException {
        try (Connection c = ds.getConnection()) {
            return query(c, filter);
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    private DataList query(Connection c, Record filter) throws DataException {
        Query q = findQuery(filter);
        CommandSQL command = q.buildSQLCommand(filter);
        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLKindParameters kindparams = new SQLKindParameters(stmt, command.getParamNames());
            write(kindparams, filter.getKey());
            write(kindparams, filter.getValue());

            try (ResultSet resultset = stmt.executeQuery()) {
                List<RecordMap> r = new ArrayList<>();
                SQLKindResults kindresults = new SQLKindResults(resultset);
                while (resultset.next()) {
                    r.add(new RecordMap(
                        read(q, kindresults, filter.getKey()), 
                        read(q, kindresults, filter.getValue())));
                }
                return new DataList(r);
            }
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    private void write(SQLKindParameters kindparams, Values param) throws DataException {

        if (param == null) {
            return;
        }
        for (String name : param.getNames()) {
            param.getKind(name).set(kindparams, name, param.getValue(name));
        }
    }

    private ValuesMap read(Query q, SQLKindResults kindresults, Values param) throws DataException {

        if (param == null) {
            return new ValuesMap();
        }
        List<ValuesEntry> l = new ArrayList<>();
        for (String name : param.getNames()) {
            if ("_ENTITY".equals(name)) {
                l.add(new ValuesEntry(name, param.getKind(name), param.getValue(name)));
            } else if (q.isField(name)) { 
                Kind k = param.getKind(name);
                l.add(new ValuesEntry(name, k, k.get(kindresults, name)));
            }
        }
        return new ValuesMap(l);
    }

    private Query findQuery(Record keyval) {
        String entity = keyval.getKey().getValue("_ENTITY").toString();
        Query q = queries.get(entity);
        if (q == null) {
            q = new QueryTable(entity);
        }
        return q;
    }
}
