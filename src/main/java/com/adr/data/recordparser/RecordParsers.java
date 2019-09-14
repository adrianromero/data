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
import com.adr.data.varrw.ISOResults;
import com.adr.data.var.Kind;
import com.adr.data.varrw.Variants;
import java.io.IOException;

public class RecordParsers {

    private static enum States {
        RECORD_START,
        RECORD_KEY,
        RECORD_DOTS,
        RECORD_VALUE,
        RECORD_DOTSKIND,
        RECORD_END
    }

    public static Record parseRecord(Loader loader) throws IOException {
        Record.Builder fields = Record.builder();
        String name = null;
        String isovalue = null;
        Kind kind = null;
        States state;

        if (loader.getCP() == '(') {
            state = States.RECORD_START;
            loader.next();
            loader.skipBlanks();
        } else {
            throw IOExceptionMessage.createExpected(loader, '{');
        }

        for (;;) {
            if (state == States.RECORD_START) {
                if (loader.getCP() == ')') {
                    loader.next();
                    return Record.EMPTY;
                } else {
                    state = States.RECORD_KEY;
                }
            } else if (state == States.RECORD_KEY) {
                if (loader.getCP() == '\"') {
                    name = CommonParsers.parseString(loader);
                    loader.skipBlanks();
                    state = States.RECORD_DOTS;
                } else if (CodePoint.isInitIdentifier(loader.getCP())) {
                    name = CommonParsers.parseIdentifier(loader);
                    loader.skipBlanks();
                    state = States.RECORD_DOTS;
                } else {
                    throw IOExceptionMessage.createExpected(loader, "Name");
                }
            } else if (state == States.RECORD_DOTS) {
                if (loader.getCP() == ':') {
                    loader.next();
                    loader.skipBlanks();
                    state = States.RECORD_VALUE;
                } else {
                    throw IOExceptionMessage.createExpected(loader, ':');
                }
            } else if (state == States.RECORD_VALUE) {
                if (loader.getCP() == '\"') {
                    isovalue = CommonParsers.parseString(loader);
                    kind = Kind.STRING;
                    loader.skipBlanks();
                    state = States.RECORD_DOTSKIND;
                } else if (CodePoint.isInitNumber(loader.getCP())) {
                    CommonParsers.Numeric n = CommonParsers.parseNumber(loader);
                    isovalue = n.getValue();
                    if (n.getType() == CommonParsers.TYPE_INT) {
                        kind = Kind.INT;
                    } else {
                        kind = Kind.DOUBLE;
                    }
                    loader.skipBlanks();
                    state = States.RECORD_DOTSKIND;
                } else if (CodePoint.isAlpha(loader.getCP())) {
                    String v = CommonParsers.parseWord(loader);
                    if ("NULL".equalsIgnoreCase(v)) {
                        isovalue = null;
                        kind = Kind.STRING;
                    } else {
                        isovalue = v;
                        kind = Kind.BOOLEAN;
                    }
                    loader.skipBlanks();
                    state = States.RECORD_DOTSKIND;
                } else if (loader.getCP() == ':') {
                    isovalue = null;
                    kind = Kind.STRING;
                    state = States.RECORD_DOTSKIND;
                } else {
                    throw IOExceptionMessage.createExpected(loader, "Value");
                }
            } else if (state == States.RECORD_DOTSKIND) {
                if (loader.getCP() == ':') {
                    loader.next();
                    loader.skipBlanks();
                    try {
                        kind = Kind.valueOf(CommonParsers.parseWord(loader));
                    } catch (IllegalArgumentException ex) {
                        throw IOExceptionMessage.createExpected(loader, "Kind");
                    }
                    try {
                        fields.entry(name, Variants.read(new ISOResults(isovalue), kind));
                    } catch (DataException ex) {
                        throw IOExceptionMessage.create(loader, "Error parsing ISO value");
                    }
                    loader.skipBlanks();
                    state = States.RECORD_END;
                } else {
                    try {
                        fields.entry(name, Variants.read(new ISOResults(isovalue), kind));
                    } catch (DataException ex) {
                        throw IOExceptionMessage.create(loader, "Error parsing ISO value");
                    }
                    state = States.RECORD_END;
                }
            } else if (state == States.RECORD_END) {
                if (loader.getCP() == ',') {
                    loader.next();
                    loader.skipBlanks();
                    state = States.RECORD_KEY;
                } else if (loader.getCP() == ')') {
                    loader.next();
                    return fields.build();
                } else {
                    throw IOExceptionMessage.createExpected(loader, ',');
                }
            } else {
                throw IOExceptionMessage.create(loader, "Unexpected error");
            }
        }
    }
}
