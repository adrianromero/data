/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
                        link.execute(req.getDataList());
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
