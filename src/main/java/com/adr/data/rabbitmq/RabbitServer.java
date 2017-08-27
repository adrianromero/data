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

package com.adr.data.rabbitmq;

import com.adr.data.DataQueryLink;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;


/**
 *
 * @author adrian           
 */
public class RabbitServer {
    
    private final String host;
    private final String queryqueue;
    private final String dataqueue;    
    private final DataQueryLink link;
    
    private Connection connection;
    private RabbitServerQuery serverquery;
    private RabbitServerData serverdata;   
    
    public RabbitServer(String host, String queryqueue, String dataqueue, DataQueryLink link) {
        this.host = host;
        this.queryqueue = queryqueue;
        this.dataqueue = dataqueue;
        this.link = link;
    }    
     
    protected Connection connect(String host) throws IOException, TimeoutException {        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);            
        return factory.newConnection();
    } 
    
    public void start() throws IOException, TimeoutException {
        connection = connect(host);
        
        serverquery = new RabbitServerQuery(connection, queryqueue, link);
        serverquery.start();
        serverdata = new RabbitServerData(connection, dataqueue, link);
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
