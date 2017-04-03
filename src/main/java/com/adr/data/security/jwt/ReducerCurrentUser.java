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
import com.adr.data.var.Variant;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantBytes;
import com.adr.data.var.VariantString;
import com.auth0.jwt.JWT;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author adrian
 */
public abstract class ReducerCurrentUser implements ReducerQuery {
    
    public final static String AUTHENTICATION_CURRENT = "AUTHENTICATION_CURRENT";
    
    @Override
    public List<Record> query(QueryLink link, Values headers, Record filter) throws DataException {
        
        String entity = filter.getKey().get("__ENTITY").asString();
        if (!AUTHENTICATION_CURRENT.equals(entity)) {          
            return null;
        }
        
        Variant token = headers.get("token");        
        
        if (token.isNull()) {
            return Collections.emptyList(); // anonymous
        } else {
            JWT jwttoken = JWT.decode(token.asString());
            // Valid login, load user details.
            Record usernamequery = new RecordMap(
                    new Entry[]{
                        new Entry("__ENTITY", "USERNAME_BYNAME"),
                        new Entry("ID", VariantString.NULL)},
                    new Entry[]{
                        new Entry("NAME", jwttoken.getSubject()),
                        new Entry("DISPLAYNAME", VariantString.NULL),
                        new Entry("CODECARD", VariantString.NULL),
                        new Entry("ROLE_ID", VariantString.NULL),
                        new Entry("ROLE", VariantString.NULL),
                        new Entry("VISIBLE", VariantBoolean.NULL),
                        new Entry("IMAGE", VariantBytes.NULL)});
            return link.query(usernamequery);
        }
    }
 
}
