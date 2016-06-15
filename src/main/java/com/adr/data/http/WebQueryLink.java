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
import com.adr.data.QueryLink;
import com.adr.data.Record;
import com.adr.data.utils.JSONSerializer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;


/**
 *
 * @author adrian
 */
public class WebQueryLink implements QueryLink {
    
    private final String USERAGENT = "AdrDataWebClient/1.0";
    
    private final HttpClient client = HttpClientBuilder.create().build();
    private final String url;
    
    public WebQueryLink(String url) {
        this.url = url;
    }

    @Override
    public Record find(Record filter) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//	HttpPost post = new HttpPost(url);
//
//	// add header
//	post.setHeader("User-Agent", USERAGENT);
//
//        
//        String data = JSONSerializer.INSTANCE.toJSON(filter);
//	
//        StringEntity entity = new StringEntity(data, "UTF-8");
//        entity.setContentType("application/json");
//        post.setEntity(entity);      
//
//	HttpResponse response = client.execute(post);
//        
//         if (response.getStatusLine().getStatusCode() == 200) {
//         
//         }
//
//	System.out.println("Response Code : " 
//                + response.getStatusLine().getStatusCode());
//
//	BufferedReader rd = new BufferedReader(
//	        new InputStreamReader(response.getEntity().getContent()));
//
//	StringBuilder result = new StringBuilder();
//	String line;
//	while ((line = rd.readLine()) != null) {
//		result.append(line);
//	}
    }

    @Override
    public List<Record> query(Record filter) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
