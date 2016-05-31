/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import com.adr.data.DataException;
import com.adr.data.RecordMap;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import com.adr.data.rabbitmq.MQQueryLink;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adrian
 */
public class MQTestQueryLink {
    
    public MQTestQueryLink() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

//    @Test
//    public void simpleQuery() {
//    @Test
//    public void run() {
    
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            MQQueryLink link = new MQQueryLink(channel, "exquerylink");

            System.out.println(JSONSerializer.INSTANCE.toSimpleJSON(
                    link.query(new RecordMap(
                        new ValuesMap(
                            new ValuesEntry("_ENTITY", "subject"),
                            new ValuesEntry("id")),
                        new ValuesMap(
                            new ValuesEntry("name::LIKE", "%o%"),
                            new ValuesEntry("name"))))));              
            // [{"_ENTITY":"subject","id":"role","name":"Role"},{"_ENTITY":"subject","id":"permission","name":"Permission"}]
            
            channel.close();    
            connection.close();
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(MQTestQueryLink.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(MQTestQueryLink.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
}
