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

import com.adr.data.record.Record;
import com.adr.data.recordparser.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrian
 */
public abstract class EnvelopeRequest {

    public abstract String getType();
    public abstract EnvelopeResponse process(ProcessRequest proc);
    public abstract void write(Writer w) throws IOException;
    public final String write() throws IOException {
        StringWriter writer = new StringWriter();
        write(writer);
        return writer.toString();
    }
    public static final EnvelopeRequest read(String value) throws IOException {
        return read(new StringReader(value));
    }
    public static final EnvelopeRequest read(Reader r) throws IOException {
        Loader loader = new StreamLoader(r);
        loader.next();
        String type = CommonParsers.parseWord(loader);
        loader.skipBlanks();
        if (RequestExecute.NAME.equals(type)){
            Record header = RecordParsers.parseRecord(loader);
            loader.skipBlanks();
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
            return new RequestExecute(header, recordsList);
        } else if (RequestQuery.NAME.equals(type)) {
            Record header = RecordParsers.parseRecord(loader);
            loader.skipBlanks();
            Record filter = RecordParsers.parseRecord(loader);
            loader.skipBlanks();
            if (CodePoint.isEOF(loader.getCP())) {
                return new RequestQuery(header, filter);
            } else {
                throw new IOException(loader.messageExpected(-1));
            }
        } else {
            throw new IOException(loader.messageExpected("Request type name"));
        }
    }
}
