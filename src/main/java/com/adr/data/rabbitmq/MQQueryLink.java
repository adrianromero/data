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
import com.adr.data.QueryLink;
import com.adr.data.Record;
import com.adr.data.utils.EnvelopeResponse;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.utils.RequestQuery;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
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
    public List<Record> query(Record filter, Map<String, String> options) throws DataException {
        
        try {
            byte[] request = JSONSerializer.INSTANCE.toJSON(new RequestQuery(filter, options)).getBytes("UTF-8");
            byte[] response = client.primitiveCall(request);
            EnvelopeResponse envelope = JSONSerializer.INSTANCE.fromJSONResponse(new String(response, "UTF-8"));
            return envelope.getAsListRecord();
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
