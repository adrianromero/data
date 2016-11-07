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
import com.adr.data.record.Record;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author adrian
 */
public class RouteSelectorList implements RouteSelector {
    
    private final RouteEntry[] entries;
    
    private final Map<String, QueryLink> links;
    private final QueryLink defaultlink;

    public RouteSelectorList(RouteEntry ... entries) {
        
        this.entries = entries;
        
        links = new HashMap<>();
        for (RouteEntry e : entries) {
            for (String s: e.getKeys()){ 
                links.put(s, e.getLink());
            }
        }        
        defaultlink = links.get("_DEFAULT");
    }

    @Override
    public QueryLink getQueryLink(Record filter) throws DataException {
        String entity = filter.getKey().get("_ENTITY").asString();
        QueryLink link = links.get(entity);
        if (link == null) {
            if (defaultlink == null) {
                throw new DataException("No filter link found for entity: " + entity);
            } else {
                return defaultlink;
            }
        } else {
            return link;
        }
    }

    @Override
    public void close() throws DataException {
        for(RouteEntry entry: entries) {
            entry.getLink().close();
        }
    }  
}
