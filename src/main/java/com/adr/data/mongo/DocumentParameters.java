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
import com.adr.data.var.Parameters;
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
public class DocumentParameters implements Parameters {
    
    private final Document document;
    private final String name;
    
    DocumentParameters(Document document, String name) {
        this.document = document;
        this.name = name;
    }

    @Override
    public void setString(String value) throws DataException {
        document.append(name, value); // if name = _id then OID de mongo?
    }

    @Override
    public void setInt(Integer value) throws DataException {
        document.append(name, value);
    }

    @Override
    public void setLong(Long value) throws DataException {
        document.append(name, value);
    }

    @Override
    public void setDouble(Double value) throws DataException {
        document.append(name, value);
    }

    @Override
    public void setBigDecimal(BigDecimal value) throws DataException {
        document.append(name, value == null ? null : value.toString());
    }

    @Override
    public void setBoolean(Boolean value) throws DataException {
        document.append(name, value);
    }

    @Override
    public void setInstant(Instant value) throws DataException {
        document.append(name, value == null ? null : value.toEpochMilli());
    }

    @Override
    public void setLocalDateTime(LocalDateTime value) throws DataException {
        document.append(name, name == null ? null : value.toString());
    }

    @Override
    public void setLocalDate(LocalDate value) throws DataException {
        document.append(name, name == null ? null : value.toString());
    }

    @Override
    public void setLocalTime(LocalTime value) throws DataException {
        document.append(name, name == null ? null : value.toString());
    }

    @Override
    public void setBytes(byte[] value) throws DataException {
        document.append(name, value);
    }
}
