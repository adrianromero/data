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

import com.adr.data.AssignableSession;
import com.adr.data.QueryLink;
import com.adr.data.utils.ProcessRequest;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 * @author adrian
 */
public class WebQueryLinkServer implements Route {
    
    private static final String SESSIONNAME = "DATA_SESSION";
    private static final Logger LOG = Logger.getLogger(WebQueryLinkServer.class.getName());
    
    private final QueryLink link;
    private final AssignableSession assignsession;

    public WebQueryLinkServer(QueryLink link, AssignableSession assignsession) {
        this.link = link;
        this.assignsession = assignsession;
    }
    
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String message;
        if ("GET".equals(request.protocol())) {
            message = request.params(":message");
        } else if ("POST".equals(request.protocol())) {
            message = request.body();
        } else {
            throw new java.lang.IllegalArgumentException("Protocol not supported: " + request.protocol());
        }

        // loads the session
        assignsession.setSerializableSession(request.session().attribute(SESSIONNAME));    
        
        // Execute the process
        String result = ProcessRequest.serverQueryProcess(link, message, LOG); 
        
        // saves the session
        request.session().attribute(SESSIONNAME, assignsession.getSerializableSession());
        
        response.type("application/json");
        return result;
    }
}
