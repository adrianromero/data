//     Data Access is a Java library to store data
//     Copyright (C) 2016-2019 Adrián Romero Corchado.
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
package com.adr.data.test.persist;

import com.adr.data.DataException;
import com.adr.data.Link;
import com.adr.data.record.Header;
import com.adr.data.security.ReducerLogin;
import org.junit.Assert;
import org.junit.Test;
import com.adr.data.record.Record;
import com.adr.data.test.SourceLink;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantString;

/**
 *
 * @author adrian
 */
public class DataTests {

    @Test
    public void testSomeUpdates() throws DataException {

        SourceLink.createCommandQueryLink();
        try {

            // Login
            String authorization = ReducerLogin.login(SourceLink.getQueryLink(), "admin", "admin");
            Header header = new Header(new Record("AUTHORIZATION", authorization));

            // Insert
            SourceLink.getCommandLink().execute(
                    header,
                    new Record[]{
                        Record.builder()
                                .entry("COLLECTION.KEY", "USERNAME")
                                .entry("ID.KEY", "newid")
                                .entry("NAME", "newuser")
                                .entry("DISPLAYNAME", "New User")
                                .entry("CODECARD", "123457")
                                .entry("ROLE_ID", "g")
                                .entry("VISIBLE", true)
                                .entry("ACTIVE", true).build()});
            Record r = loadUser(SourceLink.getQueryLink(), header, "newid");
            Assert.assertEquals("newuser", r.getString("NAME"));
            Assert.assertEquals("New User", r.getString("DISPLAYNAME"));
            Assert.assertEquals(Boolean.TRUE, r.getBoolean("VISIBLE"));

            // update
            SourceLink.getCommandLink().execute(
                    header,
                    new Record[]{
                        Record.builder()
                                .entry("COLLECTION.KEY", "USERNAME")
                                .entry("ID.KEY", "newid")
                                .entry("NAME", "newuser")
                                .entry("DISPLAYNAME", "New User Changed")
                                .entry("CODECARD", "12345")
                                .entry("ROLE_ID", "m")
                                .entry("VISIBLE", true)
                                .entry("ACTIVE", true)
                                .build()});

            r = loadUser(SourceLink.getQueryLink(), header, "newid");
            Assert.assertEquals("newuser", r.getString("NAME"));
            Assert.assertEquals("New User Changed", r.getString("DISPLAYNAME"));
            Assert.assertEquals(Boolean.TRUE, r.getBoolean("VISIBLE"));

            // Delete newid
            SourceLink.getCommandLink().execute(
                    header,
                    new Record[]{
                        Record.builder()
                                .entry("COLLECTION.KEY", "USERNAME")
                                .entry("ID.KEY", "newid")
                                .build()});

            r = loadUser(SourceLink.getQueryLink(), header, "newid");
            Assert.assertNull(r);

        } finally {
            SourceLink.destroyCommandQueryLink();
        }
    }

    private Record loadUser(Link querylink, Header header, String id) throws DataException {
        return querylink.find(header, Record.builder()
                .entry("COLLECTION.KEY", "USERNAME")
                .entry("ID.KEY", id)
                .entry("NAME", VariantString.NULL)
                .entry("DISPLAYNAME", VariantString.NULL)
                .entry("CODECARD", VariantString.NULL)
                .entry("ROLE_ID", VariantString.NULL)
                .entry("VISIBLE", VariantBoolean.NULL)
                .entry("ACTIVE", VariantBoolean.NULL)
                .build());
    }
}
