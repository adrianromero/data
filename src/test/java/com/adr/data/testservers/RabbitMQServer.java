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

import com.adr.data.DataQueryLink;
import com.adr.data.rabbitmq.MQDataLinkServer;
import com.adr.data.rabbitmq.MQQueryLinkServer;
import com.adr.data.testlinks.DataQueryLinkSQL;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.RpcServer;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author adrian           
 */
public class RabbitMQServer {
    
    private static final Logger LOG = Logger.getLogger(RabbitMQServer.class.getName());
    
    private final String host;
    private final String queryqueue;
    private final String dataqueue;    
    private final String sqlname;
    
    private Connection connection;
    private Channel querychannel;
    private MQQueryLinkServer queryserver;        
    private Channel datachannel;
    private MQDataLinkServer dataserver;         
    
    public RabbitMQServer(String host, String queryqueue, String dataqueue, String sqlname) {
        this.host = host;
        this.queryqueue = queryqueue;
        this.dataqueue = dataqueue;
        this.sqlname = sqlname;
    }    
    
    public void start() {
       try {
            DataQueryLinkSQL dql = new DataQueryLinkSQL(sqlname);
            DataQueryLink link = dql.createDataQueryLink();
            
            connection = connect(host);
            
            // Run the query server
            querychannel = connection.createChannel();
            queryserver = new MQQueryLinkServer(querychannel, queryqueue, link);            
            
            // Run the data server
            datachannel = connection.createChannel();
            dataserver = new MQDataLinkServer(datachannel, dataqueue, link);                 
            
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
            
            // Run the data server
            new Thread(() -> {
                try {
                    LOG.info("MQ Data Server started.");
                    dataserver.mainloop();
                    LOG.info("MQ Data Server stopped.");
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }).start();
            
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop() throws IOException {
        close(queryserver);
        close(dataserver);
        close(querychannel);
        close(datachannel);
        connection.close();
    }

    private static void close(RpcServer resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException ex) {
            LOG.log(Level.WARNING, null, ex);
        }   
    }
    
    private static void close(Channel resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException | TimeoutException ex) {
             LOG.log(Level.WARNING, null, ex);
        }   
    }
    
    public static Connection connect(String host) {        
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);            
            return factory.newConnection();
        } catch (IOException | TimeoutException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }       
}
