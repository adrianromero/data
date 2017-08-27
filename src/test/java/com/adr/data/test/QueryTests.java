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
public class QueryTests {

    private void testQueryByKey(QueryLink link, Record header) throws DataException {
        List<Record> result = link.query(
                header,
                new RecordMap(
                        new Entry("__ENTITY", "USERNAME"),
                        new Entry("ID$KEY", "admin"),
                        new Entry("NAME", VariantString.NULL),
                        new Entry("CODECARD", VariantString.NULL)));

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("USERNAME", result.get(0).getString("__ENTITY"));
        Assert.assertEquals("admin", result.get(0).getString("ID$KEY"));
        Assert.assertEquals("admin", result.get(0).getString("NAME"));
        Assert.assertEquals(null, result.get(0).getString("CODECARD"));
        Assert.assertEquals(VariantVoid.INSTANCE, result.get(0).get("IMAGE"));
    }

    private void testQueryOrder(QueryLink link, Record header) throws DataException {
        List<Record> result = link.query(header,
                new RecordMap(
                        new Entry("__ENTITY", "USERNAME"),
                        new Entry("__ORDERBY", "NAME$DESC"),
                        new Entry("ID$KEY", VariantString.NULL),
                        new Entry("NAME", VariantString.NULL),
                        new Entry("CODECARD", VariantString.NULL)));

        Assert.assertEquals(3, result.size());
        Assert.assertEquals("manager", result.get(0).getString("NAME"));
        Assert.assertEquals("guest", result.get(1).getString("NAME"));
        Assert.assertEquals("admin", result.get(2).getString("NAME"));
    }

    private void testSentenceQuery(QueryLink link, Record header) throws DataException {
        Record result = link.find(header,
                new RecordMap(
                        new Entry("__ENTITY", "USERNAME_BYNAME"),
                        new Entry("ID$KEY", VariantString.NULL),
                        new Entry("NAME", "guest"),
                        new Entry("DISPLAYNAME", VariantString.NULL),
                        new Entry("ROLE", VariantString.NULL),
                        new Entry("DISPLAYROLE", VariantString.NULL),
                        new Entry("PASSWORD", VariantString.NULL)
                ));

        Assert.assertEquals("Guest", result.getString("DISPLAYNAME"));
        Assert.assertEquals("GUEST", result.getString("ROLE"));
        Assert.assertEquals("Guest", result.getString("DISPLAYROLE"));
    }

    private void testSentenceView(QueryLink link, Record header) throws DataException {
        Record result = link.find(header,
                new RecordMap(
                        new Entry("__ENTITY", "TEST_USERNAME_VIEW"),
                        new Entry("ID$KEY", VariantString.NULL),
                        new Entry("NAME", "guest"),
                        new Entry("DISPLAYNAME", VariantString.NULL)));

        Assert.assertEquals("Guest", result.getString("DISPLAYNAME"));
    }

    @Test
    public void testSomeQueries() throws DataException {

        SourceLink.createDataQueryLink();
        try {
            // Login
            String authorization = ReducerLogin.login(SourceLink.getQueryLink(), "admin", "admin");
            Record header = new RecordMap(new Entry("Authorization", authorization));

            testQueryByKey(SourceLink.getQueryLink(), header);
            testQueryOrder(SourceLink.getQueryLink(), header);
            testSentenceQuery(SourceLink.getQueryLink(), header);
            testSentenceView(SourceLink.getQueryLink(), header);

            List<Record> result3 = SourceLink.getQueryLink().query(header,
                    new RecordMap(
                            new Entry("__ENTITY", "USERNAME"),
                            new Entry("ID$KEY", VariantString.NULL),
                            new Entry("NAME", "manager"),
                            new Entry("VISIBLE", VariantBoolean.NULL),
                            new Entry("CODECARD", VariantString.NULL)));
            Assert.assertEquals(1, result3.size());
            Assert.assertEquals("manager", result3.get(0).getString("NAME"));
            Assert.assertEquals(null, result3.get(0).getString("CODECARD"));
            Assert.assertEquals(VariantVoid.INSTANCE, result3.get(0).get("IMAGE"));

            List<Record> result4 = SourceLink.getQueryLink().query(header,
                    new RecordMap(
                            new Entry("__ENTITY", "USERNAME"),
                            new Entry("__ORDERBY", "NAME"),
                            new Entry("ID$KEY", VariantString.NULL),
                            new Entry("NAME", VariantString.NULL),
                            new Entry("NAME__LIKE", "%a%"),
                            new Entry("VISIBLE", VariantBoolean.NULL),
                            new Entry("CODECARD", VariantString.NULL)));
            Assert.assertEquals(2, result4.size());
            Assert.assertEquals("admin", result4.get(0).getString("NAME"));
            Assert.assertEquals("manager", result4.get(1).getString("NAME"));
        } finally {
            SourceLink.destroyDataQueryLink();
        }
    }

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
                                new Entry("ID$KEY", "newid"),
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
                                new Entry("ID$KEY", "newid"),
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
                                new Entry("ID$KEY", "newid"))});

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
                        new Entry("ID$KEY", id),
                        new Entry("NAME", VariantString.NULL),
                        new Entry("DISPLAYNAME", VariantString.NULL),
                        new Entry("CODECARD", VariantString.NULL),
                        new Entry("ROLE_ID", VariantString.NULL),
                        new Entry("VISIBLE", VariantBoolean.NULL),
                        new Entry("ACTIVE", VariantBoolean.NULL)));
    }
}
