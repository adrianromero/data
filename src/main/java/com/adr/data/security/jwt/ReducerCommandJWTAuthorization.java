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
import com.adr.data.QueryLink;
import com.adr.data.record.Header;
import com.adr.data.security.SecurityDataException;
import java.util.List;
import java.util.Set;
import com.adr.data.record.Record;
import com.adr.data.record.Records;
import com.adr.data.route.ReducerCommand;

/**
 *
 * @author adrian
 */
public class ReducerCommandJWTAuthorization implements ReducerCommand {
    
    private final QueryLink querylink;
    private final Authorizer authorizer;

    public ReducerCommandJWTAuthorization(QueryLink querylink, Set<String> anonymousresources, Set<String> authenticatedresources) {
        this.querylink = querylink;
        authorizer = new Authorizer(anonymousresources, authenticatedresources);
    }

    @Override
    public boolean execute(Header headers, List<Record> l) throws DataException {

        RoleInfo roleinfo = new RoleInfo(headers);
        
        for (Record r: l) {
            String collectionkey = Records.getCollection(r);
            if (!authorizer.hasAuthorization(querylink, roleinfo.getRole(), collectionkey + Authorizer.ACTION_EXECUTE)) {
                throw new SecurityDataException("Role " + roleinfo.getDisplayRole() + " does not have authorization to execute the resource: " + collectionkey);
            }
        }
        return false;
    }
}
