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
package com.adr.data.test;

import com.adr.data.DataException;
import com.adr.data.DataQueryLink;
import com.adr.data.QueryLink;
import com.adr.data.QueryOptions;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.utils.JSON;
import com.adr.data.ValuesMap;
import com.adr.data.ValuesEntry;
import com.adr.data.security.SecureFacade;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantString;
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

        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            SecureFacade secfac = new SecureFacade(link);
            secfac.login("admin", "admin");
            
            List<Record> result1 = link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "USERNAME"),
                        new ValuesEntry("ID", new VariantString("admin"))),
                    new ValuesMap(
                        new ValuesEntry("NAME", VariantString.NULL),
                        new ValuesEntry("CODECARD", VariantString.NULL))));
            
            Assert.assertEquals(1, result1.size());
            Assert.assertEquals("admin", result1.get(0).getString("NAME"));
            Assert.assertEquals(null, result1.get(0).getString("CODECARD"));
            Assert.assertEquals(null, result1.get(0).getValue().get("IMAGE"));
            
            List<Record> result2 = link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "USERNAME"),
                        new ValuesEntry("ID", VariantString.NULL)),
                    new ValuesMap(
                        new ValuesEntry("NAME", VariantString.NULL),
                        new ValuesEntry("CODECARD", VariantString.NULL))),
                    QueryOptions.orderBy("NAME::DESC"));
                
            Assert.assertEquals(3, result2.size());
            Assert.assertEquals("user", result2.get(0).getString("NAME"));            
            Assert.assertEquals("manager", result2.get(1).getString("NAME"));            
            Assert.assertEquals("admin", result2.get(2).getString("NAME"));   

            System.out.println("3.- " + JSON.INSTANCE.toSimpleJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "USERNAME"),
                        new ValuesEntry("ID", "manager")),
                    new ValuesMap(
                        new ValuesEntry("NAME", VariantString.NULL),
                        new ValuesEntry("VISIBLE", VariantBoolean.NULL),
                        new ValuesEntry("CODECARD", VariantString.NULL))))));

            System.out.println("4.- " + JSON.INSTANCE.toSimpleJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "USERNAME"),
                        new ValuesEntry("ID", VariantString.NULL)),
                    new ValuesMap(
                        new ValuesEntry("NAME::LIKE", "%a%"),
                        new ValuesEntry("VISIBLE", VariantBoolean.NULL),
                        new ValuesEntry("CODECARD", VariantString.NULL))))));
            secfac.logout();
        }
    }

    @Test
    public void testSomeUpdates() throws DataException {
        
        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            SecureFacade secfac = new SecureFacade(link);
            secfac.login("admin", "admin");

            // Insert
            link.execute(new RecordMap(
                new ValuesMap(
                    new ValuesEntry("_ENTITY", "USERNAME"),
                    new ValuesEntry("ID", "newid1")),
                new ValuesMap(
                    new ValuesEntry("NAME", "newuser"),
                    new ValuesEntry("DISPLAYNAME", "New User"),
                    new ValuesEntry("CODECARD", "12345"),
                    new ValuesEntry("ROLE_ID", "u"),
                    new ValuesEntry("VISIBLE", true),
                    new ValuesEntry("ACTIVE", true))));

            Record r = getUser(link, "newid1");
            Assert.assertEquals("newuser", r.getString("NAME"));
            Assert.assertEquals("New User", r.getString("DISPLAYNAME"));
            Assert.assertEquals(Boolean.TRUE, r.getBoolean("VISIBLE"));

            // Insert
            link.execute(new RecordMap(
                new ValuesMap(
                    new ValuesEntry("_ENTITY", "USERNAME"),
                    new ValuesEntry("ID", "newid1")),
                new ValuesMap(
                    new ValuesEntry("NAME", "newuser"),
                    new ValuesEntry("DISPLAYNAME", "New User Changed"),
                    new ValuesEntry("CODECARD", "12345"),
                    new ValuesEntry("ROLE_ID", "u"),
                    new ValuesEntry("VISIBLE", true),
                    new ValuesEntry("ACTIVE", true))));

            r = getUser(link, "newid1");
            Assert.assertEquals("newuser", r.getString("NAME"));
            Assert.assertEquals("New User Changed", r.getString("DISPLAYNAME"));
            Assert.assertEquals(Boolean.TRUE, r.getBoolean("VISIBLE"));

            // Delete
            link.execute(new RecordMap(
                new ValuesMap(
                    new ValuesEntry("_ENTITY", "USERNAME"),
                    new ValuesEntry("ID", "newid1"))));

            r = getUser(link, "newid1");
            Assert.assertNull(r);
            
            secfac.logout();
        }
    }

    private Record getUser(QueryLink link, String id) throws DataException {
        return link.find(new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "USERNAME"),
                new ValuesEntry("ID", id)),
            new ValuesMap(
                new ValuesEntry("NAME", VariantString.NULL),
                new ValuesEntry("DISPLAYNAME", VariantString.NULL),
                new ValuesEntry("CODECARD", VariantString.NULL),
                new ValuesEntry("ROLE_ID", VariantString.NULL),
                new ValuesEntry("VISIBLE", VariantBoolean.NULL),
                new ValuesEntry("ACTIVE", VariantBoolean.NULL))));
    }
}
