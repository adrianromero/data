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
                        Record.builder()
                                .entry("COLLECTION.KEY", "USERNAME")
                                .entry("ID.KEY", "admin")
                                .entry("NAME", "admin")
                                .entry("DISPLAYNAME", "Administrator")
                                .entry("CODECARD", "123457")
                                .entry("ROLE_ID", "a")
                                .entry("VISIBLE", true)
                                .entry("ACTIVE", true)
                                .build(),
                        Record.builder()
                                .entry("COLLECTION.KEY", "USERNAME")
                                .entry("ID.KEY", "guest")
                                .entry("NAME", "guest")
                                .entry("DISPLAYNAME", "Guest")
                                .entry("CODECARD", "11111111")
                                .entry("ROLE_ID", "g")
                                .entry("VISIBLE", true)
                                .entry("ACTIVE", true)
                                .build(),
                        Record.builder()
                                .entry("COLLECTION.KEY", "USERNAME")
                                .entry("ID.KEY", "manager")
                                .entry("NAME", "manager")
                                .entry("DISPLAYNAME", "Manager")
                                .entry("CODECARD", "22121")
                                .entry("ROLE_ID", "m")
                                .entry("VISIBLE", true)
                                .entry("ACTIVE", true)
                                .build(),
                        Record.builder()
                                .entry("COLLECTION.KEY", "USERNAME")
                                .entry("ID.KEY", "developer")
                                .entry("NAME", "developer")
                                .entry("DISPLAYNAME", "Developer")
                                .entry("CODECARD", "666666")
                                .entry("ROLE_ID", "m")
                                .entry("VISIBLE", false)
                                .entry("ACTIVE", true)
                                .build()});

            List<Record> result = SourceLink.getQueryLink().query(
                    Record.builder()
                            .entry("COLLECTION.KEY", "USERNAME")
                            .entry("ID.KEY", VariantString.NULL)
                            .entry("NAME", VariantString.NULL)
                            .entry("DISPLAYNAME", VariantString.NULL)
                            .entry("CODECARD", VariantString.NULL)
                            .entry("ROLE_ID", VariantString.NULL)
                            .entry("VISIBLE", VariantBoolean.NULL)
                            .entry("ACTIVE", VariantBoolean.NULL)
                            .build());

            Assert.assertEquals("(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"admin\", NAME: \"admin\", DISPLAYNAME: \"Administrator\", CODECARD: \"123457\", ROLE_ID: \"a\", VISIBLE: true, ACTIVE: true)\n"
                    + "(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"guest\", NAME: \"guest\", DISPLAYNAME: \"Guest\", CODECARD: \"11111111\", ROLE_ID: \"g\", VISIBLE: true, ACTIVE: true)\n"
                    + "(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"manager\", NAME: \"manager\", DISPLAYNAME: \"Manager\", CODECARD: \"22121\", ROLE_ID: \"m\", VISIBLE: true, ACTIVE: true)\n"
                    + "(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"developer\", NAME: \"developer\", DISPLAYNAME: \"Developer\", CODECARD: \"666666\", ROLE_ID: \"m\", VISIBLE: false, ACTIVE: true)",
                    RecordsSerializer.writeList(result));

            List<Record> result2 = SourceLink.getQueryLink().query(
                    Record.builder()
                            .entry("COLLECTION.KEY", "USERNAME")
                            .entry("ID.KEY", VariantString.NULL)
                            .entry("NAME", "guest")
                            .entry("DISPLAYNAME", VariantString.NULL)
                            .entry("CODECARD", VariantString.NULL)
                            .entry("ROLE_ID", VariantString.NULL)
                            .entry("VISIBLE", VariantBoolean.NULL)
                            .entry("ACTIVE", VariantBoolean.NULL)
                            .build());

            Assert.assertEquals("(COLLECTION.KEY: \"USERNAME\", ID.KEY: \"guest\", NAME: \"guest\", DISPLAYNAME: \"Guest\", CODECARD: \"11111111\", ROLE_ID: \"g\", VISIBLE: true, ACTIVE: true)",
                    RecordsSerializer.writeList(result2));

        } finally {
            SourceLink.destroyCommandQueryLink();
        }
    }
}
