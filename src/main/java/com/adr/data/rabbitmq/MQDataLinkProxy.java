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
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
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
public class MQDataLinkProxy {

    private final static Logger logger = Logger.getLogger(MQDataLinkProxy.class.getName());

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Connection connection;
    private final String queue;
    private final DataLink link;

    private Channel channel = null;

    public MQDataLinkProxy(Connection connection, String queue, DataLink link) {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection connection = factory.newConnection();
        this.connection = connection;
        this.queue = queue;
        this.link = link;
    }

    public CompletableFuture start() {
        return stop().thenRun(() -> {
            try {
                channel = connection.createChannel();
                channel.queueDeclarePassive(queue);
                channel.basicQos(1);
                QueueingConsumer consumer = new QueueingConsumer(channel);                        
                channel.basicConsume(queue, false, consumer);
                
                doLoop(consumer);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                throw new CompletionException(ex);  
            }
        });
    }

    public CompletableFuture stop() {
        return CompletableFuture.runAsync(() -> {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException ex) {
                    logger.log(Level.SEVERE, "Cannot close the channel.", ex);
                }
                channel = null;
            }
        }, executor);
    }
    
    private CompletableFuture doLoop(final QueueingConsumer consumer) {
        return CompletableFuture.runAsync(() -> {
//            boolean doloop = true;
//            while (doloop) {
//                try {
//
//                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//                    String message;
//                    try {
//                        message = new String(delivery.getBody(), "UTF-8");
//                    } catch (UnsupportedEncodingException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                    DataList l = JSONSerializer.INSTANCE.fromJSON(message, DataList.class);
//                    try {
//                        link.execute(l);
//                    } catch (DataException ex) {
//                        logger.log(Level.SEVERE, "Cannot save process.", ex);
//                    }
//
//                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//                } catch (InterruptedException | ShutdownSignalException | ConsumerCancelledException ex) {
//                    // Failed next Delivery
//                    Logger.getLogger(MQDataLinkProxy.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    // Failed ACK
//                    logger.log(Level.SEVERE, null, ex);
//                    doloop = false;
//                }
//            }
        }, executor);      
    }
}
