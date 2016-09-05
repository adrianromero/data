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

import com.adr.data.DataLink;
import com.adr.data.DataQueryLink;
import com.adr.data.QueryLink;
import com.adr.data.security.SecureLink;
import com.adr.data.sql.SQLQueryLink;
import com.adr.data.security.SecureCommands;
import com.adr.data.sql.SQLDataLink;
import com.adr.data.sql.SQLEngine;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class DataQueryLinkSQL implements DataQueryLinkBuilder {
    
    private final static Logger LOG = Logger.getLogger(DataQueryLinkSQL.class.getName());
    
    private final ComboPooledDataSource cpds;
    private final SQLEngine engine;
    
    public DataQueryLinkSQL(String enginename) {
        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(System.getProperty(enginename + ".database.driver"));
            cpds.setJdbcUrl(System.getProperty(enginename + ".database.url"));
            cpds.setUser(System.getProperty(enginename + ".database.user"));  
            cpds.setPassword(System.getProperty(enginename + ".database.password"));
            engine = SQLEngine.valueOf(System.getProperty(enginename + ".database.engine", SQLEngine.GENERIC.name()));
            
            LOG.log(Level.INFO, "Database engine = {0}", engine.toString());
        } catch (PropertyVetoException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }     
    
    public QueryLink createQueryLink() {
        return new SQLQueryLink(cpds, engine, SecureCommands.QUERIES);
    }
    
    public DataLink createDataLink() {
        return new SQLDataLink(cpds, engine, SecureCommands.COMMANDS);
    }

    @Override
    public DataQueryLink createDataQueryLink() {
        return new SecureLink(
            createQueryLink(),
            createDataLink(),
            new HashSet<>(Arrays.asList("USERNAME_VISIBLE")), // anonymous res
            new HashSet<>(Arrays.asList("authenticatedres"))); // authenticated res
    }
}
