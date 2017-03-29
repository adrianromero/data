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
package com.adr.data.http;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.record.Record;
import com.adr.data.record.Values;
import com.adr.data.utils.EnvelopeResponse;
import com.adr.data.utils.JSON;
import com.adr.data.utils.RequestExecute;
import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author adrian
 */
public class WebDataLink implements DataLink {

//    private final String USERAGENT = "DataWebClient/1.0";
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final HttpUrl baseurl;
    private final String segment;
    
    public WebDataLink(String baseurl, String segment, OkHttpClient client) {
        this.client = client;
        this.baseurl = HttpUrl.parse(baseurl);
        this.segment = segment;
    }

    public WebDataLink(String baseurl) {
        this(baseurl, null, new OkHttpClient.Builder().build());
    }

    @Override
    public void execute(Values headers, List<Record> l) throws DataException {
        try {
            String message = JSON.INSTANCE.toJSON(new RequestExecute(headers, l));
            
            HttpUrl newurl = segment == null
                    ? baseurl
                    : baseurl.newBuilder()
                            .addPathSegment(segment)
                            .build();
            
            Request request = new Request.Builder()
                .url(newurl)
                // .header("User-Agent", USERAGENT)
                .post(RequestBody.create(MEDIA_TYPE_JSON, message))
                .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new DataException("Unexpected result code: " + response);
            }

            EnvelopeResponse envelope = JSON.INSTANCE.fromJSONResponse(response.body().string());
            envelope.asSuccess();
        } catch (IOException ex) {
            throw new DataException(ex);
        }
    }

    @Override
    public void close() throws DataException {
    }
}
