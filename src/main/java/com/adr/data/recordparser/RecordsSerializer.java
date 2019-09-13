//     Data Access is a Java library to store data
//     Copyright (C) 2017-2019 Adrián Romero Corchado.
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
package com.adr.data.recordparser;

import com.adr.data.DataException;
import com.adr.data.record.Record;
import com.adr.data.varrw.ISOParameters;
import com.adr.data.var.Kind;
import com.adr.data.varrw.Parameters;
import com.adr.data.var.Variant;
import com.adr.data.varrw.Variants;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author adrian
 */
public class RecordsSerializer {

    public static Record read(String value) throws IOException {
        return read(new StringReader(value));
    }
    
    public static Record read(Reader reader) throws IOException {
        Loader loader = new StreamLoader(reader);
        loader.next();
        loader.skipBlanks();
        Record record = RecordParsers.parseRecord(loader);
        loader.skipBlanks();
        if (CodePoint.isEOF(loader.getCP())) {
            return record;
        } else {
            throw IOExceptionMessage.createExpected(loader, -1);
        }    
    }   

    public static List<Record> readList(String value) throws IOException {
        return readList(new StringReader(value));
    }
    
    public static List<Record> readList(Reader reader) throws IOException {
        Loader loader = new StreamLoader(reader);
        List<Record> recordsList = new ArrayList<>();
        loader.next();
        loader.skipBlanks(); 
        for (;;) {
            if (loader.getCP() == '(') {
                recordsList.add(RecordParsers.parseRecord(loader));
                loader.skipBlanks();
            } else if (CodePoint.isEOF(loader.getCP())) {
                return recordsList;    
            } else {
                throw IOExceptionMessage.createExpected(loader, -1);
            }
        }
    }
    
    public static final String writeList(List<Record> l) throws IOException {
        StringWriter writer = new StringWriter();
        writeList(l, writer);
        return writer.toString();        
    }
    
    public static final void writeList(List<Record> l, Writer writer) throws IOException {
        boolean rc = false;
        for (Record r: l) {
            if (rc) {
                writer.append('\n');
            } else {
                rc = true;
            }
            RecordsSerializer.write(r, writer);
        }         
    }
    
    public static final String write(Record r) throws IOException {
        StringWriter writer = new StringWriter();
        write(r, writer);
        return writer.toString();
    }
    
    public static final void write(Record r, Writer writer) throws IOException {
        writer.write('(');
        boolean comma = false;
        for (Map.Entry<String, Variant> entry: r.entrySet()) {
            if (comma) {
                writer.write(", ");
            } else {
                comma = true;
            }
            writer.write(CommonParsers.isIdentifier(entry.getKey()) ? entry.getKey() : CommonParsers.quote(entry.getKey()));
            writer.write(": ");

            // Calculate value
            try {
                if (entry.getValue().isNull()) {
                    writer.write("NULL");
                    if (entry.getValue().getKind() != Kind.STRING) {
                        writer.write(':');
                        writer.write(entry.getValue().getKind().toString());
                    }
                } else {
                    Parameters params = new ISOParameters();
                    Variants.write(params, entry.getValue().getKind(), entry.getValue());
                    writer.write(hasQuotes(entry.getValue().getKind()) ? CommonParsers.quote(params.toString()) : params.toString());
                    if (!isDefaulted(entry.getValue().getKind())) {
                        writer.write(':');
                        writer.write(entry.getValue().getKind().toString());
                    }
                }
            } catch (DataException ex) {
                throw new IOException(ex);
            }
        }
        writer.write(')');
    }
    
    private static boolean hasQuotes(Kind kind) {
        return kind != Kind.BOOLEAN && 
               kind != Kind.DECIMAL && 
               kind != Kind.INT && 
               kind != Kind.LONG &&
               kind != Kind.FLOAT &&
               kind != Kind.DOUBLE;
    }
    private static boolean isDefaulted(Kind kind) {
        return kind == Kind.BOOLEAN || 
               kind == Kind.INT ||
               kind == Kind.DOUBLE ||
               kind == Kind.STRING;
    }     
}
