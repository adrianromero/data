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
import java.util.Base64;

/**
 *
 * @author adrian
 */
class KindBytes extends Kind {
    
    @Override
    public Variant fromISO(String value) throws DataException {
        try {
            return value == null ? VariantBytes.NULL : new VariantBytes(Base64.getDecoder().decode(value));
        } catch(IllegalArgumentException e) {
            throw new DataException(e);
        }            
    }
    
    @Override
    public Variant read(Results read, String name) throws DataException {
        return new VariantBytes(read.getBytes(name));
    }    
    
    @Override
    public String toString() {
        return "BYTES";
    }
}
