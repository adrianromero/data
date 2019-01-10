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

import com.adr.data.QueryLink;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian           
 */
public class RabbitQueryServer {
    
    private static final Logger LOG = Logger.getLogger(RabbitQueryServer.class.getName());
    
    private final Connection connection;
    private final String queryqueue;
    private final QueryLink link;
   
    private Channel querychannel;
    private MQQueryServer queryserver;             
    
    public RabbitQueryServer(Connection connection, String queryqueue, QueryLink link) {
        this.connection = connection;
        this.queryqueue = queryqueue;
        this.link = link;
    }    
    
    public void start() throws IOException {         
        // Run the query server
        querychannel = connection.createChannel();
        queryserver = new MQQueryServer(querychannel, queryqueue, link);                    

        // Run the query server
        new Thread(() -> {
            try {
                LOG.info("MQ Query Server started.");
                queryserver.mainloop();     
                LOG.info("MQ Query Server stopped.");
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }).start();                     
    }
    
    public void stop() throws IOException, TimeoutException {
        queryserver.close();
        queryserver = null;
        querychannel.close();
        querychannel = null;
    }   
}
