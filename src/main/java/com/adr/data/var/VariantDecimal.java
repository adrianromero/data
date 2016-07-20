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
package com.adr.data.var;

import com.adr.data.DataException;
import com.adr.data.Parameters;
import com.adr.data.Results;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantDecimal extends Variant {
    
    public final static VariantDecimal NULL = new VariantDecimal(null);
     
    private BigDecimal value;
    
    public VariantDecimal(BigDecimal value) {
        this.value = value;
    }
    
    protected VariantDecimal() {
        this(null);
    }

    @Override
    public Kind getKind() {
        return Kind.DECIMAL;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : value.toString();
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        try {
            this.value = value == null || value.equals("") ? null : new BigDecimal(value);       
        } catch (NumberFormatException ex) {
            throw new DataException(ex);
        }            
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setBigDecimal(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getBigDecimal(name);
    }
    
    @Override
    public boolean isNull() {
        return value == null;
    }
    
    @Override
    public Number asNumber() {
        return value;
    }
    @Override
    public Integer asInteger() {
        return value == null ? null : value.intValue();
    }
    
    @Override
    public Long asLong() {
        return value == null ? null : value.longValue();
    }
    
    @Override
    public Double asDouble() {
        return value == null ? null : value.doubleValue();
    }    
    
    @Override
    public BigDecimal asBigDecimal() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VariantDecimal other = (VariantDecimal) obj;
        return Objects.equals(this.value, other.value);
    } 
}
