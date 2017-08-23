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

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.utils.EnvelopeResponse;
import com.adr.data.utils.JSON;
import com.adr.data.utils.RequestQuery;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class MQQueryLink implements QueryLink {
    
    private final RpcClient client;
    
//        channel = connection.createChannel();
//        client = new RpcClient(channel, exchange, routingKey, timeout);
// DO STUFF
//        client.close();
//        channel.close();
    
    public MQQueryLink(RpcClient client) {
        this.client = client;
    }

    @Override
    public List<Record> query(Record headers, Record filter) throws DataException {
        
        try {
            byte[] request = JSON.INSTANCE.toJSON(new RequestQuery(headers, filter)).getBytes(StandardCharsets.UTF_8);
            byte[] response = client.primitiveCall(request);
            EnvelopeResponse envelope = JSON.INSTANCE.fromJSONResponse(new String(response, StandardCharsets.UTF_8));
            return envelope.getAsListRecord();
        } catch (IOException | ShutdownSignalException | TimeoutException ex) {
            throw new DataException(ex);
        }
    }
}
