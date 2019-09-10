//     Data Access is a Java library to store data
//     Copyright (C) 2016-2019 Adrián Romero Corchado.
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

package com.adr.data.varrw;

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
public interface Parameters {

    public void setString(String value) throws DataException;
    public void setInt(Integer value) throws DataException;
    public void setLong(Long value) throws DataException;
    public void setFloat(Float value) throws DataException;
    public void setDouble(Double value) throws DataException;
    public void setBigDecimal(BigDecimal value) throws DataException;
    public void setBoolean(Boolean value) throws DataException;
    public void setInstant(Instant value) throws DataException;
    public void setLocalDateTime(LocalDateTime value) throws DataException;
    public void setLocalDate(LocalDate value) throws DataException;
    public void setLocalTime(LocalTime value) throws DataException;
    public void setBytes(byte[] value) throws DataException;
}