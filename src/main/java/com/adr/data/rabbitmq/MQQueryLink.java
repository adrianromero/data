/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.rabbitmq;

import com.adr.data.DataException;
import com.adr.data.DataList;
import com.adr.data.QueryLink;
import com.adr.data.Record;
import com.adr.data.utils.EnvelopeResponse;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.utils.RequestFind;
import com.adr.data.utils.RequestQuery;
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
public class MQQueryLink implements QueryLink {
    
    private final RpcClient client;
    
    public MQQueryLink(Channel channel, String exchange, String routingKey, int timeout) throws IOException {
        client = new RpcClient(channel, exchange, routingKey, timeout);
    }
    public MQQueryLink(Channel channel, String exchange, int timeout) throws IOException {
        this(channel, exchange, "", timeout);
    }
    public MQQueryLink(Channel channel, String exchange, String routingKey) throws IOException {
        this(channel, exchange, routingKey, 2500);
    }
    public MQQueryLink(Channel channel, String exchange) throws IOException {
        this(channel, exchange, "", 2500);
    }

    @Override
    public Record find(Record filter) throws DataException {
        
        try {
            byte[] request = JSONSerializer.INSTANCE.toJSON(new RequestFind(filter)).getBytes("UTF-8");
            byte[] response = client.primitiveCall(request);
            EnvelopeResponse envelope = JSONSerializer.INSTANCE.fromJSONResponse(new String(response, "UTF-8"));
            return envelope.getAsRecord();
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedOperationException(ex); // Never happens
        } catch (IOException | ShutdownSignalException | TimeoutException ex) {
            throw new DataException(ex);
        }
    }

    @Override
    public DataList query(Record filter) throws DataException {
        
        try {
            byte[] request = JSONSerializer.INSTANCE.toJSON(new RequestQuery(filter)).getBytes("UTF-8");
            byte[] response = client.primitiveCall(request);
            EnvelopeResponse envelope = JSONSerializer.INSTANCE.fromJSONResponse(new String(response, "UTF-8"));
            return envelope.getAsDataList();
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedOperationException(ex); // Never happens
        } catch (IOException | ShutdownSignalException | TimeoutException ex) {
            throw new DataException(ex);
        }
    }
}
