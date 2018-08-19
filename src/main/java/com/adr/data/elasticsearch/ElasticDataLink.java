//     Data Access is a Java library to store data
//     Copyright (C) 2017-2018 Adrián Romero Corchado.
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
package com.adr.data.elasticsearch;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.record.Header;
import java.util.List;
import org.elasticsearch.client.transport.TransportClient;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class ElasticDataLink implements DataLink {

    private final TransportClient client;

//        client = new PreBuiltTransportClient(Settings.EMPTY)
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
// DO STUFF
//        client.close()
    
    public ElasticDataLink(TransportClient client) {
        this.client = client;
    }


    @Override
    public void execute(Header headers, List<Record> l) throws DataException {
        
//        Map<String, Object> jsonDocument = new HashMap<>();       
//        
//        UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
//                .doc(jsonDocument)
//                .docAsUpsert(true);
//        client.update(updateRequest).get();        
    }
}
