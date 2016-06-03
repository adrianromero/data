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
import com.adr.data.sql.LoginCommands;
import com.adr.data.sql.SQLDataLink;
import com.adr.data.sql.SQLQueryLink;
import com.adr.data.sqlh2.SentenceH2Put;
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
        return new SQLDataLink(cpds, new SentenceH2Put(), LoginCommands.COMMANDS); 
    }
    
    public QueryLink getQueryLink() {
        return new SQLQueryLink(cpds, LoginCommands.QUERIES);
    }  
}
