//     Data Access is a Java library to store data
//     Copyright (C) 2016-2018 Adrián Romero Corchado.
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
import com.adr.data.record.Header;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;
import com.adr.data.record.Record;
import com.adr.data.utils.RequestLink;
import com.adr.data.utils.ResponseLink;
import com.adr.data.Link;

/**
 *
 * @author adrian
 */
public class MQLink implements Link {
    
    private final RpcClient client;
    
//        channel = connection.createChannel();
//        client = new RpcClient(channel, exchange, routingKey, timeout);
// DO STUFF
//        client.close();
//        channel.close();
    
    public MQLink(RpcClient client) {
        this.client = client;
    }

    @Override
    public List<Record> process(Header headers, List<Record> l) throws DataException {
        
        try {
            byte[] request = new RequestLink(headers, l).write().getBytes(StandardCharsets.UTF_8);
            byte[] response = client.primitiveCall(request);            
            ResponseLink envelope = ResponseLink.read(new String(response, StandardCharsets.UTF_8));            
            return envelope.getResult();
        } catch (IOException | ShutdownSignalException | TimeoutException ex) {
            throw new DataException(ex);
        }
    }  
}
