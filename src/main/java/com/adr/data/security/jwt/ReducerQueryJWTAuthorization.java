//     Data Access is a Java library to store data
//     Copyright (C) 2017 Adrián Romero Corchado.
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
import com.adr.data.QueryLink;
import com.adr.data.record.Entry;
import com.adr.data.record.RecordMap;
import com.adr.data.route.ReducerQuery;
import com.adr.data.security.ReducerLogin;
import com.adr.data.security.SecurityDataException;
import com.adr.data.var.Variant;
import com.adr.data.var.VariantBoolean;
import com.auth0.jwt.JWT;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 *
 * @author adrian
 */
public class ReducerQueryJWTAuthorization implements ReducerQuery {
    
    private final QueryLink querylink;
    private final Authorizer authorizer;

    public ReducerQueryJWTAuthorization(QueryLink querylink, Set<String> anonymousresources, Set<String> authenticatedresources) {
        this.querylink = querylink;
        authorizer = new Authorizer(anonymousresources, authenticatedresources);
    }
        
    @Override
    public List<Record> query(Record headers, Record filter) throws DataException {
            
        Variant authorization = headers.get("Authorization");        
        String role;
        String displayrole;
        if (authorization.isNull()) {
            role = "ANONYMOUS";
            displayrole = "Anonymous";
        } else {
            JWT jwtauthorization = JWT.decode(authorization.asString());         
            role = jwtauthorization.getClaim("role").asString();
            displayrole = jwtauthorization.getClaim("displayrole").asString();
        }
        
        String entity = Records.getCollection(filter);
        if (ReducerLogin.AUTHORIZATION_REQUEST.equals(entity)) {        
            // Request authorizer
            String resource = filter.getString("RESOURCE");
            Record response = new RecordMap(
                    new Entry("COLLECTION.KEY", ReducerLogin.AUTHORIZATION_REQUEST),
                    new Entry("RESOURCE", resource),
                    new Entry("ROLE", role),
                    new Entry("RESULT", new VariantBoolean(authorizer.hasAuthorization(querylink, role, resource))));
            return Collections.singletonList(response);             
        } else {
            // Normal query
            if (authorizer.hasAuthorization(querylink, role, entity + Authorizer.ACTION_QUERY)) {
                return null;
            } else {
                throw new SecurityDataException("Role " + displayrole + " does not have authorization to query the resource: " + entity);
            }            
        }          
    }    
}
