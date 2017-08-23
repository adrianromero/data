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
public class SentenceUpdateInsert extends Sentence {
    
    private final SentenceInsert insert = new SentenceInsert();
    private final SentenceUpdate update = new SentenceUpdate();

    @Override
    public String getName() {
        return "UPDATE-INSERT";
    }

    @Override
    public void execute(Connection c, SQLEngine engine, Record record) throws DataException {
        
        int rows = Sentence.execute(c, update.build(engine, record), record);
        if (rows == 0) {
            if (Sentence.execute(c, insert.build(engine, record), record) != 1) {
                throw new DataException("Sentence \"" + insert.getName() + "\" in \"" + getName() + "\" must return 1 row.");
            }
        } else if (rows != 1) {
            throw new DataException("Sentence \"" + update.getName() + "\" in \"" + getName() + "\" must return 0 or 1 row.");
        }
    }
}
