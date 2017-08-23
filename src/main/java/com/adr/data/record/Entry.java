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
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantDouble;
import com.adr.data.var.VariantInt;
import com.adr.data.var.VariantString;

/**
 *
 * @author adrian
 */
public class Entry {
    
    private final String name;
    private final Variant value;
       
    public Entry(String name, String value) {
        this(name, new VariantString(value));
    }
    
    public Entry(String name, Integer value) {
        this(name, new VariantInt(value));
    }
    
    public Entry(String name, Boolean value) {
        this(name, new VariantBoolean(value));
    }
    
    public Entry(String name, Double value) {
        this(name, new VariantDouble(value));
    }
    
    public Entry(String name, Variant value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Variant getValue() {
        return value;
    }    
}
