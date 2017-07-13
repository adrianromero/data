//     Data Access is a Java library to store data
//     Copyright (C) 2016-2017 Adrián Romero Corchado.
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author  adrian
 */
public interface Results {

    public String getString() throws DataException;
    public Integer getInt() throws DataException;
    public Long getLong() throws DataException;
    public Double getDouble() throws DataException;
    public BigDecimal getBigDecimal() throws DataException;
    public Boolean getBoolean() throws DataException;
    public Instant getInstant() throws DataException;
    public LocalDateTime getLocalDateTime() throws DataException;
    public LocalDate getLocalDate() throws DataException;
    public LocalTime getLocalTime() throws DataException;
    public byte[] getBytes() throws DataException;
    public Object getObject() throws DataException;
}