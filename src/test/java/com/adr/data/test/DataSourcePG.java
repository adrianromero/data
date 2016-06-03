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

package com.adr.data.test;

import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.adr.data.sql.SQLDataLink;
import com.adr.data.sql.SQLQueryLink;
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
