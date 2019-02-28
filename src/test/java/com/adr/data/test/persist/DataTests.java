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
import com.adr.data.record.Header;
import com.adr.data.security.ReducerLogin;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantString;
import org.junit.Assert;
import org.junit.Test;
import com.adr.data.record.Record;
import com.adr.data.test.SourceLink;
import com.adr.data.Link;

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
            Header header = new Header(new Record(Record.entry("AUTHORIZATION", authorization)));

            // Insert
            SourceLink.getCommandLink().execute(
                    header,
                    new Record[]{
                        new Record(
                                Record.entry("COLLECTION.KEY", "USERNAME"),
                                Record.entry("ID.KEY", "newid"),
                                Record.entry("NAME", "newuser"),
                                Record.entry("DISPLAYNAME", "New User"),
                                Record.entry("CODECARD", "123457"),
                                Record.entry("ROLE_ID", "g"),
                                Record.entry("VISIBLE", true),
                                Record.entry("ACTIVE", true))});
            Record r = loadUser(SourceLink.getQueryLink(), header, "newid");
            Assert.assertEquals("newuser", r.getString("NAME"));
            Assert.assertEquals("New User", r.getString("DISPLAYNAME"));
            Assert.assertEquals(Boolean.TRUE, r.getBoolean("VISIBLE"));

            // update
            SourceLink.getCommandLink().execute(
                    header,
                    new Record[]{
                        new Record(
                                Record.entry("COLLECTION.KEY", "USERNAME"),
                                Record.entry("ID.KEY", "newid"),
                                Record.entry("NAME", "newuser"),
                                Record.entry("DISPLAYNAME", "New User Changed"),
                                Record.entry("CODECARD", "12345"),
                                Record.entry("ROLE_ID", "m"),
                                Record.entry("VISIBLE", true),
                                Record.entry("ACTIVE", true))});

            r = loadUser(SourceLink.getQueryLink(), header, "newid");
            Assert.assertEquals("newuser", r.getString("NAME"));
            Assert.assertEquals("New User Changed", r.getString("DISPLAYNAME"));
            Assert.assertEquals(Boolean.TRUE, r.getBoolean("VISIBLE"));

            // Delete newid
            SourceLink.getCommandLink().execute(
                    header,
                    new Record[]{
                        new Record(
                                Record.entry("COLLECTION.KEY", "USERNAME"),
                                Record.entry("ID.KEY", "newid"))});

            r = loadUser(SourceLink.getQueryLink(), header, "newid");
            Assert.assertNull(r);

        } finally {
            SourceLink.destroyCommandQueryLink();
        }
    }

    private Record loadUser(Link querylink, Header header, String id) throws DataException {
        return querylink.find(header,
                new Record(
                        Record.entry("COLLECTION.KEY", "USERNAME"),
                        Record.entry("ID.KEY", id),
                        Record.entry("NAME", VariantString.NULL),
                        Record.entry("DISPLAYNAME", VariantString.NULL),
                        Record.entry("CODECARD", VariantString.NULL),
                        Record.entry("ROLE_ID", VariantString.NULL),
                        Record.entry("VISIBLE", VariantBoolean.NULL),
                        Record.entry("ACTIVE", VariantBoolean.NULL)));
    }
}
