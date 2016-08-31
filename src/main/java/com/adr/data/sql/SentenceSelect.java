//     Data Access is a Java library to store data
//     Copyright (C) 2016 Adrián Romero Corchado.
//
//     This file is part of Data Access
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//     
//         http://www.apache.org/licenses/LICENSE-2.0
//     
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.

package com.adr.data.sql;

import com.adr.data.QueryOptions;
import com.adr.data.Record;
import com.adr.data.Values;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrian
 */
public abstract class SentenceSelect extends SentenceQRY {

    protected abstract String getViewName(Record keyval);

    @Override
    public CommandSQL build(Record keyval, QueryOptions options) {

        SentenceSelect.SentenceBuilder builder = new SentenceSelect.SentenceBuilder();
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
        sqlsent.append(" TABLE_ALIAS");
        sqlsent.append(builder.getSqlfilter());
        
        // Append order and limit
        if (options.getLimit() < Integer.MAX_VALUE) {
            sqlsent.append(" LIMIT ");
            sqlsent.append(options.getLimit());
        }
        if (options.getOffset() > 0) {
            sqlsent.append(" OFFSET ");
            sqlsent.append(options.getOffset());
        }   

        // build statement
        return new CommandSQL(sqlsent.toString(), builder.getFieldsList());
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
            if (n.endsWith("::EQUAL")) {
                realname = n.substring(0, n.length() - 7);
                criteria = " = ?";
            } else if (n.endsWith("::LIKE")) {
                realname = n.substring(0, n.length() - 6);
                criteria = " LIKE ? {escape '$'}";
            } else {
                realname = n;
                criteria = " = ?";
            }
            // PROJECTION
            if (!n.contains("::")) {
                if (comma) {
                    sqlsent.append(", ");
                } else {
                    comma = true;
                }
                sqlsent.append(realname);
            }
            // FILTER
            if (!v.get(n).isNull()) {
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
