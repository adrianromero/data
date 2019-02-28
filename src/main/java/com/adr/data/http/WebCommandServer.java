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

import com.adr.data.utils.RequestCommand;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import spark.Route;
import com.adr.data.CommandLink;

public class WebCommandServer implements Route {

    private static final Logger LOG = Logger.getLogger(WebCommandServer.class.getName());

    private final CommandLink commandlink;

    public WebCommandServer(CommandLink commandlink) {
        this.commandlink = commandlink;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json; charset=utf-8");
        return RequestCommand.serverCommandProcess(commandlink, request.body(), LOG);
    }
}