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

package com.adr.data.testlinks;

import com.adr.data.BasicDataQueryLink;
import com.adr.data.DataQueryLink;
import com.adr.data.http.WebDataQueryLink;
import com.adr.data.rabbitmq.MQDataLinkSync;
import com.adr.data.rabbitmq.MQQueryLink;
import com.adr.data.security.SecureLink;
import com.adr.data.sql.SQLDataLink;
import com.adr.data.sql.SQLQueryLink;
import com.adr.data.security.SecureCommands;
import com.adr.data.sql.SentencePut;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Eva
 */
public class SourceLink_OLD {
     
    private static ComboPooledDataSource cpds = null;
    private static Connection connection = null;
       
    public static DataSource getDataSource() {
        if (cpds == null) {
            try {
                cpds = new ComboPooledDataSource();
                cpds.setDriverClass(System.getProperty("database.driver"));
                cpds.setJdbcUrl(System.getProperty("database.url"));
                cpds.setUser(System.getProperty("database.user"));  
                cpds.setPassword(System.getProperty("database.password"));
            } catch (PropertyVetoException ex) {
                Logger.getLogger(SourceLink_OLD.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        return cpds; 
    }
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                String host = System.getProperty("rabbitmq.host");
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(host);            
                connection = factory.newConnection();
            } catch (IOException | TimeoutException ex) {
                Logger.getLogger(SourceLink_OLD.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        return connection;
    }
     
    public static DataQueryLink createDataQueryLink() {
        if ("rabbitmq".equals(System.getProperty("link.type"))) {
            return createMQLink();
        } else if ("http".equals(System.getProperty("link.type"))) {
            return createHttpLink();
        } else {        
            return createLocalSecureLink();
        }
    }
    
    private static DataQueryLink createLocalSecureLink() {
        return new SecureLink(
            new SQLQueryLink(getDataSource(), SecureCommands.QUERIES),
            new SQLDataLink(getDataSource(), SecureCommands.COMMANDS),
            new HashSet<>(Arrays.asList("USERNAME_VISIBLE")), // anonymous res
            new HashSet<>(Arrays.asList("authenticatedres"))); // authenticated res
    }

    private static DataQueryLink createMQLink() {
        try {
            return new BasicDataQueryLink(
                new MQQueryLink(getConnection(), System.getProperty("rabbitmq.queryexchange")),
                new MQDataLinkSync(getConnection(), System.getProperty("rabbitmq.dataexchange")));
        } catch (IOException ex) {
            Logger.getLogger(SourceLink_OLD.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    private static DataQueryLink createHttpLink() {
        return new WebDataQueryLink(System.getProperty("http.url"));
    }
}
