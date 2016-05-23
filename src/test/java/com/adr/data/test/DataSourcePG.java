/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import javax.sql.DataSource;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.BeforeClass;
import org.postgresql.ds.PGPoolingDataSource;

/**
 *
 * @author Eva
 */
public class DataSourcePG {
    
     private static PGPoolingDataSource source;   
    
    public static void setUpDB() throws Exception {       
   
        
        source = new PGPoolingDataSource();
        source.setDataSourceName("localpostgresql");
        source.setServerName("localhost");
        source.setPortNumber(5433);
        source.setDatabaseName("dbtests");
        source.setUser("tad");
        source.setPassword("tad");
        source.setMaxConnections(10);   
    }

    public static void tearDownDB() throws Exception {       
    }
    
    public static DataSource get() {
        return source; 
    }
}
