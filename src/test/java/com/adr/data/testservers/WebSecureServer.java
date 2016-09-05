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
package com.adr.data.testservers;

import com.adr.data.http.WebSecureLinkServer;
import com.adr.data.security.SecureLink;
import com.adr.data.testlinks.DataQueryLinkSQL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;
import spark.Service;

/**
 *
 * @author adrian
 */
public class WebSecureServer {
    
    private static final Logger LOG = Logger.getLogger(WebSecureServer.class.getName());
    private static final String SESSIONNAME ="DataSessionName";
    
    private Service http;
    private final int port;
    private final String context;
    private final String sqlname;
    
    public WebSecureServer(int port, String context, String sqlname) {
        this.port = port;
        this.context = context;
        this.sqlname = sqlname;
    }
    
    public void start() throws Exception {

        WebSecureLinkServer route = createWebSecureLinkServer();
        
        http = Service.ignite();
        http.port(port);
        
        // default port 4567
        http.post(context + "/:process", (request, response) -> {

            // loads the session or create a new one
            byte[] session = request.session().attribute(SESSIONNAME);
            SecureLink.UserSession usersession = new SecureLink.UserSession();
            if (session != null) {
                usersession.setData(session);
            }
         
            String result = route.handle(request.params(":process"), request.body(), usersession);
         
            // store the session
            request.session().attribute(SESSIONNAME, usersession.getData());

            response.type("application/json; charset=utf-8");
            return result;
        });
        
        http.awaitInitialization();
        LOG.info("Web Server started.");
    }
    
    public void stop() throws Exception {
        http.stop();
        LOG.info("Web Server stopped.");
    }
    
    public WebSecureLinkServer createWebSecureLinkServer() {
        
        DataQueryLinkSQL dql = new DataQueryLinkSQL(sqlname);
        
        return new WebSecureLinkServer(
            dql.createQueryLink(),
            dql.createDataLink(),
            new HashSet<>(Arrays.asList("USERNAME_VISIBLE")), // anonymous res
            new HashSet<>(Arrays.asList("authenticatedres"))); // authenticated res
    }    
}
