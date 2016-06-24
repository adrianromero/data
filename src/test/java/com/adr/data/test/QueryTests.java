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
import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.ValuesMap;
import com.adr.data.ValuesEntry;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantString;
import junit.framework.Assert;
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

        QueryLink link = SourceLink.getQueryLink();

        System.out.println(JSONSerializer.INSTANCE.toSimpleJSON(
            link.query(new RecordMap(
                new ValuesMap(
                    new ValuesEntry("_ENTITY", "username"),
                    new ValuesEntry("id", new VariantString("admin"))),
                new ValuesMap(
                    new ValuesEntry("name", VariantString.NULL),
                    new ValuesEntry("codecard", VariantString.NULL))))));

        System.out.println(JSONSerializer.INSTANCE.toSimpleJSON(
            link.query(new RecordMap(
                new ValuesMap(
                    new ValuesEntry("_ENTITY", "username"),
                    new ValuesEntry("id", VariantString.NULL)),
                new ValuesMap(
                    new ValuesEntry("name", VariantString.NULL),
                    new ValuesEntry("codecard", VariantString.NULL))))));

        System.out.println(JSONSerializer.INSTANCE.toSimpleJSON(
            link.query(new RecordMap(
                new ValuesMap(
                    new ValuesEntry("_ENTITY", "username"),
                    new ValuesEntry("id", "manager")),
                new ValuesMap(
                    new ValuesEntry("name", VariantString.NULL),
                    new ValuesEntry("visible", VariantBoolean.NULL),
                    new ValuesEntry("codecard", VariantString.NULL))))));
        
        System.out.println(JSONSerializer.INSTANCE.toSimpleJSON(
            link.query(new RecordMap(
                new ValuesMap(
                    new ValuesEntry("_ENTITY", "username"),
                    new ValuesEntry("id", VariantString.NULL)),
                new ValuesMap(
                    new ValuesEntry("name::LIKE", "%a%"),
                    new ValuesEntry("visible", VariantBoolean.NULL),
                    new ValuesEntry("codecard", VariantString.NULL))))));
//                    also create a query ::LIKE because the write() will have to remove the :: 
    }

    @Test
    public void testSomeUpdates() throws DataException {
        DataLink datalink = SourceLink.getDataLink();
        QueryLink link = SourceLink.getQueryLink();

        // Insert
        datalink.execute(new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "username"),
                new ValuesEntry("id", "newid1")),
            new ValuesMap(
                new ValuesEntry("name", "newuser"),
                new ValuesEntry("displayname", "New User"),
                new ValuesEntry("codecard", "12345"),
                new ValuesEntry("role_id", "u"),
                new ValuesEntry("visible", true),
                new ValuesEntry("active", true))));

        Record r = getUser(link, "newid1");
        Assert.assertEquals("newuser", r.getString("name"));
        Assert.assertEquals("New User", r.getString("displayname"));
        Assert.assertEquals(Boolean.TRUE, r.getBoolean("visible"));

        // Insert
        datalink.execute(new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "username"),
                new ValuesEntry("id", "newid1")),
            new ValuesMap(
                new ValuesEntry("name", "newuser"),
                new ValuesEntry("displayname", "New User Changed"),
                new ValuesEntry("codecard", "12345"),
                new ValuesEntry("role_id", "u"),
                new ValuesEntry("visible", true),
                new ValuesEntry("active", true))));

        r = getUser(link, "newid1");
        Assert.assertEquals("newuser", r.getString("name"));
        Assert.assertEquals("New User Changed", r.getString("displayname"));
        Assert.assertEquals(Boolean.TRUE, r.getBoolean("visible"));

        // Delete
        datalink.execute(new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "username"),
                new ValuesEntry("id", "newid1"))));
        
        r = getUser(link, "newid1");
        Assert.assertNull(r);
    }

    private Record getUser(QueryLink link, String id) throws DataException {
        return link.find(new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "username"),
                new ValuesEntry("id", id)),
            new ValuesMap(
                new ValuesEntry("name", VariantString.NULL),
                new ValuesEntry("displayname", VariantString.NULL),
                new ValuesEntry("codecard", VariantString.NULL),
                new ValuesEntry("role_id", VariantString.NULL),
                new ValuesEntry("visible", VariantBoolean.NULL),
                new ValuesEntry("active", VariantBoolean.NULL))));
    }
//-- DROP TABLE USERNAME;
//CREATE TABLE USERNAME (
//    ID VARCHAR(32) NOT NULL,
//    NAME VARCHAR(1024) NOT NULL,
//    DISPLAYNAME VARCHAR(2024) NOT NULL,
//    PASSWORD VARCHAR(1024),
//    CODECARD VARCHAR(1024),
//    ROLE_ID VARCHAR(32) NOT NULL,
//    VISIBLE BOOLEAN NOT NULL DEFAULT TRUE,
//    ACTIVE BOOLEAN,
//    IMAGE BYTEA,
//    PRIMARY KEY (ID),
//    FOREIGN KEY (ROLE_ID) REFERENCES ROLE(ID)
//);
}
