//     Data Access is a Java library to store data
//     Copyright (C) 2018 Adrián Romero Corchado.
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
import org.junit.Test;
import com.adr.data.record.Record;
import com.adr.data.recordparser.RecordsSerializer;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantString;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;

/**
 *
 * @author adrian
 */
public class RecordsTests {

    @Test
    public void testSomeUpdates() throws DataException, IOException {

        SourceLink.createDataQueryLink();
        try {
            // Insert
            SourceLink.getDataLink().execute(
                    new Record[]{
                        new Record(
                                new Entry("COLLECTION.KEY", "USERNAME"),
                                new Entry("ID.KEY", "admin"),
                                new Entry("NAME", "admin"),
                                new Entry("DISPLAYNAME", "Administrator"),
                                new Entry("CODECARD", "123457"),
                                new Entry("ROLE_ID", "a"),
                                new Entry("VISIBLE", true),
                                new Entry("ACTIVE", true)),
                        new Record(
                                new Entry("COLLECTION.KEY", "USERNAME"),
                                new Entry("ID.KEY", "guest"),
                                new Entry("NAME", "guest"),
                                new Entry("DISPLAYNAME", "Guest"),
                                new Entry("CODECARD", "11111111"),
                                new Entry("ROLE_ID", "g"),
                                new Entry("VISIBLE", true),
                                new Entry("ACTIVE", true)),
                        new Record(
                                new Entry("COLLECTION.KEY", "USERNAME"),
                                new Entry("ID.KEY", "manager"),
                                new Entry("NAME", "manager"),
                                new Entry("DISPLAYNAME", "Manager"),
                                new Entry("CODECARD", "22121"),
                                new Entry("ROLE_ID", "m"),
                                new Entry("VISIBLE", true),
                                new Entry("ACTIVE", true)),
                        new Record(
                                new Entry("COLLECTION.KEY", "USERNAME"),
                                new Entry("ID.KEY", "developer"),
                                new Entry("NAME", "developer"),
                                new Entry("DISPLAYNAME", "Developer"),
                                new Entry("CODECARD", "666666"),
                                new Entry("ROLE_ID", "m"),
                                new Entry("VISIBLE", false),
                                new Entry("ACTIVE", true))});
            
            List<Record> result = SourceLink.getQueryLink().query(
                        new Record(
                                new Entry("COLLECTION.KEY", "USERNAME"),
                                new Entry("ID.KEY", VariantString.NULL),
                                new Entry("NAME", VariantString.NULL),
                                new Entry("DISPLAYNAME", VariantString.NULL),
                                new Entry("CODECARD", VariantString.NULL),
                                new Entry("ROLE_ID", VariantString.NULL),
                                new Entry("VISIBLE", VariantBoolean.NULL),
                                new Entry("ACTIVE", VariantBoolean.NULL)));     
            
            
            Assert.assertEquals("(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"admin\", NAME: \"admin\", DISPLAYNAME: \"Administrator\", CODECARD: \"123457\", ROLE_ID: \"a\", VISIBLE: true, ACTIVE: true)\n" +
                                "(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"guest\", NAME: \"guest\", DISPLAYNAME: \"Guest\", CODECARD: \"11111111\", ROLE_ID: \"g\", VISIBLE: true, ACTIVE: true)\n" +
                                "(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"manager\", NAME: \"manager\", DISPLAYNAME: \"Manager\", CODECARD: \"22121\", ROLE_ID: \"m\", VISIBLE: true, ACTIVE: true)\n" +
                                "(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"developer\", NAME: \"developer\", DISPLAYNAME: \"Developer\", CODECARD: \"666666\", ROLE_ID: \"m\", VISIBLE: false, ACTIVE: true)", 
                    RecordsSerializer.writeList(result));
           
            List<Record> result2 = SourceLink.getQueryLink().query(
                        new Record(
                                new Entry("COLLECTION.KEY", "USERNAME"),
                                new Entry("ID.KEY", VariantString.NULL),
                                new Entry("NAME", "guest"),
                                new Entry("DISPLAYNAME", VariantString.NULL),
                                new Entry("CODECARD", VariantString.NULL),
                                new Entry("ROLE_ID", VariantString.NULL),
                                new Entry("VISIBLE", VariantBoolean.NULL),
                                new Entry("ACTIVE", VariantBoolean.NULL)));     

            Assert.assertEquals("(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"guest\", NAME: \"guest\", DISPLAYNAME: \"Guest\", CODECARD: \"11111111\", ROLE_ID: \"g\", VISIBLE: true, ACTIVE: true)",
                    RecordsSerializer.writeList(result2));


        } finally {
            SourceLink.destroyDataQueryLink();
        }
    }
}
