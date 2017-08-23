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

package com.adr.data.record;

import com.adr.data.var.Variant;
import com.adr.data.var.VariantVoid;

/**
 *
 * @author adrian
 */
public interface Record {

    public final static Record EMPTY = new Record() {
        private String [] NONAMES = {};
        @Override
        public String[] getNames() { 
            return NONAMES;
        }
        @Override
        public Variant get(String name) {
            return VariantVoid.INSTANCE;
        }
    };
    
    public String [] getNames();   
    public Variant get(String name);   
    
    public default String getString(String name) {
        return get(name).asString();    
    }
    
    public default int getInteger(String name) {
        return get(name).asInteger(); 
    }
    
    public default double getDouble(String name) {
        return get(name).asDouble();
    }    
    
    public default Boolean getBoolean(String name) {
        return get(name).asBoolean();
    }
    
    public default byte[] getBytes(String name) {
        return get(name).asBytes();
    }      
}
