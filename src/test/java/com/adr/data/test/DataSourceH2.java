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

/**
 *
 * @author Eva
 */
public class DataSourceH2 {
    
    private JdbcConnectionPool cpds;   
    
    public void setUpDB() throws Exception {       
        cpds = JdbcConnectionPool.create("jdbc:h2:~/h2testdb", "sa", "");     
    }

    public void tearDownDB() throws Exception {
        if (cpds != null) {        
            cpds.dispose();
            cpds = null;
        }        
    }
    
    public DataLink getDataLink() {
        return new SQLDataLink(cpds, new SentenceH2Put()); 
    }
    
    public QueryLink getQueryLink() {
        return new SQLQueryLink(cpds);
    }  
}
