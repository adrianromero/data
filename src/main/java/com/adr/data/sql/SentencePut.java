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

package com.adr.data.sql;

import com.adr.data.DataException;
import java.sql.Connection;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 *
 * @author adrian
 */
public class SentencePut extends Sentence {
    
    private final SentenceDelete delete = new SentenceDelete();
    private final SentenceUpdateInsert updateinsert = new SentenceUpdateInsert();

    @Override
    public String getName() {
        return "PUT";
    }

    @Override
    public void execute(Connection c, SQLEngine engine, Record val) throws DataException {
        if (Records.isDeleteSentence(val)) {
            delete.execute(c, engine, val);
        } else {
            updateinsert.execute(c, engine, val);
        }
    }
}
