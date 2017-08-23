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

package com.adr.data.sql;

import com.adr.data.DataException;
import java.sql.Connection;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public abstract class SentenceDML extends Sentence {

    protected abstract CommandSQL build(SQLEngine engine, Record val);

    @Override
    public final void execute(Connection c, SQLEngine engine, Record val) throws DataException {
        if (Sentence.execute(c, build(engine, val), val) != 1) {
            throw new DataException("Sentence \"" + getName() + "\" must return 1 row.");
        }
    }         
}
