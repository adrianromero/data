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
package com.adr.data.test;

import com.adr.data.DataException;
import com.adr.data.record.Header;
import com.adr.data.security.ReducerLogin;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantString;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import com.adr.data.record.Record;
import com.adr.data.Link;

/**
 *
 * @author adrian
 */
public class QueryTests {

    private void testQueryByKey(Link querylink, Header header) throws DataException {
        List<Record> result = querylink.query(
                header,
                Record.builder()
                        .entry("COLLECTION.KEY", "USERNAME")
                        .entry("ID.KEY", "admin")
                        .entry("NAME", VariantString.NULL)
                        .entry("CODECARD", VariantString.NULL)
                        .build());

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("USERNAME", result.get(0).getString("COLLECTION.KEY"));
        Assert.assertEquals("admin", result.get(0).getString("ID.KEY"));
        Assert.assertEquals("admin", result.get(0).getString("NAME"));
        Assert.assertNull(result.get(0).getString("CODECARD"));
        Assert.assertNull(result.get(0).get("IMAGE"));
    }

    private void testQueryOrder(Link querylink, Header header) throws DataException {
        List<Record> result = querylink.query(
                header,
                Record.builder()
                        .entry("COLLECTION.KEY", "USERNAME")
                        .entry("ID.KEY", VariantString.NULL)
                        .entry("NAME", VariantString.NULL)
                        .entry("CODECARD", VariantString.NULL)
                        .entry("..ORDERBY", "NAME$DESC")
                        .build());

        Assert.assertEquals(3, result.size());
        Assert.assertEquals("manager", result.get(0).getString("NAME"));
        Assert.assertEquals("guest", result.get(1).getString("NAME"));
        Assert.assertEquals("admin", result.get(2).getString("NAME"));
    }

    private void testSentenceQuery(Link querylink, Header header) throws DataException {
        Record result = querylink.find(
                header,
                Record.builder()
                        .entry("COLLECTION.KEY", "USERNAME_BYNAME")
                        .entry("NAME", "guest")
                        .entry("DISPLAYNAME", VariantString.NULL)
                        .entry("ROLE", VariantString.NULL)
                        .entry("DISPLAYROLE", VariantString.NULL)
                        .entry("PASSWORD", VariantString.NULL)
                        .build());

        Assert.assertEquals("Guest", result.getString("DISPLAYNAME"));
        Assert.assertEquals("GUEST", result.getString("ROLE"));
        Assert.assertEquals("Guest", result.getString("DISPLAYROLE"));
    }

    private void testSentenceView(Link querylink, Header header) throws DataException {
        Record result = querylink.find(
                header,
                Record.builder()
                        .entry("COLLECTION.KEY", "TEST_USERNAME_VIEW")
                        .entry("ID.KEY", VariantString.NULL)
                        .entry("NAME", "guest")
                        .entry("DISPLAYNAME", VariantString.NULL)
                        .build());

        Assert.assertEquals("Guest", result.getString("DISPLAYNAME"));
    }

    private void testSentenceTable(Link querylink, Header header) throws DataException {
        List<Record> result3 = querylink.query(
                header,
                Record.builder()
                        .entry("COLLECTION.KEY", "USERNAME")
                        .entry("ID.KEY", VariantString.NULL)
                        .entry("NAME", "manager")
                        .entry("VISIBLE", VariantBoolean.NULL)
                        .entry("CODECARD", VariantString.NULL)
                        .build());
        Assert.assertEquals(1, result3.size());
        Assert.assertEquals("manager", result3.get(0).getString("NAME"));
        Assert.assertEquals(null, result3.get(0).getString("CODECARD"));
        Assert.assertNull(result3.get(0).get("IMAGE"));
    }

    private void testSentenceTableContains(Link querylink, Header header) throws DataException {
        List<Record> result4 = querylink.query(
                header,
                Record.builder()
                        .entry("COLLECTION.KEY", "USERNAME")
                        .entry("ID.KEY", VariantString.NULL)
                        .entry("NAME", VariantString.NULL)
                        .entry("NAME..CONTAINS", "a")
                        .entry("VISIBLE", VariantBoolean.NULL)
                        .entry("CODECARD", VariantString.NULL)
                        .entry("..ORDERBY", "NAME")
                        .build());
        Assert.assertEquals(2, result4.size());
        Assert.assertEquals("admin", result4.get(0).getString("NAME"));
        Assert.assertEquals("manager", result4.get(1).getString("NAME"));
    }

    @Test
    public void testSomeQueries() throws DataException {

        SourceLink.createCommandQueryLink();
        try {
            // Login
            String authorization = ReducerLogin.login(SourceLink.getQueryLink(), "admin", "admin");
            Header header = new Header(new Record("AUTHORIZATION", authorization));

            testQueryByKey(SourceLink.getQueryLink(), header);
            testQueryOrder(SourceLink.getQueryLink(), header);
            testSentenceQuery(SourceLink.getQueryLink(), header);
            testSentenceView(SourceLink.getQueryLink(), header);
            testSentenceTable(SourceLink.getQueryLink(), header);
            testSentenceTableContains(SourceLink.getQueryLink(), header);
        } finally {
            SourceLink.destroyCommandQueryLink();
        }
    }
}
