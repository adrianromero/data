//     Data Access is a Java library to store data
//     Copyright (C) 2017-2018 Adrián Romero Corchado.
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
package com.adr.data.cache;

import com.adr.data.DataException;
import com.adr.data.record.Header;
import com.adr.data.utils.ResponseQuery;
import com.adr.data.utils.RequestQuery;
import com.adr.data.utils.ResponseQueryListRecord;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.io.IOException;
import java.util.List;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class CacheProviderMem implements CacheProvider {

    private final Cache<String, String> cache;

    public CacheProviderMem() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build();
    }

    @Override
    public void put(Header headers, Record filter, List<Record> records) throws DataException {
        try {
            cache.put(new RequestQuery(Header.EMPTY, filter).write(),
                    new ResponseQueryListRecord(records).write());
        } catch (IOException e) {
            throw new DataException(e);
        }
    }

    @Override
    public List<Record> getIfPresent(Header headers, Record filter) throws DataException {
        try {
            String cachedresult = cache.getIfPresent(new RequestQuery(Header.EMPTY, filter).write());
            if (cachedresult == null) {
                return null;
            } else {
                return ResponseQuery.read(cachedresult).getResult();
            }
        } catch (IOException e) {
            throw new DataException(e);
        }
    }
}
