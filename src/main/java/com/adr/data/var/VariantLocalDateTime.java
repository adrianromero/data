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

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantLocalDateTime extends Variant {

    public final static VariantLocalDateTime NULL = new VariantLocalDateTime(null);

    private final LocalDateTime value;
    
    public VariantLocalDateTime(LocalDateTime value) {
        this.value = value;
    }

    @Override
    public Kind getKind() {
        return Kind.LOCALDATETIME;
    }
    
    @Override
    public boolean isNull() {
        return value == null;
    }
    
    @Override
    public LocalDateTime asLocalDateTime() {
        return value;
    }

    @Override
    public Object asObject() {
        return value;
    }
    
    @Override
    public int hashCode() {
        return 79 + Objects.hashCode(this.value);
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
        final VariantLocalDateTime other = (VariantLocalDateTime) obj;
        return Objects.equals(this.value, other.value);
    }
}
