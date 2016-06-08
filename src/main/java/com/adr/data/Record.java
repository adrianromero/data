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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author adrian
 */
public interface Record {
    public Values getKey();
    public Values getValue();
    
    public default String getString(String name) {
        return getValue().get(name).asString();    
    }
    
    public default int getInteger(String name) {
        return getValue().get(name).asInteger(); 
    }
    
    public default double getDouble(String name) {
        return getValue().get(name).asDouble();
    }    
    
    public default Boolean getBoolean(String name) {
        return getValue().get(name).asBoolean();
    }
    
    public default byte[] getBytes(String name) {
        return getValue().get(name).asBytes();
    }
        
}
