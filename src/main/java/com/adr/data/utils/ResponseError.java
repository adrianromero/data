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
import com.adr.data.record.Entry;
import com.adr.data.record.RecordMap;
import com.adr.data.recordparser.RecordsSerializer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class ResponseError extends EnvelopeResponse {
     
    public static final String NAME = "ERROR";
    
    private final Throwable ex;
    
    public ResponseError(Throwable ex) {
        this.ex = ex;
    }
    
    public Throwable getException() {
        return ex;
    }

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public void write(Writer w) throws IOException {
        w.append(NAME);
        w.append('\n');
        RecordsSerializer.write(new RecordMap(
                new Entry("EXCEPTION", ex.getClass().getName()),
                new Entry("MESSAGE", ex.getMessage())), w);
    }

    @Override
    public List<Record> getAsListRecord() throws DataException {
        if (ex instanceof DataException) {
            throw (DataException) ex;
        } else if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        } else {
            throw new DataException(ex.toString());
        } 
    }
}
