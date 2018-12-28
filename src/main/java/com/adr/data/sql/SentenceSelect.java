//     Data Access is a Java library to store data
//     Copyright (C) 2016-2018 Adrián Romero Corchado.
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

import com.adr.data.DataException;
import com.adr.data.FilterBuilderMethods;
import java.util.ArrayList;
import java.util.List;
import com.adr.data.record.Record;
import com.adr.data.FilterBuilder;

/**
 *
 * @author adrian
 */
public abstract class SentenceSelect extends SentenceQRY {

    protected abstract String getViewName(Record record) throws DataException;

    @Override
    public CommandSQL build(SQLEngine engine, Record record) throws DataException {

        SentenceSelect.SentenceBuilder builder = new SentenceSelect.SentenceBuilder(engine);       
        FilterBuilderMethods.build(builder, record);
 
        StringBuilder sqlsent = new StringBuilder();
        sqlsent.append("SELECT ");
        sqlsent.append(builder.getSqlsent());
        sqlsent.append(" FROM ");
        sqlsent.append(getViewName(record));
        sqlsent.append(" TABLE_ALIAS");
        sqlsent.append(builder.getSqlfilter());
        
        SentenceQRY.addQueryOptions(sqlsent, engine, record);     

        return new CommandSQL(sqlsent.toString(), builder.getFieldsList());
    }
    
    private static class SentenceBuilder implements FilterBuilder {
        
        private final SQLEngine engine;

        private final StringBuilder sqlsent = new StringBuilder();
        private final StringBuilder sqlfilter = new StringBuilder();
        private final List<String> fieldslist = new ArrayList<>();
        private boolean comma = false;
        private boolean commafilter = false;
        
        public SentenceBuilder(SQLEngine engine) {
            this.engine = engine;
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
        
        private void filterCriteria(String name, String realname, String criteria) {
            
            if (name.equals("COLLECTION.KEY")) {
                return;
            }
            
            if (commafilter) {
                sqlfilter.append(" AND ");
            } else {
                sqlfilter.append(" WHERE ");
                commafilter = true;
            }    
            sqlfilter.append(realname);
            sqlfilter.append(criteria);
            fieldslist.add(name);            
        }

        @Override
        public void project(String name, String realname) {
            
            if (name.equals("COLLECTION.KEY")) {
                return;
            }
             
            if (comma) {
                sqlsent.append(", ");
            } else {
                comma = true;
            }
            sqlsent.append(realname);
            sqlsent.append(" AS \"");
            sqlsent.append(name);
            sqlsent.append("\"");
        }

        @Override
        public void filterEqual(String name, String realname) {
            filterCriteria(name, realname, " = ?");     
        }

        @Override
        public void filterDistinct(String name, String realname) {
            filterCriteria(name, realname, " <> ?");     
        }

        @Override
        public void filterGreater(String name, String realname) {
            filterCriteria(name, realname, " > ?");     
        }

        @Override
        public void filterGreaterOrEqual(String name, String realname) {
            filterCriteria(name, realname, " >= ?");     
        }

        @Override
        public void filterLess(String name, String realname) {
            filterCriteria(name, realname, " < ?");     
        }

        @Override
        public void filterLessOrEqual(String name, String realname) {
            filterCriteria(name, realname, " <= ?");     
        }

        @Override
        public void filterContains(String name, String realname) {
            filterCriteria(name, realname, engine.getContainsExpression());     
        }

        @Override
        public void filterStarts(String name, String realname) {
            filterCriteria(name, realname, engine.getStartsExpression());     
        }

        @Override
        public void filterEnds(String name, String realname) {
            filterCriteria(name, realname, engine.getEndsExpression());     
        }
    }   
}
