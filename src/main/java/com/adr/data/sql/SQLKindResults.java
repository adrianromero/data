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
import com.adr.data.KindResults;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author adrian
 */
public final class SQLKindResults implements KindResults {
    
    private final ResultSet resultset;
    
    public SQLKindResults(ResultSet resultset) {
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
    public Number getInt(String columnName) throws DataException {
        try {
            int iValue = resultset.getInt(columnName);
            return resultset.wasNull() ? null : iValue;
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public Number getDouble(String columnName) throws DataException {
        try {
            double dValue = resultset.getDouble(columnName);
            return resultset.wasNull() ? null : dValue;
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
            boolean bValue = resultset.getBoolean(columnName);
            return resultset.wasNull() ? null : bValue;
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public java.util.Date getTimestamp(String columnName) throws DataException {
        try {
            java.sql.Timestamp ts = resultset.getTimestamp(columnName);
            return ts == null ? null : new java.util.Date(ts.getTime());
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public java.util.Date getDate(String columnName) throws DataException {
        try {
            java.sql.Date da = resultset.getDate(columnName);
            return da == null ? null : new java.util.Date(da.getTime());
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
    @Override
    public java.util.Date getTime(String columnName) throws DataException {
        try {
            java.sql.Time da = resultset.getTime(columnName);
            return da == null ? null : new java.util.Date(da.getTime());
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
