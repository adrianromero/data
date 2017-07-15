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
import com.adr.data.DataQueryLink;
import com.adr.data.QueryLink;
import com.adr.data.recordmap.Entry;
import com.adr.data.record.Record;
import com.adr.data.record.Values;
import com.adr.data.recordmap.RecordMap;
import com.adr.data.recordmap.ValuesMap;
import com.adr.data.security.ReducerLogin;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantString;
import com.adr.data.var.VariantVoid;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author adrian
 */
public class QueryTests {

    public QueryTests() {
    }

    @Test
    public void testSomeQueries() throws DataException {

        DataQueryLink link = SourceLink.createDataQueryLink();
        try {

            // Login
            String authorization = ReducerLogin.login(link, "admin", "admin");
            Values header = new ValuesMap(new Entry("Authorization", authorization));

            // First query
            List<Record> result1 = link.query(
                    header,
                    new RecordMap(
                            new Entry[]{
                                new Entry("__ENTITY", "USERNAME"),
                                new Entry("ID", "admin")},
                            new Entry[]{
                                new Entry("NAME", VariantString.NULL),
                                new Entry("CODECARD", VariantString.NULL)}));

            Assert.assertEquals(1, result1.size());
            Assert.assertEquals("admin", result1.get(0).getString("NAME"));
            Assert.assertEquals(null, result1.get(0).getString("CODECARD"));
            Assert.assertEquals(VariantVoid.INSTANCE, result1.get(0).getValue().get("IMAGE"));

            List<Record> result2 = link.query(
                    header,
                    new RecordMap(
                            new Entry[]{
                                new Entry("__ENTITY", "USERNAME"),
                                new Entry("__ORDERBY", "NAME__DESC"),
                                new Entry("ID", VariantString.NULL)},
                            new Entry[]{
                                new Entry("NAME", VariantString.NULL),
                                new Entry("CODECARD", VariantString.NULL)}));

            Assert.assertEquals(3, result2.size());
            Assert.assertEquals("manager", result2.get(0).getString("NAME"));
            Assert.assertEquals("guest", result2.get(1).getString("NAME"));
            Assert.assertEquals("admin", result2.get(2).getString("NAME"));

            List<Record> result3 = link.query(
                    header,
                    new RecordMap(
                            new Entry[]{
                                new Entry("__ENTITY", "USERNAME"),
                                new Entry("ID", VariantString.NULL)},
                            new Entry[]{
                                new Entry("NAME", "manager"),
                                new Entry("VISIBLE", VariantBoolean.NULL),
                                new Entry("CODECARD", VariantString.NULL)}));
            Assert.assertEquals(1, result3.size());
            Assert.assertEquals("manager", result3.get(0).getString("NAME"));
            Assert.assertEquals(null, result3.get(0).getString("CODECARD"));
            Assert.assertEquals(VariantVoid.INSTANCE, result3.get(0).getValue().get("IMAGE"));

            List<Record> result4 = link.query(
                    header,
                    new RecordMap(
                            new Entry[]{
                                new Entry("__ENTITY", "USERNAME"),
                                new Entry("__ORDERBY", "NAME"),
                                new Entry("ID", VariantString.NULL)},
                            new Entry[]{
                                new Entry("NAME__LIKE", "%a%"),
                                new Entry("NAME", VariantString.NULL),
                                new Entry("VISIBLE", VariantBoolean.NULL),
                                new Entry("CODECARD", VariantString.NULL)}));
            Assert.assertEquals(2, result4.size());
            Assert.assertEquals("admin", result4.get(0).getString("NAME"));
            Assert.assertEquals("manager", result4.get(1).getString("NAME"));
        } finally {
            SourceLink.destroyDataQueryLink();
        }
    }

    @Test
    public void testSomeUpdates() throws DataException {

        DataQueryLink link = SourceLink.createDataQueryLink();
        try {

            // Login
            String authorization = ReducerLogin.login(link, "admin", "admin");
            Values header = new ValuesMap(new Entry("Authorization", authorization));

            // Insert
            link.execute(
                    header,
                    new RecordMap(
                            new Entry[]{
                                new Entry("__ENTITY", "USERNAME"),
                                new Entry("ID", "newid")},
                            new Entry[]{
                                new Entry("NAME", "newuser"),
                                new Entry("DISPLAYNAME", "New User"),
                                new Entry("CODECARD", "123457"),
                                new Entry("ROLE_ID", "g"),
                                new Entry("VISIBLE", true),
                                new Entry("ACTIVE", true)}));
            Record r = loadUser(link, header, "newid");
            Assert.assertEquals("newuser", r.getString("NAME"));
            Assert.assertEquals("New User", r.getString("DISPLAYNAME"));
            Assert.assertEquals(Boolean.TRUE, r.getBoolean("VISIBLE"));

            // Insert
            link.execute(
                    header,
                    new RecordMap(
                            new Entry[]{
                                new Entry("__ENTITY", "USERNAME"),
                                new Entry("ID", "newid")},
                            new Entry[]{
                                new Entry("NAME", "newuser"),
                                new Entry("DISPLAYNAME", "New User Changed"),
                                new Entry("CODECARD", "12345"),
                                new Entry("ROLE_ID", "m"),
                                new Entry("VISIBLE", true),
                                new Entry("ACTIVE", true)}));

            r = loadUser(link, header, "newid");
            Assert.assertEquals("newuser", r.getString("NAME"));
            Assert.assertEquals("New User Changed", r.getString("DISPLAYNAME"));
            Assert.assertEquals(Boolean.TRUE, r.getBoolean("VISIBLE"));
            
            // Delete newid
            link.execute(
                    header,
                    new RecordMap(
                            new Entry[]{
                                new Entry("__ENTITY", "USERNAME"),
                                new Entry("ID", "newid")}));

            r = loadUser(link, header, "newid");
            Assert.assertNull(r);            

        } finally {
            SourceLink.destroyDataQueryLink();
        }
    }

    private Record loadUser(QueryLink link, Values header, String id) throws DataException {
        return link.find(
                header,
                new RecordMap(
                        new Entry[]{
                            new Entry("__ENTITY", "USERNAME"),
                            new Entry("ID", id)},
                        new Entry[]{
                            new Entry("NAME", VariantString.NULL),
                            new Entry("DISPLAYNAME", VariantString.NULL),
                            new Entry("CODECARD", VariantString.NULL),
                            new Entry("ROLE_ID", VariantString.NULL),
                            new Entry("VISIBLE", VariantBoolean.NULL),
                            new Entry("ACTIVE", VariantBoolean.NULL)}));
    }
}
