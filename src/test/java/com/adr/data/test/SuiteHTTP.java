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

import com.adr.data.testlinks.DataQueryLinkHTTP;
import com.adr.data.testservers.WebSecureServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author adrian
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({QueryTests.class, SecurityTests.class})
public class SuiteHTTP {
    
    private static final int PORT = 4567;
    private static final String CONTEXT = "/data";
    private static final String SQLNAME = "h2";
    
    private static final WebSecureServer server = new WebSecureServer(PORT, CONTEXT, SQLNAME);
    private static final String url = "http://localhost:" + Integer.toString(PORT) + CONTEXT;

    @BeforeClass
    public static void setUpClass() throws Exception {
        server.start();                
        SourceLink.setBuilder(new DataQueryLinkHTTP(url));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        SourceLink.setBuilder(null);       
        server.stop();
    }   
}
