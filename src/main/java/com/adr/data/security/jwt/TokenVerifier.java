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
package com.adr.data.security.jwt;

import com.adr.data.DataException;
import com.adr.data.record.Header;
import com.adr.data.security.SecurityDataException;
import com.adr.data.var.Variant;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 *
 * @author adrian
 */
public class TokenVerifier {
    
    private final JWTVerifier verifier;
    
    public TokenVerifier(byte[] secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm).build();
    }

    public void verify(Header headers) throws DataException {

        Variant authorization = headers.getRecord().get("AUTHORIZATION");        
        
        if (authorization.isNull()) {
            return; // anonymous;
        }
         
        try {
            verifier.verify(authorization.asString());
        } catch (JWTVerificationException exception) {
            throw new SecurityDataException(exception);
        }
    }    
}
