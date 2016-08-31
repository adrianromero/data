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

package com.adr.data.sqlpg;

import com.adr.data.DataException;
import com.adr.data.Record;
import com.adr.data.sql.Sentence;
import com.adr.data.sql.SentenceDelete;
import java.sql.Connection;

/**
 *
 * @author adrian
 */
public class SentencePGPut extends Sentence {
    
    private final SentenceDelete delete = new SentenceDelete();
    private final SentencePGMerge pgmerge = new SentencePGMerge();

    @Override
    public String getName() {
        return "PostgreSQL-PUT";
    }

    @Override
    public void execute(Connection c, Record keyval) throws DataException {        
        if (keyval.getValue() == null) {
            delete.execute(c, keyval);
        } else {
            pgmerge.execute(c, keyval);
        }
    }
}
