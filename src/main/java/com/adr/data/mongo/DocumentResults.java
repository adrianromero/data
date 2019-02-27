//     Data Access is a Java library to store data
//     Copyright (C) 2018-2019 Adrián Romero Corchado.
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
package com.adr.data.mongo;

import com.adr.data.DataException;
import com.adr.data.var.Results;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.bson.Document;

/**
 *
 * @author adrian
 */
public class DocumentResults implements Results {
    
    private final Document document;
    private final String realname;
    
    public DocumentResults(Document document, String columnName) {
        this.document = document;
        this.realname = columnName.endsWith(".KEY") 
                ? columnName.substring(0, columnName.length() - 4)
                : columnName;
    }
    
    @Override
    public String getString() throws DataException {
        return document.getString(realname);
    }

    @Override
    public Integer getInt() throws DataException {
        return document.getInteger(realname);
    }

    @Override
    public Long getLong() throws DataException {
        return document.getLong(realname);
    }

    @Override
    public Float getFloat() throws DataException {
        Double d = document.getDouble(realname);
        return d == null ? null : d.floatValue();
    }

    @Override
    public Double getDouble() throws DataException {
        return document.getDouble(realname);
    }

    @Override
    public BigDecimal getBigDecimal() throws DataException {
        return new BigDecimal(document.getString(realname));
    }

    @Override
    public Boolean getBoolean() throws DataException {
        return document.getBoolean(realname);
    }

    @Override
    public Instant getInstant() throws DataException {
        return Instant.ofEpochMilli(document.getLong(realname));
    }

    @Override
    public LocalDateTime getLocalDateTime() throws DataException {
        return LocalDateTime.parse(document.getString(realname));
    }

    @Override
    public LocalDate getLocalDate() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LocalTime getLocalTime() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] getBytes() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    } 
}
