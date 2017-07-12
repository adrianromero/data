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
import com.adr.data.utils.EnvelopeRequest;
import com.adr.data.utils.JSON;
import com.adr.data.utils.ProcessRequest;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.RpcServer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class MQQueryLinkServer extends RpcServer {
    
    private static final Logger LOG = Logger.getLogger(MQQueryLinkServer.class.getName());
    
    private final QueryLink link;

    public MQQueryLinkServer(Channel channel, String queueName, QueryLink link) throws IOException {
        super(channel, queueName);
        this.link = link;
    }
    
    @Override
    public void handleCast(byte[] requestBody) {
        try {
            String message = new String(requestBody, "UTF-8");
            EnvelopeRequest request = JSON.INSTANCE.fromJSONRequest(message);
            LOG.log(Level.SEVERE, "There is no correlation or destination in the request {0} : {1}.", new Object[]{request.getType(), message});
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedOperationException(ex);            
        }            
    }

    @Override
    public byte[] handleCall(byte[] requestBody, AMQP.BasicProperties replyProperties) {        
        try{
            String message = new String(requestBody, "UTF-8");          
            return ProcessRequest.serverQueryProcess(link, message, LOG).getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedOperationException(ex);            
        }
    }
}
