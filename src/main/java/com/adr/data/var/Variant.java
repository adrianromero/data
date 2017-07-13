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
import com.adr.data.Parameters;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author adrian
 */
public abstract class Variant {
    
    public abstract Kind getKind();
    
    public abstract void write(Parameters write) throws DataException;

    public abstract boolean isNull();
    
    public String asString() {
        throw new UnsupportedOperationException("Variant cannot be converted to string.");
    }
    
    public Number asNumber() {
        throw new UnsupportedOperationException("Variant cannot be converted to number.");
    }
    
    public Integer asInteger() {
        throw new UnsupportedOperationException("Variant cannot be converted to integer.");
    }
    
    public Long asLong() {
        throw new UnsupportedOperationException("Variant cannot be converted to long.");
    }
    
    public Double asDouble() {
        throw new UnsupportedOperationException("Variant cannot be converted to double.");
    }    
    
    public BigDecimal asBigDecimal() {
        throw new UnsupportedOperationException("Variant cannot be converted to decimal.");
    }
    
    public Boolean asBoolean() {
        throw new UnsupportedOperationException("Variant cannot be converted to boolean.");
    }
    
    public LocalTime asLocalTime() {
        throw new UnsupportedOperationException("Variant cannot be converted to local time.");
    }
    
    public LocalDate asLocalDate() {
        throw new UnsupportedOperationException("Variant cannot be converted to local date.");
    }
    
    public LocalDateTime asLocalDateTime() {
        throw new UnsupportedOperationException("Variant cannot be converted to local date time.");
    }
    
    public Instant asInstant() {
        throw new UnsupportedOperationException("Variant cannot be converted to instant.");
    }
    
    public byte[] asBytes() {
        throw new UnsupportedOperationException("Variant cannot be converted to bytes.");
    }
    
    public Object asObject() {
        throw new UnsupportedOperationException("Variant cannot be converted to serialized object.");
    }
}
