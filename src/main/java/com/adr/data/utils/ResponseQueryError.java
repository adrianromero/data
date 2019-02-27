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
package com.adr.data.utils;

import com.adr.data.DataException;
import com.adr.data.record.Record;
import com.adr.data.recordparser.CodePoint;
import com.adr.data.recordparser.IOExceptionMessage;
import com.adr.data.recordparser.Loader;
import com.adr.data.recordparser.RecordParsers;
import com.adr.data.recordparser.RecordsSerializer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 *
 * @author adrian
 */
public class ResponseQueryError extends ResponseQuery {

    public static final String NAME = "ERROR";

    private final Throwable ex;

    public ResponseQueryError(Throwable ex) {
        this.ex = ex;
    }

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public void write(Writer w) throws IOException {
        w.append(NAME);
        w.append('\n');
        RecordsSerializer.write(new Record(
                Record.entry("EXCEPTION", ex.getClass().getName()),
                Record.entry("MESSAGE", ex.getMessage())), w);
    }

    @Override
    public List<Record> getResult() throws DataException {
        throw ResponseErrors.createDataException(ex);
    }

    public static ResponseQuery readData(Loader loader) throws IOException {
        Record error = RecordParsers.parseRecord(loader);
        loader.skipBlanks();
        if (CodePoint.isEOF(loader.getCP())) {
            String name = error.getString("EXCEPTION");
            String message = error.getString("MESSAGE");
            return new ResponseQueryError(ResponseErrors.createException(name, message));
        } else {
            throw IOExceptionMessage.createExpected(loader, -1);
        }
    }
}
