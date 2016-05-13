//    Data SQL is a light JDBC wrapper.
//    Copyright (C) 2014-2015 Adrián Romero Corchado.
//
//    This file is part of Data SQL
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

package com.adr.data.test;



import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.postgresql.ds.PGPoolingDataSource;

/**
 *
 * @author adrian
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ProcessTests.class
})
public class PGTestSuite {
    
    private static PGPoolingDataSource source = null;   
    
    @BeforeClass
    public static void setUpClass() throws Exception { 
        
        source = new PGPoolingDataSource();
        source.setDataSourceName("localpostgresql");
        source.setServerName("localhost");
        source.setPortNumber(5433);
        source.setDatabaseName("dbtests");
        source.setUser("tad");
        source.setPassword("tad");
        source.setMaxConnections(10);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {      
    }
    
    public static  DataSource  getDataSource() {
        return source; 
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
