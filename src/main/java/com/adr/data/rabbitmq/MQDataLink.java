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
import com.adr.data.record.Record;
import com.adr.data.utils.JSON;
import com.adr.data.utils.RequestExecute;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author adrian
 */
public class MQDataLink implements DataLink {
    
    private Channel channel = null;
    private RpcClient client = null;
    
    public MQDataLink(Connection connection, String exchange, String routingKey, int timeout) throws IOException {
        channel = connection.createChannel();
        client = new RpcClient(channel, exchange, routingKey, timeout);
    }
    public MQDataLink(Connection connection, String exchange, int timeout) throws IOException {
        this(connection, exchange, "", timeout);
    }
    public MQDataLink(Connection connection, String exchange, String routingKey) throws IOException {
        this(connection, exchange, routingKey, 2500);
    }
    public MQDataLink(Connection connection, String exchange) throws IOException {
        this(connection, exchange, "", 2500);
    }

    @Override
    public void execute(List<Record> l) throws DataException {
        
        try {
            byte[] request = JSON.INSTANCE.toJSON(new RequestExecute(l)).getBytes("UTF-8");
            client.publish(null, request);
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedOperationException(ex); // Never happens
        } catch (IOException | ShutdownSignalException ex) {
            throw new DataException(ex);
        }
    }
    
    @Override
    public void close() throws DataException {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException ex) {
            throw new DataException(ex);
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException ex) {
                    throw new DataException(ex);
                }
            }
        }
    }
}
