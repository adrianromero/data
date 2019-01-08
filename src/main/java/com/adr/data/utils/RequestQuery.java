//     Data Access is a Java library to store data
//     Copyright (C) 2016-2018 Adrián Romero Corchado.
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
import com.adr.data.QueryLink;
import com.adr.data.record.Header;
import com.adr.data.recordparser.RecordsSerializer;
import com.adr.data.record.Record;
import com.adr.data.recordparser.CodePoint;
import com.adr.data.recordparser.Loader;
import com.adr.data.recordparser.RecordParsers;
import com.adr.data.recordparser.StreamLoader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class RequestQuery {
    
    private final Header headers;
    private final Record filter;
    
    public RequestQuery(Header headers, Record filter) {
        this.headers = headers;
        this.filter = filter;
    }
    
    public String write() throws IOException {
        StringWriter writer = new StringWriter();
        write(writer);
        return writer.toString();
    }
    
    public void write(Writer w) throws IOException {
        RecordsSerializer.write(headers.getRecord(), w);
        w.append('\n');
        RecordsSerializer.write(filter, w);
    }
    
    public static RequestQuery read(String value) throws IOException {
        return read(new StringReader(value));
    }    
    
    public static RequestQuery read(Reader r) throws IOException {
        Loader loader = new StreamLoader(r);
        loader.next();
        loader.skipBlanks();
        Header header = new Header(RecordParsers.parseRecord(loader));
        loader.skipBlanks();
        Record filter = RecordParsers.parseRecord(loader);
        loader.skipBlanks();
        if (CodePoint.isEOF(loader.getCP())) {
            return new RequestQuery(header, filter);
        } else {
            throw new IOException(loader.messageExpected(-1));
        }
    } 
    
    public static String serverQueryProcess(QueryLink link, String message, Logger logger) throws IOException {

        RequestQuery request = RequestQuery.read(message);
        logger.log(Level.CONFIG, "Processing Query: {0}.", new Object[]{message});

        try {
            return new ResponseQueryListRecord(link.query(request.headers, request.filter)).write();
        } catch (DataException ex) {
            logger.log(Level.SEVERE, "Cannot execute query request.", ex);
            return new ResponseExecuteError(ex).write();
        }
    }    
}
