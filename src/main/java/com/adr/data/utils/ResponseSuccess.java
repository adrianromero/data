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
import com.adr.data.recordparser.RecordsSerializer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import com.adr.data.record.Record;
import com.adr.data.recordparser.CodePoint;
import com.adr.data.recordparser.IOExceptionMessage;
import com.adr.data.recordparser.Loader;
import com.adr.data.recordparser.RecordParsers;
import java.util.ArrayList;

/**
 *
 * @author adrian
 */
public class ResponseSuccess extends ResponseLink {

    public static final String NAME = "LISTRECORD";

    private final List<Record> result;

    public ResponseSuccess(List<Record> result) {
        this.result = result;
    }

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public void write(Writer w) throws IOException {
        w.append(NAME);
        w.append('\n');
        RecordsSerializer.writeList(result, w);
    }

    @Override
    public List<Record> getResult() throws DataException {
        return result;
    }
    
    public static ResponseLink readData(Loader loader) throws IOException {
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
        return new ResponseSuccess(recordsList);        
    }
    
}
