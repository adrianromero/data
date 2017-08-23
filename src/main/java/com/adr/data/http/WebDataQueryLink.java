//     Data Access is a Java library to store data
//     Copyright (C) 2016-2017 Adrián Romero Corchado.
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
package com.adr.data.http;

import com.adr.data.DataException;
import com.adr.data.DataQueryLink;
import java.util.List;
import okhttp3.OkHttpClient;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class WebDataQueryLink implements DataQueryLink {

    private final WebQueryLink querylink;
    private final WebDataLink datalink;

    public WebDataQueryLink(String baseurl) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        
        querylink = new WebQueryLink(baseurl, "query", client);
        datalink = new WebDataLink(baseurl, "execute", client);
    }

    @Override
    public List<Record> query(Record headers, Record filter) throws DataException {
        return querylink.query(headers, filter);
    }

    @Override
    public void execute(Record headers, List<Record> l) throws DataException {
        datalink.execute(headers, l);
    }
}