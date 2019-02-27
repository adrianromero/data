//     Data Access is a Java library to store data
//     Copyright (C) 2019 Adrián Romero Corchado.
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

        SourceLink.createCommandQueryLink();
        try {
            // Insert
            SourceLink.getCommandLink().execute(
                    new Record[]{
                        new Record(
                                Record.entry("COLLECTION.KEY", "USERNAME"),
                                Record.entry("ID.KEY", "admin"),
                                Record.entry("NAME", "admin"),
                                Record.entry("DISPLAYNAME", "Administrator"),
                                Record.entry("CODECARD", "123457"),
                                Record.entry("ROLE_ID", "a"),
                                Record.entry("VISIBLE", true),
                                Record.entry("ACTIVE", true)),
                        new Record(
                                Record.entry("COLLECTION.KEY", "USERNAME"),
                                Record.entry("ID.KEY", "guest"),
                                Record.entry("NAME", "guest"),
                                Record.entry("DISPLAYNAME", "Guest"),
                                Record.entry("CODECARD", "11111111"),
                                Record.entry("ROLE_ID", "g"),
                                Record.entry("VISIBLE", true),
                                Record.entry("ACTIVE", true)),
                        new Record(
                                Record.entry("COLLECTION.KEY", "USERNAME"),
                                Record.entry("ID.KEY", "manager"),
                                Record.entry("NAME", "manager"),
                                Record.entry("DISPLAYNAME", "Manager"),
                                Record.entry("CODECARD", "22121"),
                                Record.entry("ROLE_ID", "m"),
                                Record.entry("VISIBLE", true),
                                Record.entry("ACTIVE", true)),
                        new Record(
                                Record.entry("COLLECTION.KEY", "USERNAME"),
                                Record.entry("ID.KEY", "developer"),
                                Record.entry("NAME", "developer"),
                                Record.entry("DISPLAYNAME", "Developer"),
                                Record.entry("CODECARD", "666666"),
                                Record.entry("ROLE_ID", "m"),
                                Record.entry("VISIBLE", false),
                                Record.entry("ACTIVE", true))});
            
            List<Record> result = SourceLink.getQueryLink().query(
                        new Record(
                                Record.entry("COLLECTION.KEY", "USERNAME"),
                                Record.entry("ID.KEY", VariantString.NULL),
                                Record.entry("NAME", VariantString.NULL),
                                Record.entry("DISPLAYNAME", VariantString.NULL),
                                Record.entry("CODECARD", VariantString.NULL),
                                Record.entry("ROLE_ID", VariantString.NULL),
                                Record.entry("VISIBLE", VariantBoolean.NULL),
                                Record.entry("ACTIVE", VariantBoolean.NULL)));     
            
            
            Assert.assertEquals("(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"admin\", NAME: \"admin\", DISPLAYNAME: \"Administrator\", CODECARD: \"123457\", ROLE_ID: \"a\", VISIBLE: true, ACTIVE: true)\n" +
                                "(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"guest\", NAME: \"guest\", DISPLAYNAME: \"Guest\", CODECARD: \"11111111\", ROLE_ID: \"g\", VISIBLE: true, ACTIVE: true)\n" +
                                "(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"manager\", NAME: \"manager\", DISPLAYNAME: \"Manager\", CODECARD: \"22121\", ROLE_ID: \"m\", VISIBLE: true, ACTIVE: true)\n" +
                                "(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"developer\", NAME: \"developer\", DISPLAYNAME: \"Developer\", CODECARD: \"666666\", ROLE_ID: \"m\", VISIBLE: false, ACTIVE: true)", 
                    RecordsSerializer.writeList(result));
           
            List<Record> result2 = SourceLink.getQueryLink().query(
                        new Record(
                                Record.entry("COLLECTION.KEY", "USERNAME"),
                                Record.entry("ID.KEY", VariantString.NULL),
                                Record.entry("NAME", "guest"),
                                Record.entry("DISPLAYNAME", VariantString.NULL),
                                Record.entry("CODECARD", VariantString.NULL),
                                Record.entry("ROLE_ID", VariantString.NULL),
                                Record.entry("VISIBLE", VariantBoolean.NULL),
                                Record.entry("ACTIVE", VariantBoolean.NULL)));     

            Assert.assertEquals("(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"guest\", NAME: \"guest\", DISPLAYNAME: \"Guest\", CODECARD: \"11111111\", ROLE_ID: \"g\", VISIBLE: true, ACTIVE: true)",
                    RecordsSerializer.writeList(result2));


        } finally {
            SourceLink.destroyCommandQueryLink();
        }
    }
}
