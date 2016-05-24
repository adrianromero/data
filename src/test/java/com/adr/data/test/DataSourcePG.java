/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.adr.data.sql.SQLDataLink;
import com.adr.data.sql.SQLQueryLink;
import com.adr.data.sqlh2.SentenceH2Put;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcConnectionPool;
import org.postgresql.ds.PGPoolingDataSource;

/**
 *
 * @author Eva
 */
public class DataSourcePG {
    
    private PGPoolingDataSource source;    
    
    public void setUpDB() throws Exception {       
        source = new PGPoolingDataSource();
        source.setDataSourceName("localpostgresql");
        source.setServerName("localhost");
        source.setPortNumber(5433);
        source.setDatabaseName("dbtests");
        source.setUser("tad");
        source.setPassword("tad");
        source.setMaxConnections(10);    
    }

    public void tearDownDB() throws Exception {        
    }
    
    public DataLink getDataLink() {
        return new SQLDataLink(source); 
    }
    
    public QueryLink getQueryLink() {
        return new SQLQueryLink(source);
    }  
}
