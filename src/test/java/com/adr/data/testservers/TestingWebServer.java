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
package com.adr.data.testservers;

import com.adr.data.DataQueryLink;
import com.adr.data.testlinks.DataQueryLinkBuilder;
import com.adr.data.testlinks.DataQueryLinkSQL;
import com.adr.data.utils.ProcessRequest;
import java.util.logging.Logger;
import spark.Service;

/**
 *
 * @author adrian
 */
public class TestingWebServer {
    
    private static final Logger LOG = Logger.getLogger(TestingWebServer.class.getName());
    
    private Service http;
    private final int port;
    private final String context; 
    private final DataQueryLinkBuilder builder;
    
    public TestingWebServer(int port, String context, String sqlname) {
        this.port = port;
        this.context = context;
        this.builder = new DataQueryLinkSQL(sqlname);
    }
    
    public void start() throws Exception {
          
        DataQueryLink link = builder.create();
        
        http = Service.ignite();
        http.port(port);
        
        // default port 4567
        http.post(context + "/:process", (request, response) -> {
            String process = request.params(":process");
            response.type("application/json; charset=utf-8");
            if ("query".equals(process)) {
                return ProcessRequest.serverQueryProcess(link, request.body(), LOG);
            } else if ("execute".equals(process)) {
                return ProcessRequest.serverDataProcess(link, request.body(), LOG);
            } else {
                throw new IllegalArgumentException("Process not supported: " + process);
            }          
        });
        
        http.awaitInitialization();
        LOG.info("Web Server started.");
    }
    
    public void stop() throws Exception {
        http.stop();
        builder.destroy();
        LOG.info("Web Server stopped.");
    }
}
