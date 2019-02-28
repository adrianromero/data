//     Data Access is a Java library to store data
//     Copyright (C) 2016-2019 Adrián Romero Corchado.
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

package com.adr.data.utils;

import com.adr.data.DataException;
import com.adr.data.recordparser.*;

import java.io.*;
import java.util.List;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public abstract class ResponseLink {
    
    public abstract String getType();
    
    public abstract List<Record> getResult() throws DataException;
    
    public abstract void write(Writer w) throws IOException;
    
    public final String write() throws IOException {
        StringWriter writer = new StringWriter();
        write(writer);
        return writer.toString();
    }
    
    public static final ResponseLink read(String value) throws IOException {
        return read(new StringReader(value));
    }
    
    public static final ResponseLink read(Reader r) throws IOException {
        Loader loader = new StreamLoader(r);
        loader.next();
        String type = CommonParsers.parseWord(loader);
        loader.skipBlanks();
        if (ResponseError.NAME.equals(type)) {
            return ResponseError.readData(loader);
        } else if (ResponseSuccess.NAME.equals(type)) {
            return ResponseSuccess.readData(loader);
        } else {
            throw IOExceptionMessage.createExpected(loader, "Response query type name");
        }
    }
}
