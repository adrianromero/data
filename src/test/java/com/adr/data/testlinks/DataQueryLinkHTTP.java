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
package com.adr.data.testlinks;

import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.adr.data.http.WebDataLink;
import com.adr.data.http.WebQueryLink;
import okhttp3.OkHttpClient;

/**
 *
 * @author adrian
 */
public class DataQueryLinkHTTP implements DataQueryLinkBuilder {
    
    private final String urldata;
    private final String urlquery;
    
    private QueryLink querylink;
    private DataLink datalink;
    
    public DataQueryLinkHTTP(String urldata, String urlquery) {
        this.urldata = urldata;
        this.urlquery = urlquery;
    }

    @Override
    public void create() {
        OkHttpClient client = new OkHttpClient.Builder().build();      
        querylink = new WebQueryLink(urlquery, client);
        datalink = new WebDataLink(urldata, client);
    }

    @Override
    public void destroy() {
        querylink = null;
        datalink = null;
    }

    @Override
    public QueryLink getQueryLink() {
        return querylink;
    }

    @Override
    public DataLink getDataLink() {
        return datalink;
    }
}
