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
package com.adr.data.cache;

import com.adr.data.DataException;
import com.adr.data.QueryOptions;
import com.adr.data.record.Record;
import com.adr.data.utils.JSON;
import com.adr.data.utils.RequestQuery;
import com.adr.data.utils.ResponseListRecord;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.List;

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
    public void put(Record filter, QueryOptions options, List<Record> value) {
        cache.put(              
                JSON.INSTANCE.toJSON(new RequestQuery(filter, options)), 
                JSON.INSTANCE.toJSON(new ResponseListRecord(value)));
    }

    @Override
    public List<Record> getIfPresent(Record filter, QueryOptions options) throws DataException {
        String cachedresult = cache.getIfPresent(JSON.INSTANCE.toJSON(new RequestQuery(filter, options)));
        if (cachedresult == null) {
            return null;
        } else {
            return JSON.INSTANCE.fromJSONResponse(cachedresult).getAsListRecord();
        }
    } 
}
