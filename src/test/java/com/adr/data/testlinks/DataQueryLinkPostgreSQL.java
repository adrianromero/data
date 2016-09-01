/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.testlinks;

import com.adr.data.DataQueryLink;
import com.adr.data.security.SecureLink;
import com.adr.data.sql.SQLDataLink;
import com.adr.data.sql.SQLQueryLink;
import com.adr.data.security.SecureCommands;
import com.adr.data.sql.SQLEngine;
import com.adr.data.sql.SentencePut;
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
public class DataQueryLinkPostgreSQL implements DataQueryLinkBuilder {
    
    private final ComboPooledDataSource cpds;
    private final SQLEngine engine;
    
    public DataQueryLinkPostgreSQL() {
        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(System.getProperty("database.driver"));
            cpds.setJdbcUrl(System.getProperty("database.url"));
            cpds.setUser(System.getProperty("database.user"));  
            cpds.setPassword(System.getProperty("database.password"));
            engine = SQLEngine.valueOf(System.getProperty("database.engine", SQLEngine.GENERIC.name()));
        } catch (PropertyVetoException ex) {
            Logger.getLogger(SourceLink_OLD.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }        

    @Override
    public DataQueryLink createDataQueryLink() {
        return new SecureLink(
            new SQLQueryLink(cpds, engine, SecureCommands.QUERIES),
            new SQLDataLink(cpds, engine, SecureCommands.COMMANDS),
            new HashSet<>(Arrays.asList("USERNAME_VISIBLE")), // anonymous res
            new HashSet<>(Arrays.asList("authenticatedres"))); // authenticated res
    }
}
