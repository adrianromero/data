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
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantBoolean extends Variant {
    
    public final static VariantBoolean NULL = new VariantBoolean(null);
    public final static VariantBoolean TRUE = new VariantBoolean(Boolean.TRUE);
    public final static VariantBoolean FALSE = new VariantBoolean(Boolean.FALSE);    
    
    private Boolean value;
    
    public VariantBoolean(Boolean value) {
        this.value = value;
    }
    
    protected VariantBoolean() {
        this(null);
    }

    @Override
    public Kind getKind() {
        return Kind.BOOLEAN;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : value.toString();
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        this.value = value == null || value.equals("") ? null : Boolean.valueOf(value);                
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setBoolean(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getBoolean(name);
    }
    
    @Override
    public boolean isNull() {
        return value == null;
    }
    
    @Override
    public Boolean asBoolean() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.value);
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
        final VariantBoolean other = (VariantBoolean) obj;
        return Objects.equals(this.value, other.value);
    }
}
