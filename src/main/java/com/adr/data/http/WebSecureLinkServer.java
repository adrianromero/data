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
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class WebSecureLinkServer {

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
       
    public String handleQuery(String message, SecureLink.UserSession usersession) throws IOException {
        return ProcessRequest.serverQueryProcess(createSecureLink(usersession), message, LOG);                   
    }
    
    public String handleExecute(String message, SecureLink.UserSession usersession) throws IOException {
        return ProcessRequest.serverDataProcess(createSecureLink(usersession), message, LOG);         
    } 
    
    public String handle(String process, String message, SecureLink.UserSession usersession) throws IOException {
        if ("query".equals(process)) {
            return handleQuery(message, usersession);
        } else if ("execute".equals(process)) {
            return handleExecute(message, usersession);
        } else {
            throw new IllegalArgumentException("Process not supported: " + process);
        }  
    }
    
    private SecureLink createSecureLink(SecureLink.UserSession usersession) {
        return new SecureLink(querylink, datalink, anonymousresources, authenticatedresources, usersession);
    }
}