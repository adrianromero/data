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
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;

/**
 *
 * @author adrian
 */
public final class ISOParameters implements Parameters {
    
    private String iso = "";

    public ISOParameters() {
    }  
    
    @Override
    public String toString() {
        return iso;
    }

    @Override
    public void setInt(Integer value) throws DataException {
        iso = value == null ? null : value.toString();
    }
    @Override
    public void setLong(Long value) throws DataException {
        iso = value == null ? null : value.toString();
    }
    @Override
    public void setString(String value) throws DataException {
        iso = value;
    }
    @Override
    public void setDouble(Double value) throws DataException {
        iso = value == null ? null : value.toString();
    }
    @Override
    public void setBigDecimal(BigDecimal value) throws DataException {
        iso = value == null ? null : value.toString();
    }

    @Override
    public void setBoolean(Boolean value) throws DataException {
        iso = value == null ? null : value.toString();
    }

    @Override
    public void setInstant(Instant value) throws DataException {
        iso = value == null ? null : value.toString();
    }

    @Override
    public void setLocalDateTime(LocalDateTime value) throws DataException {
        iso = value == null ? null : value.toString();
    }

    @Override
    public void setLocalDate(LocalDate value) throws DataException {
        iso = value == null ? null : value.toString();
    }
    
    @Override
    public void setLocalTime(LocalTime value) throws DataException {
        iso = value == null ? null : value.toString();
    }        

    @Override
    public void setBytes(byte[] value) throws DataException {
        iso = value == null ? null : Base64.getEncoder().encodeToString(value);
    }

    @Override
    public void setObject(Object value) throws DataException {
        try {
            iso = value == null ? null : Serializer.serialize(value);
        } catch (IOException ex) {
            iso = null;
            throw new DataException(ex);
        }
    }
}
