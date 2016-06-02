/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.rabbitmq;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.DataList;
import com.adr.data.utils.EnvelopeResponse;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.utils.RequestExecute;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author adrian
 */
public class MQDataLinkSync implements DataLink {
    
    private final RpcClient client;
    
    public MQDataLinkSync(Channel channel, String exchange, String routingKey, int timeout) throws IOException {
        client = new RpcClient(channel, exchange, routingKey, timeout);
    }
    public MQDataLinkSync(Channel channel, String exchange, int timeout) throws IOException {
        this(channel, exchange, "", timeout);
    }
    public MQDataLinkSync(Channel channel, String exchange, String routingKey) throws IOException {
        this(channel, exchange, routingKey, 2500);
    }
    public MQDataLinkSync(Channel channel, String exchange) throws IOException {
        this(channel, exchange, "", 2500);
    }

    @Override
    public void execute(DataList l) throws DataException {
        
        try {
            byte[] request = JSONSerializer.INSTANCE.toJSON(new RequestExecute(l)).getBytes("UTF-8");
            byte[] response = client.primitiveCall(request);
            EnvelopeResponse envelope = JSONSerializer.INSTANCE.fromJSONResponse(new String(response, "UTF-8"));
            envelope.asSuccess();
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedOperationException(ex); // Never happens
        } catch (IOException | ShutdownSignalException | TimeoutException ex) {
            throw new DataException(ex);
        }
    }
    
    public void close() throws IOException {
        client.close();
    }    
}
