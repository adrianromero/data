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
import com.adr.data.utils.ProcessRequest;
import java.util.logging.Logger;
import spark.Service;

/**
 *
 * @author adrian
 */
public class WebServerData {

    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

    private final Service http;
    private final String context;
    private final DataLink link;

    public WebServerData(Service http, String context, DataLink link) {
        this.http = http;
        this.context = context;
        this.link = link;
    }

    public void start() {
        http.post(context, (request, response) -> {
            response.type("application/json; charset=utf-8");
            return ProcessRequest.serverDataProcess(link, request.body(), LOG);
        });
        LOG.info("Web Data Server started.");
    }

    public void stop() {
        LOG.info("Web Data Server stopped.");
    }
}
