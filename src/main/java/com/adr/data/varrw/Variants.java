//     Data Access is a Java library to store data
//     Copyright (C) 2019 Adrián Romero Corchado.
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
package com.adr.data.varrw;

import com.adr.data.DataException;
import com.adr.data.var.Kind;
import com.adr.data.var.Variant;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantBytes;
import com.adr.data.var.VariantDecimal;
import com.adr.data.var.VariantDouble;
import com.adr.data.var.VariantFloat;
import com.adr.data.var.VariantInstant;
import com.adr.data.var.VariantInt;
import com.adr.data.var.VariantLocalDate;
import com.adr.data.var.VariantLocalDateTime;
import com.adr.data.var.VariantLocalTime;
import com.adr.data.var.VariantLong;
import com.adr.data.var.VariantString;

public class Variants {
    
    public static boolean isNull(Variant v) {
        return v == null || v.isNull();
    }

    public static Variant read(Results read, Kind kind) throws DataException {
        switch (kind) {
        case STRING: return new VariantString(read.getString());
        case INT: return new VariantInt(read.getInt());
        case LONG: return new VariantLong(read.getLong());
        case FLOAT: return new VariantFloat(read.getFloat());
        case DOUBLE: return new VariantDouble(read.getDouble());
        case DECIMAL: return new VariantDecimal(read.getBigDecimal());
        case BOOLEAN: return new VariantBoolean(read.getBoolean());
        case INSTANT: return new VariantInstant(read.getInstant());
        case LOCALDATETIME: return new VariantLocalDateTime(read.getLocalDateTime());
        case LOCALDATE: return new VariantLocalDate(read.getLocalDate());
        case LOCALTIME: return new VariantLocalTime(read.getLocalTime());
        case BYTES: return new VariantBytes(read.getBytes());
        default: throw new RuntimeException("Kind not exhaustive");
        }
    }

    public static void write(Parameters write, Variant v) throws DataException {
        switch (v.getKind()) {
        case STRING: write.setString(v.asString()); break;
        case INT: write.setInt(v.asInteger()); break;
        case LONG: write.setLong(v.asLong()); break;
        case FLOAT: write.setFloat(v.asFloat()); break;
        case DOUBLE: write.setDouble(v.asDouble()); break;
        case DECIMAL: write.setBigDecimal(v.asBigDecimal()); break;
        case BOOLEAN: write.setBoolean(v.asBoolean()); break;
        case INSTANT: write.setInstant(v.asInstant()); break;
        case LOCALDATETIME: write.setLocalDateTime(v.asLocalDateTime()); break;
        case LOCALDATE: write.setLocalDate(v.asLocalDate()); break;
        case LOCALTIME: write.setLocalTime(v.asLocalTime()); break;
        case BYTES: write.setBytes(v.asBytes()); break;
        default: throw new RuntimeException("Kind not exhaustive");
        }
    }
}
