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
import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.adr.data.route.ReducerData;
import com.adr.data.security.SecurityDataException;
import com.adr.data.var.Variant;
import com.auth0.jwt.JWT;
import java.util.List;
import java.util.Set;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 *
 * @author adrian
 */
public class ReducerDataJWTAuthorization implements ReducerData {
    
    private final QueryLink querylink;
    private final Authorizer authorizer;

    public ReducerDataJWTAuthorization(QueryLink querylink, Set<String> anonymousresources, Set<String> authenticatedresources) {
        this.querylink = querylink;
        authorizer = new Authorizer(anonymousresources, authenticatedresources);
    }

    @Override
    public boolean execute(DataLink link, Record headers, List<Record> l) throws DataException {

        Variant authorization = headers.get("Authorization");        
        String role;
        String roledisplay;
        if (authorization.isNull()) {
            role = "ANONYMOUS";
            roledisplay = "Anonymous";
        } else {
            JWT jwtauthorization = JWT.decode(authorization.asString());         
            role = jwtauthorization.getClaim("role").asString();
            roledisplay = jwtauthorization.getClaim("roledisplay").asString();
        }
        
        for (Record r: l) {
            String entity = Records.getEntity(r);
            if (!authorizer.hasAuthorization(querylink, role, entity + Authorizer.ACTION_EXECUTE)) {
                throw new SecurityDataException("Role " + roledisplay + " does not have authorization to execute the resource: " + entity);
            }
        }
        return false;
    }
}
