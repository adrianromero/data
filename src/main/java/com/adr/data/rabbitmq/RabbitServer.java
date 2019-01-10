//     Data Access is a Java library to store data
//     Copyright (C) 2016-2018 Adrián Romero Corchado.
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
package com.adr.data.rabbitmq;

import com.adr.data.QueryLink;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.adr.data.CommandLink;

/**
 *
 * @author adrian
 */
public class RabbitServer {

    private final ConnectionFactory factory;
    private final String queryqueue;
    private final String dataqueue;
    private final CommandLink commandlink;
    private final QueryLink querylink;

    private Connection connection;
    private RabbitQueryServer serverquery;
    private RabbitCommandServer serverdata;

    public RabbitServer(ConnectionFactory factory, String dataqueue, String queryqueue, CommandLink commandlink, QueryLink querylink) {
        this.factory = factory;
        this.queryqueue = queryqueue;
        this.dataqueue = dataqueue;
        this.commandlink = commandlink;
        this.querylink = querylink;
    }

    public RabbitServer(String host, int port, String username, String password, String dataqueue, String queryqueue, CommandLink commandlink, QueryLink querylink) {
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        this.queryqueue = queryqueue;
        this.dataqueue = dataqueue;
        this.commandlink = commandlink;
        this.querylink = querylink;
    }

    public RabbitServer(String host, int port, String dataqueue, String queryqueue, CommandLink commandlink, QueryLink querylink) {
        this(host, port, ConnectionFactory.DEFAULT_USER, ConnectionFactory.DEFAULT_PASS, dataqueue, queryqueue, commandlink, querylink);
    }

    public RabbitServer(String dataqueue, String queryqueue, CommandLink commandlink, QueryLink querylink) {
        this(ConnectionFactory.DEFAULT_HOST, ConnectionFactory.DEFAULT_AMQP_PORT, ConnectionFactory.DEFAULT_USER, ConnectionFactory.DEFAULT_PASS, dataqueue, queryqueue, commandlink, querylink);
    }

    public void start() throws IOException, TimeoutException {
        connection = factory.newConnection();
        serverquery = new RabbitQueryServer(connection, queryqueue, querylink);
        serverquery.start();
        serverdata = new RabbitCommandServer(connection, dataqueue, commandlink);
        serverdata.start();
    }

    public void stop() throws IOException, TimeoutException {
        serverdata.stop();
        serverdata = null;
        serverquery.stop();
        serverquery = null;
        connection.close();
    }
}
