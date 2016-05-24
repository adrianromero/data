/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import com.adr.data.DataLink;
import com.adr.data.QueryLink;

/**
 *
 * @author adrian
 */
public class DataSourceLink {
    
    private static final DataSourceH2 datasource = new DataSourceH2();
    
    public static void setUpDB() throws Exception {       
        datasource.setUpDB();
    }

    public static void tearDownDB() throws Exception {
        datasource.tearDownDB();       
    }
    
    public static DataLink getDataLink() {
        return datasource.getDataLink(); 
    }
    
    public static QueryLink getQueryLink() {
        return datasource.getQueryLink(); 
    }    
}
