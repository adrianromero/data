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
public interface KindParameters {

    public void setString(String name, String value) throws DataException;
    public void setInt(String name, Number value) throws DataException;
    public void setDouble(String name, Number value) throws DataException;
    public void setBigDecimal(String name, BigDecimal value) throws DataException;
    public void setBoolean(String name, Boolean value) throws DataException;
    public void setTimestamp(String name, Date value) throws DataException;
    public void setDate(String name, Date value) throws DataException;
    public void setTime(String name, Date value) throws DataException;
    public void setBytes(String name, byte[] value) throws DataException;
    public void setObject(String name, Object value) throws DataException;
}