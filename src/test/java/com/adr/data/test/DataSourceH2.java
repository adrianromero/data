/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import javax.sql.DataSource;
import org.h2.jdbcx.JdbcConnectionPool;

/**
 *
 * @author Eva
 */
public class DataSourceH2 {
    
    private static JdbcConnectionPool cpds;   
    
    public static void setUpDB() throws Exception {       
        cpds = JdbcConnectionPool.create("jdbc:h2:~/h2testdb", "sa", "");     
    }

    public static void tearDownDB() throws Exception {
        if (cpds != null) {        
            cpds.dispose();
            cpds = null;
        }        
    }
    
    public static DataSource get() {
        return cpds; 
    }

    
}
