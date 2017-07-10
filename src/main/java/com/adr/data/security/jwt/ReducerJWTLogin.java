/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.security.jwt;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.recordmap.Entry;
import com.adr.data.record.Record;
import com.adr.data.recordmap.RecordMap;
import com.adr.data.security.CryptUtils;
import com.adr.data.security.ReducerLogin;
import com.adr.data.security.SecurityDataException;
import com.adr.data.var.Variant;
import com.adr.data.var.VariantString;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import java.util.Date;

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
                    new Entry("__ENTITY", "USERNAME_BYNAME")},
                new Entry[]{
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
