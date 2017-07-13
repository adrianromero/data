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
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantDouble extends Variant {
    
    public final static VariantDouble NULL = new VariantDouble(null);
    
    private final Double value;
    
    public VariantDouble(Double value) {
        this.value = value;
    }
    
    @Override
    public Kind getKind() {
        return Kind.DOUBLE;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : value.toString();
    }
    
    @Override
    public void write(Parameters write) throws DataException {
        write.setDouble(value);
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
        return value;
    }    
    
    @Override
    public BigDecimal asBigDecimal() {
        return value == null ? null : new BigDecimal(value);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.value);
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
        final VariantDouble other = (VariantDouble) obj;
        return Objects.equals(this.value, other.value);
    } 
}
