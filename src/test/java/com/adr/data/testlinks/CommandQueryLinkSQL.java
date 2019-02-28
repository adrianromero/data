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

import com.adr.data.route.ReducerIdentity;
import com.adr.data.route.ReducerLink;
import com.adr.data.sql.SQLQueryLink;
import com.adr.data.security.SecureSentences;
import com.adr.data.security.jwt.ReducerCommandJWTVerify;
import com.adr.data.security.jwt.ReducerJWTAuthorization;
import com.adr.data.security.jwt.ReducerJWTCurrentUser;
import com.adr.data.security.jwt.ReducerJWTLogin;
import com.adr.data.sql.SQLCommandLink;
import com.adr.data.sql.SQLEngine;
import com.adr.data.sql.Sentence;
import com.adr.data.sql.SentenceView;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import com.google.common.collect.ObjectArrays;
import com.adr.data.Link;

/**
 *
 * @author adrian
 */
public class CommandQueryLinkSQL implements CommandQueryLinkBuilder {

    private final static Logger LOG = Logger.getLogger(CommandQueryLinkSQL.class.getName());

    private final DataSource cpds;
    private final SQLEngine engine;
    
    private Link querylink;
    private Link commandlink;
    
    public CommandQueryLinkSQL(String enginename) {

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(System.getProperty(enginename + ".database.driver"));
        config.setJdbcUrl(System.getProperty(enginename + ".database.url"));
        config.setUsername(System.getProperty(enginename + ".database.user"));
        config.setPassword(System.getProperty(enginename + ".database.password"));

        cpds = new HikariDataSource(config);
        engine = SQLEngine.valueOf(System.getProperty(enginename + ".database.engine", SQLEngine.GENERIC.name()));

        LOG.log(Level.INFO, "Database engine = {0}", engine.toString());
    }

    private Link createQueryLink() {
        Sentence[] morequeries = new Sentence[]{
            new SentenceView(
            "TEST_USERNAME_VIEW",
            "SELECT ID, NAME, DISPLAYNAME, IMAGE FROM USERNAME WHERE VISIBLE = TRUE AND ACTIVE = TRUE ORDER BY NAME"),
            new SentenceView(
            "ANONYMOUS_VISIBLE",
            "SELECT ID, NAME, DISPLAYNAME FROM USERNAME WHERE VISIBLE = TRUE AND ACTIVE = TRUE"),
        };
        Link query = new SQLQueryLink(cpds, engine, ObjectArrays.concat(SecureSentences.QUERIES, morequeries, Sentence.class));   

        return new ReducerLink(
                new ReducerCommandJWTVerify("secret".getBytes(StandardCharsets.UTF_8)),
                new ReducerJWTLogin(query, "secret".getBytes(StandardCharsets.UTF_8), 5000),
                new ReducerJWTCurrentUser(),
                new ReducerJWTAuthorization(query, "QUERY", new HashSet<>(Arrays.asList("ANONYMOUS_VISIBLE_QUERY")), new HashSet<>(Arrays.asList("AUTHENTICATED_VISIBLE_QUERY"))),
                new ReducerIdentity(query));
    }

    private Link createCommandLink() {
                                   
        Link query = new SQLQueryLink(cpds, engine, 
                SecureSentences.QUERIES);
        Link command = new SQLCommandLink(cpds, engine, SecureSentences.COMMANDS);
        
        return new ReducerLink(
                new ReducerCommandJWTVerify("secret".getBytes(StandardCharsets.UTF_8)),
                new ReducerJWTAuthorization(query, "EXECUTE", new HashSet<>(Arrays.asList("ANONYMOUS_VISIBLE_QUERY")), new HashSet<>(Arrays.asList("AUTHENTICATED_VISIBLE_QUERY"))),
                new ReducerIdentity(command));
    }

    @Override
    public void create() {
        querylink = createQueryLink();
        commandlink = createCommandLink();
    }

    @Override
    public void destroy() {
        querylink = null;
        commandlink = null;
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
