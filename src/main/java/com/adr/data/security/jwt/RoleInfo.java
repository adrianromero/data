//     Data Access is a Java library to store data
//     Copyright (C) 2019 Adrián Romero Corchado.
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

import com.adr.data.record.Header;
import com.adr.data.var.Variant;
import com.adr.data.varrw.Variants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 *
 * @author adrian
 */
public class RoleInfo {
    
    private final String role;
    private final String displayrole;
    
    public RoleInfo(Header headers) {
        Variant authorization = headers.getRecord().get("AUTHORIZATION");        
        if (Variants.isNull(authorization)) {
            role = "ANONYMOUS";
            displayrole = "Anonymous";
        } else {
            DecodedJWT jwtauthorization = JWT.decode(authorization.asString());         
            role = jwtauthorization.getClaim("role").asString();
            displayrole = jwtauthorization.getClaim("displayrole").asString();
        }        
    }
    
    public String getRole() {
        return role;
    }
    
    public String getDisplayRole() {
        return displayrole;
    }
}
