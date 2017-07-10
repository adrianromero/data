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
import com.adr.data.DataLink;
import com.adr.data.DataQueryLink;
import com.adr.data.QueryLink;
import com.adr.data.route.ReducerQueryIdentity;
import com.adr.data.route.ReducerQueryLink;
import com.adr.data.sql.SQLQueryLink;
import com.adr.data.security.SecureCommands;
import com.adr.data.security.jwt.ReducerJWTAuthorization;
import com.adr.data.security.jwt.ReducerJWTCurrentUser;
import com.adr.data.security.jwt.ReducerJWTLogin;
import com.adr.data.security.jwt.ReducerJWTVerifyAuthorization;
import com.adr.data.sql.SQLDataLink;
import com.adr.data.sql.SQLEngine;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author adrian
 */
public class DataQueryLinkSQL implements DataQueryLinkBuilder {
    
    private final static Logger LOG = Logger.getLogger(DataQueryLinkSQL.class.getName());
    
    private final DataSource cpds;
    private final SQLEngine engine;
    
    public DataQueryLinkSQL(String enginename) {
         
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(System.getProperty(enginename + ".database.driver"));
        config.setJdbcUrl(System.getProperty(enginename + ".database.url"));
        config.setUsername(System.getProperty(enginename + ".database.user"));  
        config.setPassword(System.getProperty(enginename + ".database.password"));
        
        cpds = new HikariDataSource(config);
        engine = SQLEngine.valueOf(System.getProperty(enginename + ".database.engine", SQLEngine.GENERIC.name()));

        LOG.log(Level.INFO, "Database engine = {0}", engine.toString());
    }     
    
    public QueryLink createQueryLink() {
        return new ReducerQueryLink(
                new SQLQueryLink(cpds, engine, SecureCommands.QUERIES),
                new ReducerJWTVerifyAuthorization("secret".getBytes(StandardCharsets.UTF_8)),
                new ReducerJWTLogin("secret".getBytes(StandardCharsets.UTF_8), 5000),
                new ReducerJWTCurrentUser(),
                new ReducerJWTAuthorization(new HashSet<>(Arrays.asList("USERNAME_VISIBLE")), new HashSet<>(Arrays.asList("authenticatedres"))),
                ReducerQueryIdentity.INSTANCE);
    }
    
    public DataLink createDataLink() {
        return new SQLDataLink(cpds, engine, SecureCommands.COMMANDS);
    }

    @Override
    public DataQueryLink create() {
        return new BasicDataQueryLink(createQueryLink(),  createDataLink());
    }

    @Override
    public void destroy() {
    }
}
