//     Data Access is a Java library to store data
//     Copyright (C) 2016-2017 Adrián Romero Corchado.
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
import com.adr.data.QueryLink;
import com.adr.data.record.Entry;
import com.adr.data.record.RecordMap;
import com.adr.data.security.ReducerLogin;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantString;
import com.adr.data.var.VariantVoid;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class DataTests {

    @Test
    public void testSomeUpdates() throws DataException {

        SourceLink.createDataQueryLink();
        try {

            // Login
            String authorization = ReducerLogin.login(SourceLink.getQueryLink(), "admin", "admin");
            Record header = new RecordMap(new Entry("Authorization", authorization));

            // Insert
            SourceLink.getDataLink().execute(
                    header,
                    new Record[]{
                        new RecordMap(
                                new Entry("__ENTITY", "USERNAME"),
                                new Entry("ID.KEY", "newid"),
                                new Entry("NAME", "newuser"),
                                new Entry("DISPLAYNAME", "New User"),
                                new Entry("CODECARD", "123457"),
                                new Entry("ROLE_ID", "g"),
                                new Entry("VISIBLE", true),
                                new Entry("ACTIVE", true))});
            Record r = loadUser(SourceLink.getQueryLink(), header, "newid");
            Assert.assertEquals("newuser", r.getString("NAME"));
            Assert.assertEquals("New User", r.getString("DISPLAYNAME"));
            Assert.assertEquals(Boolean.TRUE, r.getBoolean("VISIBLE"));

            // update
            SourceLink.getDataLink().execute(
                    header,
                    new Record[]{
                        new RecordMap(
                                new Entry("__ENTITY", "USERNAME"),
                                new Entry("ID.KEY", "newid"),
                                new Entry("NAME", "newuser"),
                                new Entry("DISPLAYNAME", "New User Changed"),
                                new Entry("CODECARD", "12345"),
                                new Entry("ROLE_ID", "m"),
                                new Entry("VISIBLE", true),
                                new Entry("ACTIVE", true))});

            r = loadUser(SourceLink.getQueryLink(), header, "newid");
            Assert.assertEquals("newuser", r.getString("NAME"));
            Assert.assertEquals("New User Changed", r.getString("DISPLAYNAME"));
            Assert.assertEquals(Boolean.TRUE, r.getBoolean("VISIBLE"));

            // Delete newid
            SourceLink.getDataLink().execute(
                    header,
                    new Record[]{
                        new RecordMap(
                                new Entry("__ENTITY", "USERNAME"),
                                new Entry("ID.KEY", "newid"))});

            r = loadUser(SourceLink.getQueryLink(), header, "newid");
            Assert.assertNull(r);

        } finally {
            SourceLink.destroyDataQueryLink();
        }
    }

    private Record loadUser(QueryLink link, Record header, String id) throws DataException {
        return link.find(header,
                new RecordMap(
                        new Entry("__ENTITY", "USERNAME"),
                        new Entry("ID.KEY", id),
                        new Entry("NAME", VariantString.NULL),
                        new Entry("DISPLAYNAME", VariantString.NULL),
                        new Entry("CODECARD", VariantString.NULL),
                        new Entry("ROLE_ID", VariantString.NULL),
                        new Entry("VISIBLE", VariantBoolean.NULL),
                        new Entry("ACTIVE", VariantBoolean.NULL)));
    }
}
