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
import com.adr.data.record.Values;
import com.adr.data.utils.PredicateSelectorList;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author adrian
 */
public class CachedQueryLink implements QueryLink {

    private final QueryLink link;

    private final CacheProvider provider;
    private final Predicate<? super Record>  selector;

    public CachedQueryLink(QueryLink link, CacheProvider provider, Predicate<? super Record>  selector) {
        this.link = link;
        this.provider = provider;
        this.selector = selector;
    }

    public CachedQueryLink(QueryLink link, CacheProvider provider) {
        this.link = link;
        this.provider = provider;
        this.selector = r -> true;
    }

    public CachedQueryLink(QueryLink link, String... entities) {
        this(link, new CacheProviderMem(), new PredicateSelectorList(entities));
    }

    @Override
    public List<Record> query(Values headers, QueryOptions options, Record filter) throws DataException {
        
        if (selector.test(filter)) {
            List<Record> cachedresult = provider.getIfPresent(headers, options, filter);
            if (cachedresult == null) {
                cachedresult = link.query(headers, options, filter);
                provider.put(headers, options, filter, cachedresult);                
            }
            return cachedresult;
        } else {
            return link.query(headers, options, filter);
        }
    }

    @Override
    public void close() throws DataException {
        link.close();
    }
}
