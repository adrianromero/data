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
import com.adr.data.Results;
import java.util.function.Supplier;

/**
 *
 * @author adrian
 */
public enum Kind {
    INT(VariantInt::new), 
    STRING(VariantString::new), 
    DOUBLE(VariantDouble::new), 
    DECIMAL(VariantDecimal::new), 
    BOOLEAN(VariantBoolean::new), 
    INSTANT(VariantInstant::new), 
    LOCALDATETIME(VariantLocalDateTime::new), 
    LOCALDATE(VariantLocalDate::new), 
    LOCALTIME(VariantLocalTime::new),  
    BYTES(VariantBytes::new), 
    OBJECT(VariantObject::new);
   
    private final Supplier<Variant> builder;
    
    private Kind(Supplier<Variant> builder) {
        this.builder = builder;
    }
    
    public Variant buildRead(Results read, String name) throws DataException {
        Variant v = builder.get();
        v.buildRead(read, name);
        return v;
    }
    
    public Variant buildISO(String value) throws DataException {
        Variant v = builder.get();
        v.buildISO(value);
        return v;
    }
}
