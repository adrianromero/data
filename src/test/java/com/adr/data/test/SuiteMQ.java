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
package com.adr.data.test;

import com.adr.data.rabbitmq.RabbitServer;
import com.adr.data.test.persist.DataTests;
import com.adr.data.testlinks.CommandQueryLinkMQ;
import com.adr.data.testlinks.CommandQueryLinkSQL;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.adr.data.testlinks.CommandQueryLinkBuilder;

/**
 *
 * @author adrian
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    QueryTests.class, 
    DataTests.class, 
    SecurityTests.class
})
public class SuiteMQ {
    
    private static final String HOST = "localhost";
    private static final int PORT = 5672;
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String EXCHANGEQUERY ="exquerylink";
    private static final String EXCHANGEDATA = "exdatalink";
    private static final String QUEUEQUERY ="mqquerylink";
    private static final String QUEUEDATA = "mqdatalink";
    private static final String SQLNAME = "postgresql";
    
    private static final CommandQueryLinkBuilder builder= new CommandQueryLinkSQL(SQLNAME);
    private static RabbitServer myserver;

    @BeforeClass
    public static void setUpClass() throws Exception {
        builder.create();
        myserver = new RabbitServer(HOST, PORT, USERNAME, PASSWORD, QUEUEDATA, QUEUEQUERY, builder.getCommandLink(), builder.getQueryLink());
        myserver.start();
        SourceLink.setBuilder(new CommandQueryLinkMQ(HOST, PORT, USERNAME, PASSWORD, EXCHANGEDATA, EXCHANGEQUERY));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        SourceLink.setBuilder(null);       
        myserver.stop();
        builder.destroy();
    }   
}
