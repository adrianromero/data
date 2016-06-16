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
import com.adr.data.sql.SecureCommands;
import com.adr.data.sqlpg.SentencePGMerge;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Eva
 */
public class DataSourceLink {
     
    private static ComboPooledDataSource cpds = null;
    
    public static DataSource getDataSource() {
        if (cpds == null) {
            try {
                cpds = new ComboPooledDataSource();
                cpds.setDriverClass(System.getProperty("databasedriver"));
                cpds.setJdbcUrl(System.getProperty("databaseurl"));
                cpds.setUser(System.getProperty("databaseuser"));  
                cpds.setPassword(System.getProperty("databasepassword"));
            } catch (PropertyVetoException ex) {
                Logger.getLogger(DataSourceLink.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        return cpds; 
    }
    
    public static DataLink getDataLink() {
        return new SQLDataLink(getDataSource(), new SentencePGMerge(), SecureCommands.COMMANDS); 
    }
    
    public static QueryLink getQueryLink() {
        return new SQLQueryLink(getDataSource(), SecureCommands.QUERIES);
    }  
}
