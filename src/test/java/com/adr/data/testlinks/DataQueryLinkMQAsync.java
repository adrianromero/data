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
package com.adr.data.testlinks;

import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.adr.data.rabbitmq.MQDataLinkAsync;
import com.adr.data.rabbitmq.MQQueryLink;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.RpcClient;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class DataQueryLinkMQAsync implements DataQueryLinkBuilder {

    private static final Logger LOG = Logger.getLogger(DataQueryLinkMQAsync.class.getName());

    private final String queryexchange;
    private final String dataexchange;
    private final Connection connection;

    private Channel channelquery = null;
    private RpcClient clientquery = null;
    private QueryLink querylink;
    private Channel channeldata = null;
    private RpcClient clientdata = null;
    private DataLink datalink;

    public DataQueryLinkMQAsync(String host, int port, String username, String password, String dataexchange, String queryexchange) {
        
        this.dataexchange = dataexchange;
        this.queryexchange = queryexchange;

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);            
            connection = factory.newConnection();
        } catch (IOException | TimeoutException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void create() {
        try {
            channelquery = connection.createChannel();
            clientquery = new RpcClient(channelquery, queryexchange, "", 2500);
            querylink = new MQQueryLink(clientquery);
            channeldata = connection.createChannel();
            clientdata = new RpcClient(channeldata, dataexchange, "", 2500);
            datalink = new MQDataLinkAsync(clientdata);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void destroy() {
        try {
            clientdata.close();
            channeldata.close();
            datalink = null;
            clientquery.close();
            channelquery.close();
            querylink = null;
        } catch (IOException | TimeoutException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public QueryLink getQueryLink() {
        return querylink;
    }

    @Override
    public DataLink getDataLink() {
        return datalink;
    }    
}
