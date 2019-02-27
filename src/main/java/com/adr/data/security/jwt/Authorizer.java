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
//     limitations under the License
package com.adr.data.security.jwt;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.var.VariantString;
import java.util.Set;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class Authorizer {

    public final static String ACTION_QUERY = "_QUERY";
    public final static String ACTION_EXECUTE = "_EXECUTE";

    private final Set<String> anonymousresources; // resources everybody logged or not has access
    private final Set<String> authenticatedresources; // resources everybody logged has access

    public Authorizer(Set<String> anonymousresources, Set<String> authenticatedresources) {
        this.anonymousresources = anonymousresources;
        this.authenticatedresources = authenticatedresources;
    }

    public boolean hasAuthorization(QueryLink link, String role, String resource) throws DataException {

        // Everybody logged or not has access to anonymous resources
        if (anonymousresources.contains(resource)) {
            return true;
        }

        // if not logged it does not have permissions to anything else
        if ("ANONYMOUS".equals(role)) {
            return false;
        }

        // Everybody logged has permissions to authenticated resources
        if (authenticatedresources.contains(resource)) {
            return true;
        }

        // Admin has permissions to everything
        if ("ADMIN".equals(role)) {
            return true;
        }

        // Valid login, load user details.
        Record subjectsquery = new Record(
                Record.entry("COLLECTION.KEY", "ROLE_SUBJECT"),
                Record.entry("ROLE", role),
                Record.entry("SUBJECT", resource),
                Record.entry("SUBJECTNAME", VariantString.NULL));
        return link.find(subjectsquery) != null;
    }
}
