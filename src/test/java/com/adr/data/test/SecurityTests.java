//     Data Access is a Java library to store data
//     Copyright (C) 2016-2018 Adrián Romero Corchado.
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
package com.adr.data.test;

import com.adr.data.DataException;
import com.adr.data.record.Entry;
import com.adr.data.record.Header;
import com.adr.data.security.SecurityDataException;
import com.adr.data.security.ReducerLogin;
import com.adr.data.var.VariantString;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class SecurityTests {

    @Test
    public void testLoginLogout() throws DataException {

        SourceLink.createDataQueryLink();

        try {
            // Login
            String authorization = ReducerLogin.login(SourceLink.getQueryLink(), "admin", "admin");
            Header header = new Header(new Record(new Entry("AUTHORIZATION", authorization)));

            Record current = ReducerLogin.current(SourceLink.getQueryLink(), header);

            // Assert loging success
            Assert.assertEquals("Administrator", current.getString("DISPLAYNAME"));
            Assert.assertEquals("ADMIN", current.getString("ROLE"));

            // this query succeds because admin has permissions to all resources
            List<Record> result1 = SourceLink.getQueryLink().query(
                    header,
                    new Record(
                            new Entry("COLLECTION.KEY", "USERNAME"),
                            new Entry("ID.KEY", new VariantString("admin")),
                            new Entry("NAME", VariantString.NULL),
                            new Entry("CODECARD", VariantString.NULL)));
            Assert.assertEquals(1, result1.size());

            try {
                // This query fails because not logged users
                SourceLink.getQueryLink().query(
                        Header.EMPTY,
                        new Record(
                                new Entry("COLLECTION.KEY", "USERNAME"),
                                new Entry("ID.KEY", new VariantString("admin")),
                                new Entry("NAME", VariantString.NULL),
                                new Entry("CODECARD", VariantString.NULL)));
                Assert.fail();
            } catch (SecurityDataException ex) {
                Assert.assertEquals("Role Anonymous does not have authorization to query the resource: USERNAME", ex.getMessage());
            }
        } finally {
            SourceLink.destroyDataQueryLink();
        }
    }

    @Test
    public void testLoginManager() throws DataException {

        SourceLink.createDataQueryLink();

        try {
            // Login
            String authorization = ReducerLogin.login(SourceLink.getQueryLink(), "manager", "");
            Header header = new Header(new Record(new Entry("AUTHORIZATION", authorization)));

            Record current = ReducerLogin.current(SourceLink.getQueryLink(), header);
            Assert.assertEquals("Manager", current.getString("DISPLAYNAME"));

            current = ReducerLogin.current(SourceLink.getQueryLink(), Header.EMPTY);
            Assert.assertNull(current);
        } finally {
            SourceLink.destroyDataQueryLink();
        }
    }

    @Test
    public void testAnonymous() throws DataException {

        SourceLink.createDataQueryLink();

        try {
            // this query succeds because anonymous has permissions to USERNAME_VISIBLE
            List<Record> result1 = SourceLink.getQueryLink().query(
                    Header.EMPTY,
                    new Record(
                            new Entry("COLLECTION.KEY", "USERNAME_VISIBLE"),
                            new Entry("ID.KEY", VariantString.NULL),
                            new Entry("NAME", VariantString.NULL),
                            new Entry("DISPLAYNAME", VariantString.NULL)));
            Assert.assertEquals(3, result1.size());
        } finally {
            SourceLink.destroyDataQueryLink();
        }
    }

    @Test
    public void testAuthorizations() throws DataException {

        SourceLink.createDataQueryLink();

        try {

            // Anonymous
            String authorization;
            Header header = Header.EMPTY;
            Assert.assertTrue(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "USERNAME_VISIBLE_QUERY"));
            Assert.assertFalse(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "authenticatedres"));
            Assert.assertFalse(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "com/adr/hellocore/fxml/datalist?datatable=com/adr/hellocore/security/role"));
            Assert.assertFalse(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "anyotherresource"));

            authorization = ReducerLogin.login(SourceLink.getQueryLink(), "manager", "");
            header = new Header(new Record(new Entry("AUTHORIZATION", authorization)));
            Assert.assertTrue(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "USERNAME_VISIBLE_QUERY"));
            Assert.assertTrue(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "authenticatedres"));
            Assert.assertTrue(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "com/adr/hellocore/fxml/datalist?datatable=com/adr/hellocore/security/role"));
            Assert.assertFalse(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "anyotherresource"));

            authorization = ReducerLogin.login(SourceLink.getQueryLink(), "guest", "");
            header = new Header(new Record(new Entry("AUTHORIZATION", authorization)));
            Assert.assertTrue(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "USERNAME_VISIBLE_QUERY"));
            Assert.assertTrue(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "authenticatedres"));
            Assert.assertFalse(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "com/adr/hellocore/fxml/datalist?datatable=com/adr/hellocore/security/role"));
            Assert.assertFalse(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "anyotherresource"));

            authorization = ReducerLogin.login(SourceLink.getQueryLink(), "admin", "admin");
            header = new Header(new Record(new Entry("AUTHORIZATION", authorization)));
            Assert.assertTrue(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "USERNAME_VISIBLE_QUERY"));
            Assert.assertTrue(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "authenticatedres"));
            Assert.assertTrue(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "com/adr/hellocore/fxml/datalist?datatable=com/adr/hellocore/security/role"));
            Assert.assertTrue(ReducerLogin.hasAuthorization(SourceLink.getQueryLink(), header, "anyotherresource"));
        } finally {
            SourceLink.destroyDataQueryLink();
        }
    }
}
