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

package com.adr.data.rabbitmq;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.utils.EnvelopeRequest;
import com.adr.data.utils.EnvelopeResponse;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.utils.ProcessRequest;
import com.adr.data.utils.RequestExecute;
import com.adr.data.utils.ResponseError;
import com.adr.data.utils.ResponseSuccess;
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
public class MQDataLinkServer extends RpcServer {

    private static final Logger logger = Logger.getLogger(MQDataLinkServer.class.getName());
    
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
        
        // the result must be just the result: OK or exception
        try {
            String message = new String(requestBody, "UTF-8");
            EnvelopeRequest request = JSONSerializer.INSTANCE.fromJSONRequest(message);

            logger.log(Level.CONFIG, "Processing {0} : {1}.", new Object[]{request.getType(), message});

           EnvelopeResponse response = request.process(new ProcessRequest() {
                @Override public EnvelopeResponse execute(RequestExecute req) {
                    try {
                        link.execute(req.getListRecord());
                        return new ResponseSuccess();
                    } catch (DataException ex) {
                        logger.log(Level.SEVERE, "Cannot execute request.", ex);
                        return new ResponseError(ex);
                    }
                }
                @Override public EnvelopeResponse other(EnvelopeRequest req) {
                    logger.log(Level.SEVERE, "Request type not supported :", new Object[]{req.getType()});
                    return new ResponseError(new UnsupportedOperationException("Request type not supported : " + req.getType()));
                }                  
            });

            return JSONSerializer.INSTANCE.toJSON(response).getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }              
    }    
}
