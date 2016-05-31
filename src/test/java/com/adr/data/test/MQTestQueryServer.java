/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import com.adr.data.QueryLink;
import com.adr.data.rabbitmq.MQQueryLinkServer;
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
public class MQTestQueryServer {
    
    public MQTestQueryServer() {
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
//    public void run() {
    
    public static void main(String[] args) {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();        
            Channel channel = connection.createChannel();
            
            DataSourceLink.setUpDB();
            QueryLink link = DataSourceLink.getQueryLink();
            
            MQQueryLinkServer server = new MQQueryLinkServer(channel, "mqquerylink", link);
            
            System.out.println("running");
            
            server.mainloop();
            
            
            DataSourceLink.tearDownDB();
            channel.close();
            connection.close();

            
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(MQTestQueryServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) { // SetupDB
            Logger.getLogger(MQTestQueryServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
