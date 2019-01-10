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
import com.adr.data.QueryLink;
import com.adr.data.record.Entry;
import com.adr.data.record.Header;
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

    private void testQueryByKey(QueryLink link, Header header) throws DataException {
        List<Record> result = link.query(
                header,
                new Record(
                        new Entry("COLLECTION.KEY", "USERNAME"),
                        new Entry("ID.KEY", "admin"),
                        new Entry("NAME", VariantString.NULL),
                        new Entry("CODECARD", VariantString.NULL)));

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("USERNAME", result.get(0).getString("COLLECTION.KEY"));
        Assert.assertEquals("admin", result.get(0).getString("ID.KEY"));
        Assert.assertEquals("admin", result.get(0).getString("NAME"));
        Assert.assertEquals(null, result.get(0).getString("CODECARD"));
        Assert.assertEquals(VariantVoid.INSTANCE, result.get(0).get("IMAGE"));
    }

    private void testQueryOrder(QueryLink link, Header header) throws DataException {
        List<Record> result = link.query(header,
                new Record(
                        new Entry("COLLECTION.KEY", "USERNAME"),
                        new Entry("ID.KEY", VariantString.NULL),
                        new Entry("NAME", VariantString.NULL),
                        new Entry("CODECARD", VariantString.NULL),
                        new Entry("..ORDERBY", "NAME$DESC")));

        Assert.assertEquals(3, result.size());
        Assert.assertEquals("manager", result.get(0).getString("NAME"));
        Assert.assertEquals("guest", result.get(1).getString("NAME"));
        Assert.assertEquals("admin", result.get(2).getString("NAME"));
    }

    private void testSentenceQuery(QueryLink link, Header header) throws DataException {
        Record result = link.find(header,
                new Record(
                        new Entry("COLLECTION.KEY", "USERNAME_BYNAME"),
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

    private void testSentenceView(QueryLink link, Header header) throws DataException {
        Record result = link.find(header,
                new Record(
                        new Entry("COLLECTION.KEY", "TEST_USERNAME_VIEW"),
                        new Entry("ID.KEY", VariantString.NULL),
                        new Entry("NAME", "guest"),
                        new Entry("DISPLAYNAME", VariantString.NULL)));

        Assert.assertEquals("Guest", result.getString("DISPLAYNAME"));
    }

    private void testSentenceTable(QueryLink link, Header header) throws DataException {
        List<Record> result3 = link.query(header,
                new Record(
                        new Entry("COLLECTION.KEY", "USERNAME"),
                        new Entry("ID.KEY", VariantString.NULL),
                        new Entry("NAME", "manager"),
                        new Entry("VISIBLE", VariantBoolean.NULL),
                        new Entry("CODECARD", VariantString.NULL)));
        Assert.assertEquals(1, result3.size());
        Assert.assertEquals("manager", result3.get(0).getString("NAME"));
        Assert.assertEquals(null, result3.get(0).getString("CODECARD"));
        Assert.assertEquals(VariantVoid.INSTANCE, result3.get(0).get("IMAGE"));
    }

    private void testSentenceTableContains(QueryLink link, Header header) throws DataException {
        List<Record> result4 = SourceLink.getQueryLink().query(header,
                new Record(
                        new Entry("COLLECTION.KEY", "USERNAME"),
                        new Entry("ID.KEY", VariantString.NULL),
                        new Entry("NAME", VariantString.NULL),
                        new Entry("NAME..CONTAINS", "a"),
                        new Entry("VISIBLE", VariantBoolean.NULL),
                        new Entry("CODECARD", VariantString.NULL),
                        new Entry("..ORDERBY", "NAME")));
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
            Header header = new Header(new Record(new Entry("AUTHORIZATION", authorization)));

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
