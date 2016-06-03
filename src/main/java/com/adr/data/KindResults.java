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

package com.adr.data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author  adrian
 */
public interface KindResults {

    public String getString(String columnName) throws DataException;
    public Number getInt(String columnName) throws DataException;
    public Number getDouble(String columnName) throws DataException;
    public BigDecimal getBigDecimal(String columnName) throws DataException;
    public Boolean getBoolean(String columnName) throws DataException;
    public Date getTimestamp(String columnName) throws DataException;
    public Date getDate(String columnName) throws DataException;
    public Date getTime(String columnName) throws DataException;
    public byte[] getBytes(String columnName) throws DataException;
    public Object getObject(String columnName) throws DataException;
}