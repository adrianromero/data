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
package com.adr.data.route;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.QueryOptions;
import com.adr.data.record.Record;
import java.util.List;

/**
 *
 * @author adrian
 */
public class FailOverQueryLink implements QueryLink {
    
    private final QueryLink[] querylinks;
    
    public FailOverQueryLink(QueryLink... querylinks) {
        this.querylinks = querylinks;
    }

    @Override
    public List<Record> query(Record filter, QueryOptions options) throws DataException {
        for(QueryLink q : querylinks) {
            try {
                return q.query(filter, options);
            } catch (DataException e) {
                // Ignore and go to next
            }
        }
        throw new DataException("Failed all QueryLinks");
    }

    @Override
    public void close() throws DataException {
        DataException t = null;
        for(QueryLink q : querylinks) {
            try {
                q.close();
            } catch (DataException e) {
                if (t == null) {
                    t = new DataException("Exception closing Query Links.");
                }
                t.addSuppressed(e);
            }
        }  
        if (t != null) {
            throw t;
        }
    }
}