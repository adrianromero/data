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
import com.adr.data.QueryLink;
import com.adr.data.QueryOptions;
import com.adr.data.record.Record;
import com.adr.data.utils.EnvelopeResponse;
import com.adr.data.utils.JSON;
import com.adr.data.utils.RequestQuery;
import com.adr.data.utils.ResponseListRecord;
import java.util.List;

/**
 *
 * @author adrian
 */
public class CachedQueryLink implements QueryLink {

    private final QueryLink link;

    private final CacheProvider provider;
    private final CacheSelector selector;

    public CachedQueryLink(QueryLink link, CacheProvider provider, CacheSelector selector) {
        this.link = link;
        this.provider = provider;
        this.selector = selector;
    }

    public CachedQueryLink(QueryLink link, String... entities) {
        this(link, new CacheProviderMem(), new CacheSelectorList(entities));
    }

    @Override
    public List<Record> query(Record filter, QueryOptions options) throws DataException {
        
        if (selector.cache(filter)) {
            String key = JSON.INSTANCE.toJSON(new RequestQuery(filter, options));
            String cachedresult = provider.getIfPresent(key);
            if (cachedresult == null) {
                List<Record> l = link.query(filter, options);
                provider.put(key, JSON.INSTANCE.toJSON(new ResponseListRecord(l)));
                return l;
            } else {
                EnvelopeResponse response = JSON.INSTANCE.fromJSONResponse(cachedresult);
                return response.getAsListRecord();
            }
        } else {
            return link.query(filter, options);
        }
    }

    @Override
    public void close() throws DataException {
        link.close();
    }
}
