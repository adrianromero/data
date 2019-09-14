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
package com.adr.data.security;

import com.adr.data.DataException;
import com.adr.data.record.Header;
import com.adr.data.var.Variant;
import java.util.Collections;
import java.util.List;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.adr.data.Link;
import com.adr.data.route.Reducer;

/**
 *
 * @author adrian
 */
public abstract class ReducerLogin implements Reducer {

    public final static String AUTHENTICATION_REQUEST = "AUTHENTICATION_REQUEST";
    public final static String AUTHENTICATION_RESPONSE = "AUTHENTICATION_RESPONSE";

    public final static String AUTHENTICATION_CURRENT = "AUTHENTICATION_CURRENT";
    public final static String AUTHORIZATION_REQUEST = "AUTHORIZATION_REQUEST";

    protected abstract Variant createAuthorization(String user, String password) throws DataException;

    @Override
    public List<Record> process(Header headers, List<Record> records) throws DataException {

        if (records.size() == 1) {
            Record filter = records.get(0);
            String entity = Records.getCollection(filter);
            if (AUTHENTICATION_REQUEST.equals(entity)) {
                // Filter Authentication requests logins
                String username = filter.getString("NAME");
                String password = filter.getString("PASSWORD");

                Variant authorization = createAuthorization(username, password);

                Record result = Record.builder()
                        .entry("COLLECTION.KEY", AUTHENTICATION_RESPONSE)
                        .entry("AUTHORIZATION", authorization)
                        .build();
                return Collections.singletonList(result);
            }
        }
        return null;
    }

    public static String login(Link link, String user, String password) throws DataException {
        Record r = link.find(Record.builder()
                .entry("COLLECTION.KEY", AUTHENTICATION_REQUEST)
                .entry("NAME", user)
                .entry("PASSWORD", password)
                .build());
        if (r == null) {
            throw new SecurityDataException("Invalid security request.");
        }
        return r.getString("AUTHORIZATION"); // Throw SecurityDataException if null
    }

    public static Record current(Link link, Header headers) throws DataException {
        return link.find(headers,
                new Record("COLLECTION.KEY", AUTHENTICATION_CURRENT));
    }

    public static boolean hasAuthorization(Link link, Header headers, String resource) throws DataException {
        Record result = link.find(headers, Record.builder()
                .entry("COLLECTION.KEY", AUTHORIZATION_REQUEST)
                .entry("RESOURCE", resource)
                .build());
        if (result == null) {
            throw new SecurityDataException("Invalid security request.");
        }
        return result.getBoolean("RESULT");
    }
}
