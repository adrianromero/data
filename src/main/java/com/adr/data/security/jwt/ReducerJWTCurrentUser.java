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
import com.adr.data.recordmap.Entry;
import com.adr.data.recordmap.RecordMap;
import com.adr.data.route.ReducerQuery;
import com.adr.data.security.ReducerLogin;
import com.adr.data.var.Variant;
import com.adr.data.var.VariantString;
import com.auth0.jwt.JWT;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author adrian
 */
public class ReducerJWTCurrentUser implements ReducerQuery {

    
    @Override
    public List<Record> query(QueryLink link, Values headers, Record filter) throws DataException {
        
        String entity = filter.getKey().get("__ENTITY").asString();
        if (!ReducerLogin.AUTHENTICATION_CURRENT.equals(entity)) {          
            return null;
        }
        
        Variant authorization = headers.get("Authorization");        
        
        if (authorization.isNull()) {
            return Collections.emptyList(); // anonymous
        } else {
            JWT jwtauthorizaion = JWT.decode(authorization.asString());
            // Valid login, load user details.
            Record usernamequery = new RecordMap(
                    new Entry[]{
                        new Entry("__ENTITY", "USERNAME_BYNAME"),
                        new Entry("ID", VariantString.NULL)},
                    new Entry[]{
                        new Entry("NAME", jwtauthorizaion.getSubject()),
                        new Entry("DISPLAYNAME", VariantString.NULL),
                        new Entry("ROLE", VariantString.NULL),
                        new Entry("ROLEDISPLAY", VariantString.NULL)});
            return link.query(usernamequery);
        }
    }
}
