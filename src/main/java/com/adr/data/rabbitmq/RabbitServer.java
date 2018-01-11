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

import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 *
 * @author adrian           
 */
public class RabbitServer {
    
    private final String host;
    private final int port;
    private final String queryqueue;
    private final String dataqueue;    
    private final DataLink datalink;
    private final QueryLink querylink;
    
    private Connection connection;
    private RabbitServerQuery serverquery;
    private RabbitServerData serverdata;   
    
    public RabbitServer(String host, int port, String dataqueue, String queryqueue, DataLink datalink, QueryLink querylink) {
        this.host = host;
        this.port = port;
        this.queryqueue = queryqueue;
        this.dataqueue = dataqueue;
        this.datalink = datalink;
        this.querylink = querylink;
    }    
    
    public RabbitServer(String dataqueue, String queryqueue, DataLink datalink, QueryLink querylink) {
        this.host = ConnectionFactory.DEFAULT_HOST; // localhost
        this.port = ConnectionFactory.DEFAULT_AMQP_PORT; // 5672
        this.queryqueue = queryqueue;
        this.dataqueue = dataqueue;
        this.datalink = datalink;
        this.querylink = querylink;
    }    
     
    protected Connection connect() throws IOException, TimeoutException {        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);     
        factory.setPort(port);
        return factory.newConnection();
    } 
    
    public void start() throws IOException, TimeoutException {
        connection = connect();
        
        serverquery = new RabbitServerQuery(connection, queryqueue, querylink);
        serverquery.start();
        serverdata = new RabbitServerData(connection, dataqueue, datalink);
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
