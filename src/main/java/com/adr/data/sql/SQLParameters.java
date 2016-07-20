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

import com.adr.data.DataException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import com.adr.data.Parameters;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author adrian
 */
public final class SQLParameters implements Parameters {

    private final String[] params;
    private final PreparedStatement stmt;

    public SQLParameters(PreparedStatement stmt, String[] params) {
        this.stmt = stmt;
        this.params = params;
    }
    
    @FunctionalInterface
    private interface Setter {
        void set(int index) throws SQLException;
    }
    
    private void set(String paramName, Setter s) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null && params[i].equals(paramName)) {
                s.set(i + 1);
            }
        } 
    }     

    @Override
    public void setInt(String paramName, Integer value) throws DataException {
        try {  
            set(paramName, i -> stmt.setObject(i, value, Types.INTEGER));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public void setLong(String paramName, Long value) throws DataException {
        try {  
            set(paramName, i -> stmt.setObject(i, value, Types.BIGINT));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public void setString(String paramName, String value) throws DataException {
        try {              
            set(paramName, i -> stmt.setString(i, value));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public void setDouble(String paramName, Double value) throws DataException {
        try {               
            set(paramName, i -> stmt.setObject(i, value, Types.DOUBLE));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public void setBigDecimal(String paramName, BigDecimal value) throws DataException {
        try {                          
            set(paramName, i -> stmt.setBigDecimal(i, value));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    @Override
    public void setBoolean(String paramName, Boolean value) throws DataException {
        try {                         
            set(paramName, index -> stmt.setObject(index, value, Types.BOOLEAN));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    @Override
    public void setInstant(String paramName, Instant value) throws DataException {
        try {
            set(paramName, i -> stmt.setObject(i, value == null ? null : java.sql.Timestamp.from(value), Types.TIMESTAMP_WITH_TIMEZONE));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    @Override
    public void setLocalDateTime(String paramName, LocalDateTime value) throws DataException {
        try {
            set(paramName, i -> stmt.setObject(i, value == null ? null : java.sql.Timestamp.valueOf(value), Types.TIMESTAMP));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    @Override
    public void setLocalDate(String paramName, LocalDate value) throws DataException {
        try {     
            set(paramName, i -> stmt.setObject(i, value == null ? null : java.sql.Date.valueOf(value), Types.DATE));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    
    @Override
    public void setLocalTime(String paramName, LocalTime value) throws DataException {
        try {     
            set(paramName, i -> stmt.setObject(i, value == null ? null : java.sql.Time.valueOf(value), Types.TIME));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }        

    @Override
    public void setBytes(String paramName, byte[] value) throws DataException {
        try {     
            set(paramName, i -> stmt.setBytes(i, value));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }

    @Override
    public void setObject(String paramName, Object value) throws DataException {
        try {     
            set(paramName, i -> stmt.setObject(i, value));
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
}
