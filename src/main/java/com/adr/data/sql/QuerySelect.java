/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.Record;
import com.adr.data.Values;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrian
 */
public abstract class QuerySelect implements Query {

    protected abstract String getViewName();
    
    @Override
    public CommandSQL buildSQLCommand(Record keyval) {

        QuerySelect.SentenceBuilder builder = new QuerySelect.SentenceBuilder();
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
        sqlsent.append(getViewName());
        sqlsent.append(" TABLE_ALIAS");
        sqlsent.append(builder.getSqlfilter());

        // build statement
        return new CommandSQL(sqlsent.toString(), builder.getFieldsList());
    }
    
    @Override
    public boolean isField(String name) {
        if (name.endsWith("_EQUAL")) {
            return false;
        }
        if (name.endsWith("_LIKE")) {
            return false;
        }
        return true;
    }
    
    private class SentenceBuilder {

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
            if (isField(n)) {
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
                    sqlfilter.append(" AND ");
                } else {
                    sqlfilter.append(" WHERE ");
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
