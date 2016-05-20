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

    private final Map<String, SQLView> viewsmap = new HashMap<>();

    public SQLQueryLink(DataSource ds, SQLView... views) {
        this.ds = ds;
        for (SQLView v : views) {
            addView(v);
        }
    }

    protected void addView(SQLView v) {
        viewsmap.put(v.getName(), v);
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
        CommandSQL command = buildSQLCommand(filter);
        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLKindParameters kindparams = new SQLKindParameters(stmt, command.getParamNames());
            write(kindparams, filter.getKey());
            write(kindparams, filter.getValue());

            try (ResultSet resultset = stmt.executeQuery()) {
                List<RecordMap> r = new ArrayList<>();
                SQLKindResults kindresults = new SQLKindResults(resultset);
                if (resultset.next()) {
                    return new RecordMap(read(kindresults, filter.getKey()), read(kindresults, filter.getValue()));
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
        CommandSQL command = buildSQLCommand(filter);
        try (PreparedStatement stmt = c.prepareStatement(command.getCommand())) {
            SQLKindParameters kindparams = new SQLKindParameters(stmt, command.getParamNames());
            write(kindparams, filter.getKey());
            write(kindparams, filter.getValue());

            try (ResultSet resultset = stmt.executeQuery()) {
                List<RecordMap> r = new ArrayList<>();
                SQLKindResults kindresults = new SQLKindResults(resultset);
                while (resultset.next()) {
                    r.add(new RecordMap(read(kindresults, filter.getKey()), read(kindresults, filter.getValue())));
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

    private ValuesMap read(SQLKindResults kindresults, Values param) throws DataException {

        if (param == null) {
            return new ValuesMap();
        }
        List<ValuesEntry> l = new ArrayList<>();
        for (String name : param.getNames()) {
            if ("_ENTITY".equals(name)) {
                l.add(new ValuesEntry(name, param.getKind(name), param.getValue(name)));
            } else if (isValidName(name)) { 
                Kind k = param.getKind(name);
                l.add(new ValuesEntry(name, k, k.get(kindresults, name)));
            }
        }
        return new ValuesMap(l);
    }

    private String getViewName(Record keyval) {
        String entity = keyval.getKey().getValue("_ENTITY").toString();
        SQLView v = viewsmap.get(entity);
        return (v == null ? entity : "(" + v.getSentence() + ")") + " TABLE_ALIAS";
    }
    
    private static boolean isValidName(String name) {
        if (name.endsWith("_EQUAL")) {
            return false;
        }
        if (name.endsWith("_LIKE")) {
            return false;
        }
        return true;
    }

    private CommandSQL buildSQLCommand(Record keyval) {

        SQLQueryLink.SentenceBuilder builder = new SQLQueryLink.SentenceBuilder();
        StringBuilder sqlsent = new StringBuilder();

        for (String f : keyval.getKey().getNames()) {
            if (!"_ENTITY".equals(f)) {
                builder.add(keyval.getKey(), f);
            }
        }
        for (String f : keyval.getValue().getNames()) {
            builder.add(keyval.getValue(), f);
        }
        sqlsent.append("SELECT ");
        sqlsent.append(builder.getSqlsent());
        sqlsent.append(" FROM ");
        sqlsent.append(getViewName(keyval));
        sqlsent.append(builder.getSqlfilter());

        // build statement
        return new CommandSQL(sqlsent.toString(), builder.getFieldsList());
    }

    private static class SentenceBuilder {

        private final StringBuilder sqlsent = new StringBuilder();
        private final StringBuilder sqlfilter = new StringBuilder();
        private final List<String> fieldslist = new ArrayList<>();
        private boolean comma = false;
        private boolean commafilter = false;

        public void add(Values v, String n) {
            // FILTERING THINGS
            String realname;
            String criteria;
            if (n.endsWith("_EQUAL")) {
                realname = n.substring(0, n.length() - 6);
                criteria = " = ?";
            } else if (n.endsWith("_LIKE")) {
                realname = n.substring(0, n.length() - 5);
                criteria = " LIKE ? {escape '$'}";
            } else {
                realname = n;
                criteria = " = ?";
            }
            // PROJECTION
            if (isValidName(n)) {
                if (comma) {
                    sqlsent.append(", ");
                } else {
                    comma = true;
                }
                sqlsent.append(realname);
            }
            // FILTER
            if (v.getValue(n) != null) {
                if (commafilter) {
                    getSqlfilter().append(" AND ");
                } else {
                    getSqlfilter().append(" WHERE ");
                    commafilter = true;
                }
                sqlfilter.append(realname);
                sqlfilter.append(criteria);
                fieldslist.add(n);
            }
        }

        public StringBuilder getSqlsent() {
            return sqlsent;
        }

        public StringBuilder getSqlfilter() {
            return sqlfilter;
        }

        public String[] getFieldsList() {
            return fieldslist.stream().toArray(String[]::new);
        }
    }
}
