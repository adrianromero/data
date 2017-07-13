//     Data Access is a Java library to store data
//     Copyright (C) 2017 Adrián Romero Corchado.
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

package com.adr.data.var;

import com.adr.data.DataException;
import java.math.BigDecimal;
import com.adr.data.Results;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;

/**
 *
 * @author adrian
 */
public final class ISOResults implements Results {

    private final String value;
    
    public ISOResults(String value) {
        this.value = value;
    }
    
    @Override
    public String getString() throws DataException {
        return value;
    }
    @Override
    public Integer getInt() throws DataException {
        try {
            return value == null || value.equals("") ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new DataException(e);
        }             
    }
    @Override
    public Long getLong() throws DataException {
        try {
            return value == null || value.equals("") ? null : Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new DataException(e);
        }             
    }
    @Override
    public Double getDouble() throws DataException {
        try {
            return value == null || value.equals("") ? null : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new DataException(e);
        }             
    }
    @Override
    public BigDecimal getBigDecimal() throws DataException {
        try {
            return value == null || value.equals("") ? null : new BigDecimal(value);
        } catch (IllegalArgumentException e) {
            throw new DataException(e);
        }             
    }
    @Override
    public Boolean getBoolean() throws DataException {
        return value == null || value.equals("") ? null : Boolean.valueOf(value);
    }
    @Override
    public Instant getInstant() throws DataException {
        try {
            return value == null || value.equals("") ? null : Instant.parse(value);  
        } catch (DateTimeParseException e) {
            throw new DataException(e);
        }
    }
    @Override
    public LocalDateTime getLocalDateTime() throws DataException {
        try {
            return value == null || value.equals("") ? null : LocalDateTime.parse(value);  
        } catch (DateTimeParseException e) {
            throw new DataException(e);
        }
    }
    @Override
    public LocalDate getLocalDate() throws DataException {
        try {
            return value == null || value.equals("") ? null : LocalDate.parse(value);  
        } catch (DateTimeParseException e) {
            throw new DataException(e);
        } 
    }
    @Override
    public LocalTime getLocalTime() throws DataException {
        try {
            return value == null || value.equals("") ? null : LocalTime.parse(value);  
        } catch (DateTimeParseException e) {
            throw new DataException(e);
        }
    }        
    @Override
    public byte[] getBytes() throws DataException {
        try {
            return value == null ? null : Base64.getDecoder().decode(value);
        } catch(IllegalArgumentException e) {
            throw new DataException(e);
        } 
    }
    @Override
    public Object getObject() throws DataException {
        try {
            return value == null || value.equals("") ? null : Serializer.deserialize(value);  
        } catch (IOException | ClassNotFoundException e) {
            throw new DataException(e);
        } 
    }  
}
