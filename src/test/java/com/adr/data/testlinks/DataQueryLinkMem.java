//     Data Access is a Java library to store data
//     Copyright (C) 2018 Adrián Romero Corchado.
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
package com.adr.data.testlinks;

import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.adr.data.mem.MemDataLink;
import com.adr.data.mem.MemQueryLink;
import com.adr.data.mem.Storage;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class DataQueryLinkMem implements DataQueryLinkBuilder {
    
    private static final Logger LOG = Logger.getLogger(DataQueryLinkMem.class.getName());  

    private QueryLink querylink;
    private DataLink datalink;
    
    public DataQueryLinkMem() {      
    }

    @Override
    public void create() {
        Storage storage = new Storage();
        datalink = new MemDataLink(storage);
        querylink = new MemQueryLink(storage);
    }
    
    @Override
    public void destroy() {
        datalink = null;
        querylink = null;       
    }    

    @Override
    public QueryLink getQueryLink() {
        return querylink;
    }

    @Override
    public DataLink getDataLink() {
        return datalink;
    }
}
