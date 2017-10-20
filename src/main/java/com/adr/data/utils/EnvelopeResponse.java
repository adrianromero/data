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

package com.adr.data.utils;

import com.adr.data.DataException;
import com.adr.data.recordparser.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public abstract class EnvelopeResponse {
    public abstract String getType();
    public abstract void write(Writer w) throws IOException;
    public final String write() throws IOException {
        StringWriter writer = new StringWriter();
        write(writer);
        return writer.toString();
    }
    public static final EnvelopeResponse read(String value) throws IOException {
        return read(new StringReader(value));
    }
    public static final EnvelopeResponse read(Reader r) throws IOException {
        Loader loader = new StreamLoader(r);
        loader.next();
        String type = CommonParsers.parseWord(loader);
        loader.skipBlanks();
        if (ResponseSuccess.NAME.equals(type)){
            if (CodePoint.isEOF(loader.getCP())) {
                return new ResponseSuccess();
            } else {
                throw new IOException(loader.messageExpected(-1));
            }
        } else if (ResponseError.NAME.equals(type)) {
            Record error = RecordParsers.parseRecord(loader);
            loader.skipBlanks();
            if (CodePoint.isEOF(loader.getCP())) {
                String name = error.getString("EXCEPTION");
                String message = error.getString("MESSAGE");
                Throwable t;
                try {
                    t = (Throwable) Class.forName(name).getConstructor(String.class).newInstance(message);
                } catch (ClassCastException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    t = new DataException("Exception name: " + name + ", with message: " + message);
                }
                return new ResponseError(t);
            } else {
                throw new IOException(loader.messageExpected(-1));
            }
        } else if (ResponseListRecord.NAME.equals(type)) {
            List<Record> recordsList = new ArrayList<>();
            for (;;) {
                if (loader.getCP() == '(') {
                    recordsList.add(RecordParsers.parseRecord(loader));
                    loader.skipBlanks();
                } else if (CodePoint.isEOF(loader.getCP())) {
                    break;
                } else {
                    throw new IOException(loader.messageExpected(-1));
                }
            }
            return new ResponseListRecord(recordsList);
        } else {
            throw new IOException(loader.messageExpected("Response type name"));
        }
    }
    public List<Record> getAsListRecord() throws DataException {
        throw new DataException("List of records response not supported.");
    }
    public void asSuccess() throws DataException {
        throw new DataException("List of records response not supported.");
    }    
}
