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
package com.adr.data.http;

import com.adr.data.DataException;
import com.adr.data.record.Header;
import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.adr.data.record.Record;
import com.adr.data.utils.RequestLink;
import com.adr.data.utils.ResponseLink;
import com.adr.data.Link;

/**
 *
 * @author adrian
 */
public class WebLink implements Link {

//    private final String USERAGENT = "DataWebClient/1.0";
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final HttpUrl url;
    
    public WebLink(String url, OkHttpClient client) {
        this.client = client;
        this.url = HttpUrl.parse(url);
    }

    public WebLink(String url) {
        this(url, new OkHttpClient.Builder().build());
    }

    @Override
    public List<Record> process(Header headers, List<Record> l) throws DataException {
        try {
            String message = new RequestLink(headers, l).write();
            
            Request request = new Request.Builder()
                .url(url)
                // .header("User-Agent", USERAGENT)
                .post(RequestBody.create(message,MEDIA_TYPE_JSON))
                .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new DataException("Unexpected result code: " + response.code());
            }

            ResponseLink envelope = ResponseLink.read(response.body().string());
            return envelope.getResult();
        } catch (IOException ex) {
            throw new DataException(ex);
        }
    }
}
