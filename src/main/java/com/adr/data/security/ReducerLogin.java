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
package com.adr.data.security;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.record.Entry;
import com.adr.data.record.RecordMap;
import com.adr.data.route.ReducerQuery;
import com.adr.data.var.Variant;
import java.util.Collections;
import java.util.List;
import com.adr.data.record.Record;
import com.adr.data.record.Records;

/**
 *
 * @author adrian
 */
public abstract class ReducerLogin implements ReducerQuery {

    public final static String AUTHENTICATION_REQUEST = "AUTHENTICATION_REQUEST";
    public final static String AUTHENTICATION_RESPONSE = "AUTHENTICATION_RESPONSE";

    public final static String AUTHENTICATION_CURRENT = "AUTHENTICATION_CURRENT";
    public final static String AUTHORIZATION_REQUEST = "AUTHORIZATION_REQUEST";

    protected abstract Variant createAuthorization(String user, String password) throws DataException;
    
    @Override
    public List<Record> query(Record headers, Record filter) throws DataException {

        String entity = Records.getCollection(filter);
        if (!AUTHENTICATION_REQUEST.equals(entity)) {
            return null;
        }

        // Filter Authentication requests logins
        String username = filter.getString("NAME");
        String password = filter.getString("PASSWORD");

        Variant authorization = createAuthorization(username, password);

        Record result = new RecordMap(
                new Entry("COLLECTION.KEY", AUTHENTICATION_RESPONSE),
                new Entry("AUTHORIZATION", authorization));
        return Collections.singletonList(result);
    }

    public static String login(QueryLink link, String user, String password) throws DataException {
        Record r = link.find(new RecordMap(
                    new Entry("COLLECTION.KEY", AUTHENTICATION_REQUEST),
                    new Entry("NAME", user),
                    new Entry("PASSWORD", password)));
        return r.getString("AUTHORIZATION");
    }

    public static Record current(QueryLink link, Record headers) throws DataException {
        return link.find(headers,
                new RecordMap(
                            new Entry("COLLECTION.KEY", AUTHENTICATION_CURRENT)));
    }

    public static boolean hasAuthorization(QueryLink link, Record headers, String resource) throws DataException {
        Record result = link.find(headers,
                new RecordMap(
                        new Entry("COLLECTION.KEY", AUTHORIZATION_REQUEST),
                            new Entry("RESOURCE", resource)));

        return result.getBoolean("RESULT");
    }
}
