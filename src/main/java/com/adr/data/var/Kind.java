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
import com.adr.data.Results;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author adrian
 */
public abstract class Kind {
    public final static Kind INT = new KindBuilderInt();
    public final static Kind LONG = new KindLong();
    public final static Kind STRING = new KindString(); 
    public final static Kind DOUBLE = new KindDouble(); 
    public final static Kind DECIMAL = new KindDecimal(); 
    public final static Kind BOOLEAN = new KindBoolean(); 
    public final static Kind INSTANT = new KindInstant(); 
    public final static Kind LOCALDATETIME = new KindLocalDateTime(); 
    public final static Kind LOCALDATE = new KindLocalDate(); 
    public final static Kind LOCALTIME = new KindLocalTime();  
    public final static Kind BYTES = new KindBytes(); 
    public final static Kind OBJECT = new KindObject();
    public final static Kind VOID = new KindVoid();
   
    public abstract Variant read(Results read) throws DataException;
     
    private static final Map<String, Kind> valuesof = new HashMap<>();
    
    static { 
        put(Kind.INT);
        put(Kind.LONG);
        put(Kind.STRING);
        put(Kind.DOUBLE);
        put(Kind.DECIMAL);
        put(Kind.BOOLEAN);
        put(Kind.INSTANT);
        put(Kind.LOCALDATETIME);
        put(Kind.LOCALDATE);
        put(Kind.LOCALTIME);
        put(Kind.BYTES);
        put(Kind.OBJECT);
        put(Kind.VOID);
    }
    
    private static void put(Kind kind) {
        valuesof.put(kind.toString(), kind);
    }
    
    public static final Kind valueOf(String value) {
        Kind kind = valuesof.get(value);
        if (kind == null) {
            throw new IllegalArgumentException("Kind not found: " + value);
        }
        return kind;
    }        
}
