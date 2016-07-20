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
import java.sql.ResultSet;
import java.sql.SQLException;
import com.adr.data.Results;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author adrian
 */
public final class SQLResults implements Results {
    
    private final ResultSet resultset;
    
    public SQLResults(ResultSet resultset) {
        this.resultset = resultset;
    }
    
    @Override
    public String getString(String columnName) throws DataException {
        try {
            return resultset.getString(columnName);
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public Integer getInt(String columnName) throws DataException {
        try {
            int value = resultset.getInt(columnName);
            return resultset.wasNull() ? null : value;
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public Long getLong(String columnName) throws DataException {
        try {
            long value = resultset.getLong(columnName);
            return resultset.wasNull() ? null : value;
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public Double getDouble(String columnName) throws DataException {
        try {
            double value = resultset.getDouble(columnName);
            return resultset.wasNull() ? null : value;
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public BigDecimal getBigDecimal(String columnName) throws DataException {
        try {
            return resultset.getBigDecimal(columnName);
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public Boolean getBoolean(String columnName) throws DataException {
        try {
            boolean value = resultset.getBoolean(columnName);
            return resultset.wasNull() ? null : value;
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public Instant getInstant(String columnName) throws DataException {
        try {
            java.sql.Timestamp ts = resultset.getTimestamp(columnName);
            return ts == null ? null : ts.toInstant();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public LocalDateTime getLocalDateTime(String columnName) throws DataException {
        try {
            java.sql.Timestamp ts = resultset.getTimestamp(columnName);
            return ts == null ? null : ts.toLocalDateTime();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public LocalDate getLocalDate(String columnName) throws DataException {
        try {
            java.sql.Date da = resultset.getDate(columnName);
            return da == null ? null : da.toLocalDate();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public LocalTime getLocalTime(String columnName) throws DataException {
        try {
            java.sql.Time da = resultset.getTime(columnName);
            return da == null ? null : da.toLocalTime();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }        
    @Override
    public byte[] getBytes(String columnName) throws DataException {
        try {
            return resultset.getBytes(columnName);
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public Object getObject(String columnName) throws DataException {
        try {
            return resultset.getObject(columnName);
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }  
}
