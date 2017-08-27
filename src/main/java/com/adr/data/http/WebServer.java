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

import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import spark.Service;

/**
 *
 * @author adrian
 */
public class WebServer {
   
    private Service http;
    private final int port;
   
    private final String contextdata; 
    private final DataLink datalink;
    private final String contextquery; 
    private final QueryLink querylink;
    
    private WebServerData serverdata;
    private WebServerQuery serverquery;
    
    public WebServer(int port, String contextdata, String contextquery, DataLink datalink, QueryLink querylink) {
        this.port = port;
        this.contextdata = contextdata;
        this.contextquery = contextquery;
        this.datalink = datalink;
        this.querylink = querylink;
    }
    
    protected Service connect(int port) {
        Service s = Service.ignite();
        s.port(port);
        return s;
    }
    
    public void start() {
       
        http = connect(port);
        
        serverdata = new WebServerData(http, contextdata, datalink);
        serverdata.start();
        serverquery = new WebServerQuery(http, contextquery, querylink);
        serverquery.start();
        
        http.awaitInitialization();
    }
    
    public void stop() {
        
        serverdata.stop();
        serverdata = null;
        serverquery.stop();
        serverquery = null;
        
        http.stop();        
    }
}