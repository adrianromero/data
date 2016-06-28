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

import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.adr.data.security.SecureLink;
import com.adr.data.utils.ProcessRequest;
import java.util.Set;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 * @author adrian
 */
public class WebSecureLinkServer implements Route {
    
    private static final String SESSIONNAME = "DATA_SESSION";
    private static final Logger LOG = Logger.getLogger(WebSecureLinkServer.class.getName());
    
    private final QueryLink querylink;
    private final DataLink datalink;
    private final Set<String> anonymousresources;
    private final Set<String> authenticatedresources;

    public WebSecureLinkServer(QueryLink querylink, DataLink datalink, Set<String> anonymousresources, Set<String> authenticatedresources) {
        this.querylink = querylink;
        this.datalink = datalink;
        this.anonymousresources = anonymousresources;
        this.authenticatedresources = authenticatedresources;
    }
    
    @Override
    public Object handle(Request request, Response response) throws Exception {
        
        byte[] session = request.session().attribute(SESSIONNAME);
        SecureLink.UserSession usersession = new SecureLink.UserSession();
        if (session != null) {
            usersession.setData(session);
        }
        
        // loads the session
        SecureLink link = new SecureLink(querylink, datalink, anonymousresources, authenticatedresources, usersession);

        String result;       
        if ("query".equals(request.params(":process"))) {
            result = ProcessRequest.serverQueryProcess(link, request.body(), LOG);     
        } else if ("execute".equals(request.params(":process"))) {
            result = ProcessRequest.serverDataProcess(link, request.body(), LOG);
        } else {
            throw new IllegalArgumentException("Process not supported: " + request.params(":process"));
        }
        
        // saves the session
        request.session().attribute(SESSIONNAME, usersession.getData());

        response.type("application/json; charset=utf-8");
        return result;
    }
}
