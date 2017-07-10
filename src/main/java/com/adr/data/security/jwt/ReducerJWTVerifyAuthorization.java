/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.security.jwt;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.record.Record;
import com.adr.data.record.Values;
import com.adr.data.route.ReducerQuery;
import com.adr.data.security.SecurityDataException;
import com.adr.data.var.Variant;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.List;

/**
 *
 * @author adrian
 */
public class ReducerJWTVerifyAuthorization implements ReducerQuery {
    
    private final JWTVerifier verifier;
    
    public ReducerJWTVerifyAuthorization(byte[] secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm).build();
    }

    
    @Override
    public List<Record> query(QueryLink link, Values headers, Record filter) throws DataException {

        Variant authorization = headers.get("Authorization");        
        
        if (authorization.isNull()) {
            return null; // anonymous;
        }
         
        try {
            verifier.verify(authorization.asString());
        } catch (JWTVerificationException exception) {
            throw new SecurityDataException(exception);
        } 
       
        return null;
    }
}
