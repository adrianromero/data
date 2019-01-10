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

import com.adr.data.QueryLink;
import spark.Service;
import com.adr.data.CommandLink;

/**
 *
 * @author adrian
 */
public class WebServer {

    private Service http;
    private final int port;

    private final String contextcommand;
    private final CommandLink commandlink;
    private final String contextquery;
    private final QueryLink querylink;

    public WebServer(int port, String contextcommand, String contextquery, CommandLink commandlink, QueryLink querylink) {
        this.port = port;
        this.contextcommand = contextcommand;
        this.contextquery = contextquery;
        this.commandlink = commandlink;
        this.querylink = querylink;
    }

    protected Service connect(int port) {
        Service s = Service.ignite();
        s.port(port);
        return s;
    }

    public void start() {
        http = connect(port);
        http.post(contextcommand, new WebCommandServer(commandlink));
        http.post(contextquery, new WebQueryServer(querylink));
        http.awaitInitialization();
    }

    public void stop() {
        http.stop();
        http = null;
    }
}
