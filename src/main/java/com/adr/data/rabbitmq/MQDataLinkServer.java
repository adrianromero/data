/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.rabbitmq;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.DataList;
import com.adr.data.utils.JSONSerializer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.RpcServer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

/**
 *
 * @author adrian
 */
public class MQDataLinkServer extends RpcServer {
    
    private final DataLink link;

    public MQDataLinkServer(Channel channel, String queueName, DataLink link) throws IOException {
        super(channel, queueName);
        this.link = link;
    }
    
    @Override
    public void handleCast(AMQP.BasicProperties requestProperties, byte[] requestBody) {
        handleCall(requestProperties, requestBody, null);            
    }

    @Override
    public byte[] handleCall(AMQP.BasicProperties requestProperties, byte[] requestBody, AMQP.BasicProperties replyProperties) {
        
        // the result must be just the result: OK or exception
        // Do not do anything with replyProperties becase can be null....
        try {
            String message = new String(requestBody, "UTF-8");
//            
//            EnvelopeLink envelope = JSONSerializer.INSTANCE.fromJSON(message, EnvelopeLink.class);
//            
//            
//            DataList l = JSONSerializer.INSTANCE.fromJSON(message, DataList.class);
//            try {
//                link.execute(l);
//                return success;
//            } catch (DataException ex) {
//                return error;
//            }  
            return null;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }              
    }    
}
