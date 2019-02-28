//     Data Access is a Java library to store data
//     Copyright (C) 2016-2019 Adrián Romero Corchado.
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

import com.adr.data.rabbitmq.MQLink;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.RpcClient;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.adr.data.Link;
import com.rabbitmq.client.RpcClientParams;

/**
 *
 * @author adrian
 */
public class CommandQueryLinkMQ implements CommandQueryLinkBuilder {
    
    private static final Logger LOG = Logger.getLogger(CommandQueryLinkMQ.class.getName());  

    private final String queryexchange;
    private final String commandexchange;
    private final Connection connection;

    private Channel querychannel = null;
    private RpcClient queryclient = null;
    private Link querylink;
    
    private Channel commandchannel = null;
    private RpcClient commandclient = null;
    private Link commandlink;
    
    public CommandQueryLinkMQ(String host, int port, String username, String password, String commandexchange, String queryexchange) {

        this.commandexchange = commandexchange;
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
            querychannel = connection.createChannel();
            RpcClientParams queryparams = new RpcClientParams()
                .channel(querychannel)
                .exchange(queryexchange)
                .routingKey("")
                .replyTo("amq.rabbitmq.reply-to")
                .timeout(2500)
                .useMandatory(false);            
            queryclient = new RpcClient(queryparams);
            querylink = new MQLink(queryclient);
            
            commandchannel = connection.createChannel();
            RpcClientParams commandparams = new RpcClientParams()
                .channel(commandchannel)
                .exchange(commandexchange)
                .routingKey("")
                .replyTo("amq.rabbitmq.reply-to")
                .timeout(2500)
                .useMandatory(false);            
            commandclient = new RpcClient(commandparams);            
            commandlink = new MQLink(commandclient);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public void destroy() {
        try {
            commandclient.close();
            commandchannel.close();
            commandlink = null;
            queryclient.close();
            querychannel.close();
            querylink = null;
        } catch (IOException | TimeoutException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }    

    @Override
    public Link getQueryLink() {
        return querylink;
    }

    @Override
    public Link getCommandLink() {
        return commandlink;
    }
}
