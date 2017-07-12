//     Data Access is a Java library to store data
//     Copyright (C) 2016 Adrián Romero Corchado.
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
import com.adr.data.record.Record;
import com.adr.data.recordmap.Entry;
import com.adr.data.recordmap.RecordMap;
import java.util.List;

/**
 *
 * @author adrian
 */
public class SecureFacade {
//
//    private final QueryLink securelink;
//
//    public SecureFacade(QueryLink securelink) {
//        this.securelink = securelink;
//    }
//
//    public Record login(String username, String password) throws DataException {
//        // Convenience login method
//        return securelink.find(new RecordMap(
//                new Entry[]{
//                    new Entry("__ENTITY", SecureLink.AUTHENTICATION_REQUEST)},
//                new Entry[]{
//                    new Entry("NAME", username),
//                    new Entry("PASSWORD", password)}));
//    }
//
//    public void logout() throws DataException {
//        // Convenience logout method
//        securelink.find(new RecordMap(
//                new Entry[]{
//                    new Entry("__ENTITY", SecureLink.AUTHENTICATION_REQUEST)}));
//    }
//
//    public Record current() throws DataException {
//        // Convenience logout method
//        return securelink.find(new RecordMap(
//                new Entry[]{
//                    new Entry("__ENTITY", SecureLink.AUTHENTICATION_CURRENT)}));
//    }
//
//    public Record saveCurrent(Record login) throws DataException {
//        return securelink.find(login);
//    }
//
//    public void savePassword(String name, String oldpassword, String password) throws DataException {
//        securelink.find(new RecordMap(
//                new Entry[]{
//                    new Entry("__ENTITY", SecureLink.AUTHENTICATION_PASSWORD)},
//                new Entry[]{
//                    new Entry("NAME", name),
//                    new Entry("OLDPASSWORD", oldpassword),
//                    new Entry("PASSWORD", password)}));
//    }
//
//    public boolean hasAuthorization(String resource, String action) throws DataException {
//        Record result = securelink.find(new RecordMap(
//                new Entry[]{
//                    new Entry("__ENTITY", SecureLink.AUTHORIZATION_REQUEST)},
//                new Entry[]{
//                    new Entry("RESOURCE", resource),
//                    new Entry("ACTION", action)}));
//
//        return result.getBoolean("RESULT");
//    }
//
//    public boolean hasAuthorization(String resource) throws DataException {
//        return hasAuthorization(resource, null);
//    }
//
//    public List<Record> getCurrentRoleAuthorizations() throws DataException {
//        // Convenience logout method
//        return securelink.query(new RecordMap(
//                new Entry[]{
//                    new Entry("__ENTITY", SecureLink.AUTHORIZATIONS_QUERY)}));
//    }
}
