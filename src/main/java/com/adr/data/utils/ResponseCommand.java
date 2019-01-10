//     Data Access is a Java library to store data
//     Copyright (C) 2017-2018 Adrián Romero Corchado.
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

/**
 *
 * @author adrian
 */
public abstract class ResponseCommand {
    public abstract String getType();
    
    public abstract void write(Writer w) throws IOException;
    
    public abstract void getResult() throws DataException;    
    
    public final String write() throws IOException {
        StringWriter writer = new StringWriter();
        write(writer);
        return writer.toString();
    }
    
    public static ResponseCommand read(String value) throws IOException {
        return read(new StringReader(value));
    }
    
    public static final ResponseCommand read(Reader r) throws IOException {
        Loader loader = new StreamLoader(r);
        loader.next();
        String type = CommonParsers.parseWord(loader);
        loader.skipBlanks();
        if (ResponseCommandSuccess.NAME.equals(type)){
            return ResponseCommandSuccess.readData(loader);
        } else if (ResponseCommandError.NAME.equals(type)) {
            return ResponseCommandError.readData(loader);
        } else {
            throw new IOException(loader.messageExpected("Request Execute type name"));
        }
    }
}
