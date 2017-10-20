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
import com.adr.data.utils.ProcessRequest;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.RpcServer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class MQDataLinkServer extends RpcServer {

    private static final Logger LOG = Logger.getLogger(MQDataLinkServer.class.getName());
    
    private final DataLink link;

    public MQDataLinkServer(Channel channel, String queueName, DataLink link) throws IOException {
        super(channel, queueName);
        this.link = link;
    }
    
    @Override
    public void handleCast(byte[] requestBody) {      
        // Ignorer the return
        handleCall(requestBody, null);            
    }

    @Override
    public byte[] handleCall(byte[] requestBody, AMQP.BasicProperties replyProperties) {
        try {
            // the result must be just the result: OK or exception
            String message = new String(requestBody, StandardCharsets.UTF_8);
            return ProcessRequest.serverDataProcess(link, message, LOG).getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }    
}
