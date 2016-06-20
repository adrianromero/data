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
import java.util.Arrays;
import java.util.Base64;

/**
 *
 * @author adrian
 */
public class VariantBytes extends Variant {

    public final static VariantBytes NULL = new VariantBytes(null); 
    
    private byte[] value;
    
    public VariantBytes(byte[] value) {
        this.value = value;
    }
    
    public VariantBytes() {
        this(null);
    }

    @Override
    public Kind getKind() {
        return Kind.BYTES;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : Base64.getEncoder().encodeToString(value);
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        try {
            this.value = value == null || value.equals("") ? null : Base64.getDecoder().decode(value);                
        } catch(IllegalArgumentException e) {
            throw new DataException(e);
        }            
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setBytes(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getBytes(name);
    }
    
    @Override
    public boolean isNull() {
        return value == null;
    }
    
    @Override
    public byte[] asBytes() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Arrays.hashCode(this.value);
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
        final VariantBytes other = (VariantBytes) obj;
        return Arrays.equals(this.value, other.value);
    }
}
