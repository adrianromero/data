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
import com.adr.data.var.Variant;
import com.auth0.jwt.JWT;
import java.util.Collections;
import java.util.List;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.adr.data.route.Reducer;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 *
 * @author adrian
 */
public class ReducerJWTCurrentUser implements Reducer {

    
    @Override
    public List<Record> process(Header headers, List<Record> records) throws DataException {
        
        if (records.size() == 1) {
            Record filter = records.get(0);               
            String entity = Records.getCollection(filter);
            if (ReducerLogin.AUTHENTICATION_CURRENT.equals(entity)) {          
                Variant authorization = headers.getRecord().get("AUTHORIZATION");        
                if (authorization.isNull()) {
                    return Collections.emptyList(); // anonymous
                } else {
                    DecodedJWT jwtauthorizaion = JWT.decode(authorization.asString());
                    // Valid login, load user details.
                    Record currentuser = new Record(
                            Record.entry("COLLECTION.KEY", "USERNAME_BYNAME"),
                            Record.entry("NAME", jwtauthorizaion.getSubject()),
                            Record.entry("DISPLAYNAME", jwtauthorizaion.getClaim("displayname").asString()),
                            Record.entry("ROLE", jwtauthorizaion.getClaim("role").asString()),
                            Record.entry("DISPLAYROLE", jwtauthorizaion.getClaim("displayrole").asString()));
                    return Collections.singletonList(currentuser);
                }
            }
        }      
        return null;
    }
}
