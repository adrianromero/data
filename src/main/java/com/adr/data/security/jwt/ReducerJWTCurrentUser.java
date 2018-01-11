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
import com.adr.data.record.Entry;
import com.adr.data.record.RecordMap;
import com.adr.data.route.ReducerQuery;
import com.adr.data.security.ReducerLogin;
import com.adr.data.var.Variant;
import com.auth0.jwt.JWT;
import java.util.Collections;
import java.util.List;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 *
 * @author adrian
 */
public class ReducerJWTCurrentUser implements ReducerQuery {

    
    @Override
    public List<Record> query(Record headers, Record filter) throws DataException {
        
        String entity = Records.getCollection(filter);
        if (!ReducerLogin.AUTHENTICATION_CURRENT.equals(entity)) {          
            return null;
        }
        
        Variant authorization = headers.get("AUTHORIZATION");        
        
        if (authorization.isNull()) {
            return Collections.emptyList(); // anonymous
        } else {
            JWT jwtauthorizaion = JWT.decode(authorization.asString());
            // Valid login, load user details.
            Record currentuser = new RecordMap(
                        new Entry("COLLECTION.KEY", "USERNAME_BYNAME"),
                        new Entry("NAME", jwtauthorizaion.getSubject()),
                        new Entry("DISPLAYNAME", jwtauthorizaion.getClaim("displayname").asString()),
                        new Entry("ROLE", jwtauthorizaion.getClaim("role").asString()),
                        new Entry("DISPLAYROLE", jwtauthorizaion.getClaim("displayrole").asString()));
            return Collections.singletonList(currentuser);
        }
    }
}
