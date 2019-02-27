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

import com.adr.data.AsyncQueryLink;
import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.record.Header;
import com.adr.data.recordparser.RecordsSerializer;
import com.adr.data.record.Record;
import com.adr.data.recordparser.CodePoint;
import com.adr.data.recordparser.IOExceptionMessage;
import com.adr.data.recordparser.Loader;
import com.adr.data.recordparser.RecordParsers;
import com.adr.data.recordparser.StreamLoader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestQuery {
    
    private final Header headers;
    private final List<Record> records;
    
    public RequestQuery(Header headers, List<Record> records) {
        this.headers = headers;
        this.records = records;
    }
    
    public String write() throws IOException {
        StringWriter writer = new StringWriter();
        write(writer);
        return writer.toString();
    }
    
    public void write(Writer w) throws IOException {
        RecordsSerializer.write(headers.getRecord(), w);
        w.append('\n');
        RecordsSerializer.writeList(records, w);
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
        List<Record> recordsList = new ArrayList<>();
        for (;;) {
            if (loader.getCP() == '(') {
                recordsList.add(RecordParsers.parseRecord(loader));
                loader.skipBlanks();
            } else if (CodePoint.isEOF(loader.getCP())) {
                break;
            } else {
                throw IOExceptionMessage.createExpected(loader, -1);
            }
        }
        return new RequestQuery(header, recordsList);

    } 
    
    public static String serverQueryProcess(QueryLink link, String message, Logger logger) throws IOException {

        RequestQuery request = RequestQuery.read(message);
        logger.log(Level.CONFIG, "Processing Query: {0}.", new Object[]{message});

        try {
            return new ResponseQueryListRecord(link.process(request.headers, request.records)).write();
        } catch (DataException ex) {
            logger.log(Level.SEVERE, "Cannot execute query request.", ex);
            return new ResponseQueryError(ex).write();
        }
    }    
    
    public static CompletableFuture<String> asyncserverQueryProcess(AsyncQueryLink querylink, String message, Logger logger) {
        return CompletableFuture.completedFuture(null)
                .thenApply(nil -> {
                    try {
                        return RequestQuery.read(message);
                    } catch (IOException ex) {
                        throw new CompletionException(ex);
                    }
                })
                .thenCompose(request -> querylink.process(request.headers, request.records))
                .thenApply(result -> {
                    try {
                        return new ResponseQueryListRecord(result).write();
                    } catch (IOException ex) {
                        throw new CompletionException(ex);
                    }
                })
                .exceptionally(t -> {
                    if (t instanceof DataException) {
                        try {
                            return new ResponseQueryError(t).write();
                        } catch (IOException ex) {
                            throw new CompletionException(ex);
                        }
                    }
                    throw new CompletionException(t);
                });    
    }     
}
