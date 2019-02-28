//     Data Access is a Java library to store data
//     Copyright (C) 2016-2019 Adrián Romero Corchado.
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
import com.adr.data.utils.PredicateSelectorList;
import java.util.List;
import java.util.function.Predicate;
import com.adr.data.record.Record;
import com.adr.data.Link;

/**
 *
 * @author adrian
 */
public class CachedQueryLink implements Link {

    private final Link querylink;

    private final CacheProvider provider;
    private final Predicate<? super List<Record>>  selector;

    public CachedQueryLink(Link querylink, CacheProvider provider, Predicate<? super List<Record>>  selector) {
        this.querylink = querylink;
        this.provider = provider;
        this.selector = selector;
    }

    public CachedQueryLink(Link link, CacheProvider provider) {
        this.querylink = link;
        this.provider = provider;
        this.selector = r -> true;
    }

    public CachedQueryLink(Link link, String... entities) {
        this(link, new CacheProviderMem(), new PredicateSelectorList(entities));
    }

    @Override
    public List<Record> process(Header headers, List<Record> records) throws DataException {
        
        if (selector.test(records)) {
            List<Record> cachedresult = provider.getIfPresent(headers, records);
            if (cachedresult == null) {
                cachedresult = querylink.process(headers, records);
                provider.put(headers, records, cachedresult);                
            }
            return cachedresult;
        } else {
            return querylink.process(headers, records);
        }
    }
}
