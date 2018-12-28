//     Data Access is a Java library to store data
//     Copyright (C) 2016-2018 Adrián Romero Corchado.
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

import java.time.Instant;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantInstant extends Variant {

    public final static VariantInstant NULL = new VariantInstant(null);

    private final Instant value;
    
    public VariantInstant(Instant value) {
        this.value = value;
    }

    @Override
    public Kind getKind() {
        return Kind.INSTANT;
    }

    @Override
    public boolean isNull() {
        return value == null;
    }
      
    @Override
    public Instant asInstant() {
        return value;
    }

    @Override
    public Object asObject() {
        return value;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.value);
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
        final VariantInstant other = (VariantInstant) obj;
        return Objects.equals(this.value, other.value);
    }
}
