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
import com.adr.data.security.CryptUtils;
import com.adr.data.security.ReducerLogin;
import com.adr.data.security.SecurityDataException;
import com.adr.data.var.Variant;
import com.adr.data.var.VariantString;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import java.util.Date;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class ReducerJWTLogin extends ReducerLogin {
    
    private final Algorithm algorithm;
    private final long validtime;
    
    public ReducerJWTLogin(byte[] secret, long validtime) {
        try {
            this.algorithm = Algorithm.HMAC256(secret);
            this.validtime = validtime;
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Variant createAuthorization(QueryLink link, String username, String password) throws DataException {
        Record userauthenticationquery = new RecordMap(
                new Entry[]{
                    new Entry("__ENTITY", "USERNAME_BYNAME"),
                    new Entry("NAME", username),
                    new Entry("DISPLAYNAME", VariantString.NULL),
                    new Entry("ROLE", VariantString.NULL),                    
                    new Entry("ROLEDISPLAY", VariantString.NULL),                    
                    new Entry("PASSWORD", VariantString.NULL)});

        Record userauthentication = link.find(userauthenticationquery);  
        
        if (userauthentication == null || !CryptUtils.validatePassword(password, userauthentication.getString("PASSWORD"))) {
            // Invalid login
            throw new SecurityDataException ("Invalid user or password.");
        } else {        
            try {
                long now = new Date().getTime();
                String authorization = JWT.create()
                        .withIssuer("datajwt")
                        .withSubject(username)
                        .withClaim("displayname", userauthentication.getString("DISPLAYNAME"))
                        .withClaim("role",  userauthentication.getString("ROLE"))
                        .withClaim("roledisplay",  userauthentication.getString("ROLEDISPLAY"))
                        .withIssuedAt(new Date(now))
                        .withExpiresAt(new Date(now + validtime))
                        .sign(algorithm);

                return new VariantString(authorization);
            } catch (JWTCreationException exception) {
                throw new SecurityDataException(exception);
            }
        }
    }
}
