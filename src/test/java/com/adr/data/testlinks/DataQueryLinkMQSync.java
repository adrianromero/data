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
import com.adr.data.rabbitmq.MQDataLinkSync;
import com.adr.data.rabbitmq.MQQueryLink;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class DataQueryLinkMQSync implements DataQueryLinkBuilder {
    
    private final String host;
    private final String queryexchange;
    private final String dataexchange;
    private Connection connection = null;
        
    public DataQueryLinkMQSync(String host, String queryexchange, String dataexchange) {
        this.host = host;
        this.queryexchange = queryexchange;
        this.dataexchange = dataexchange;
    }

    @Override
    public DataQueryLink createDataQueryLink() {
        try {
            return new BasicDataQueryLink(
                new MQQueryLink(getConnection(), queryexchange),
                new MQDataLinkSync(getConnection(), dataexchange));
        } catch (IOException ex) {
            Logger.getLogger(SourceLink_OLD.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    public Connection getConnection() {
        if (connection == null) {
            try {
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
}
