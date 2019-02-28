//     Data Access is a Java library to store data
//     Copyright (C) 2017-2019 Adrián Romero Corchado.
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

package com.adr.data.security.jwt;

import com.adr.data.DataException;
import com.adr.data.record.Header;
import com.adr.data.security.ReducerLogin;
import com.adr.data.security.SecurityDataException;
import com.adr.data.var.VariantBoolean;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.adr.data.Link;
import com.adr.data.route.Reducer;

/**
 *
 * @author adrian
 */
public class ReducerJWTAuthorization implements Reducer {
    
    private final Link querylink;
    private final Authorizer authorizer;
    private final String action;

    public ReducerJWTAuthorization(Link querylink, String action, Set<String> anonymousresources, Set<String> authenticatedresources) {        
        this.querylink = querylink;
        this.action = action;
        authorizer = new Authorizer(anonymousresources, authenticatedresources);
    }
        
    @Override
    public List<Record> process(Header headers, List<Record> records) throws DataException {
                 
        RoleInfo roleinfo = new RoleInfo(headers);
        
        if (records.size() == 1) {
            Record filter = records.get(0);               
            String entity = Records.getCollection(filter);
            if (ReducerLogin.AUTHORIZATION_REQUEST.equals(entity)) {        
                // Request authorizer
                String resource = filter.getString("RESOURCE");
                Record response = new Record(
                        Record.entry("COLLECTION.KEY", ReducerLogin.AUTHORIZATION_REQUEST),
                        Record.entry("RESOURCE", resource),
                        Record.entry("ROLE", roleinfo.getRole()),
                        Record.entry("RESULT", new VariantBoolean(authorizer.hasAuthorization(querylink, roleinfo.getRole(), resource))));
                return Collections.singletonList(response);        
            }
        }

        for (Record r: records) {
            String collectionkey = Records.getCollection(r);
            if (!authorizer.hasAuthorization(querylink, roleinfo.getRole(), collectionkey + "_" + action)) {
                throw new SecurityDataException("Role " + roleinfo.getDisplayRole() + " does not have authorization to process the action \"" + action +"\" on the resource \"" + collectionkey + "\"");
            }      
        }
        return null;          
    }    
}
