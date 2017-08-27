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

import com.adr.data.DataLink;
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
public class RabbitServerData {
    
    private static final Logger LOG = Logger.getLogger(RabbitServerData.class.getName());
    
    private final Connection connection;
    private final String dataqueue;    
    private final DataLink link;
    
    private Channel datachannel;
    private MQDataLinkServer dataserver;         
    
    public RabbitServerData(Connection connection, String dataqueue, DataLink link) {
        this.connection = connection;
        this.dataqueue = dataqueue;
        this.link = link;
    }    
    
    public void start() throws IOException {           
        // Run the data server
        datachannel = connection.createChannel();
        dataserver = new MQDataLinkServer(datachannel, dataqueue, link);                 

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
    }
    
    public void stop() throws IOException, TimeoutException {
        dataserver.close();
        dataserver = null;
        datachannel.close();
    }
}
