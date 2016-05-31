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
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class MQDataLink implements DataLink {
    
    private final static Logger logger = Logger.getLogger(MQDataLink.class.getName());
    
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Channel channel;
    private final String exchange;

    public MQDataLink(Connection connection, String exchange) throws IOException {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.channel.exchangeDeclarePassive(exchange);
        this.exchange = exchange;
    }

    public void close() throws IOException, TimeoutException {
        channel.close();        
    }

    @Override
    public void execute(DataList l) throws DataException {
        try {
            executeAsync(l).join();
        } catch (CompletionException ex) {
            throw new DataException(ex.getCause());
        } catch (Exception ex) {
            throw new DataException(ex);
        }
    }
    
    public CompletableFuture<Void> executeAsync(DataList l) {
        final byte[] json;
        try {
            json = JSONSerializer.INSTANCE.toJSON(l).getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

        return CompletableFuture.runAsync(() -> {
            try {
                channel.basicPublish(exchange, "", null, json);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Cannot publish to exchange: " + exchange, ex);
                throw new CompletionException(ex);  
            }
        }, executor);       
    }
}
