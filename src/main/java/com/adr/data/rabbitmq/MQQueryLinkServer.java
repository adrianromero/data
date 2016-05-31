/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.rabbitmq;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.utils.EnvelopeRequest;
import com.adr.data.utils.EnvelopeResponse;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.utils.ProcessRequest;
import com.adr.data.utils.RequestFind;
import com.adr.data.utils.RequestQuery;
import com.adr.data.utils.ResponseDataList;
import com.adr.data.utils.ResponseError;
import com.adr.data.utils.ResponseRecord;
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
    
    private final QueryLink link;

    public MQQueryLinkServer(Channel channel, String queueName, QueryLink link) throws IOException {
        super(channel, queueName);
        this.link = link;
    }
    
    @Override
    public void handleCast(byte[] requestBody) {
        try {
            String message = new String(requestBody, "UTF-8");
            EnvelopeRequest request = JSONSerializer.INSTANCE.fromJSONEnvelope(message);
            Logger.getLogger(MQQueryLinkServer.class.getName()).log(Level.SEVERE, "There is no correlation or destination in the request {0} : {1}.", new Object[]{request.getType(), message});
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedOperationException(ex);            
        }            
    }

    @Override
    public byte[] handleCall(byte[] requestBody, AMQP.BasicProperties replyProperties) {        
        try{
            String message = new String(requestBody, "UTF-8");
            EnvelopeRequest request = JSONSerializer.INSTANCE.fromJSONEnvelope(message);

            Logger.getLogger(MQQueryLinkServer.class.getName()).log(Level.INFO, "Processing {0} : {1}.", new Object[]{request.getType(), message});

            EnvelopeResponse response = request.process(new ProcessRequest() {
                @Override public EnvelopeResponse find(RequestFind req) {
                    try {
                        return new ResponseRecord(link.find(req.getFilter()));
                    } catch (DataException ex) {
                        Logger.getLogger(MQQueryLinkServer.class.getName()).log(Level.SEVERE, null, ex);
                        return new ResponseError(ex);
                    }
                }
                @Override public EnvelopeResponse query(RequestQuery req) {
                    try {
                        return new ResponseDataList(link.query(req.getFilter()));
                    } catch (DataException ex) {
                        Logger.getLogger(MQQueryLinkServer.class.getName()).log(Level.SEVERE, null, ex);
                        return new ResponseError(ex);
                    }
                }                   
                @Override public EnvelopeResponse other(EnvelopeRequest req) {
                    Logger.getLogger(MQQueryLinkServer.class.getName()).log(Level.SEVERE, "Request type not supported :", new Object[]{req.getType()});
                    return new ResponseError(new UnsupportedOperationException("Request type not supported : " + req.getType()));
                }                     
            });

            return JSONSerializer.INSTANCE.toJSON(response).getBytes("UTF-8");

        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedOperationException(ex);            
        }
    }
}
