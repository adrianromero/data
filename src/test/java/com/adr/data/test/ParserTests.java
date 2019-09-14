//     Data Access is a Java library to store data
//     Copyright (C) 2017-2019 Adrián Romero Corchado.
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

import com.adr.data.var.VariantString;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import com.adr.data.record.Record;
import com.adr.data.recordparser.RecordsSerializer;
import com.adr.data.var.Kind;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantInt;
import com.adr.data.var.VariantLong;
import java.io.IOException;
import org.junit.Assert;

/**
 * @author adrian
 */
public class ParserTests {

    @Test
    public void recordsSerialization1() throws IOException {

        Record record = Record.builder()
                .entry("id", new VariantString("1"))
                .entry("field", new VariantString("pepeluis"))
                .entry("amount", new VariantInt(32))
                .entry("añejo", 32.0)
                .entry("unenter ito", 32)
                .entry("usvalid \nCon retorno", 32)
                .entry("unlong", new VariantLong(33L))
                .entry("value", VariantString.NULL)
                .build();
        String recordstr = "(id: \"1\", field: \"pepeluis\", amount: 32, añejo: 32.0, \"unenter ito\": 32, \"usvalid \\nCon retorno\": 32, unlong: 33:LONG, value: NULL)";

        Assert.assertEquals(recordstr, RecordsSerializer.write(record));
    }

    @Test
    public void recordsParseNumber() throws IOException {
        Record r = RecordsSerializer.read("(field : TRUE)");
        Assert.assertTrue(r.getBoolean("field"));

        r = RecordsSerializer.read("( \"field\" : 123.4  ) ");
        Assert.assertTrue(123.4 == r.getDouble("field"));

        r = RecordsSerializer.read("( \"field\" : \"123\":INT  ) ");
        Assert.assertTrue(123 == r.getInteger("field"));

        r = RecordsSerializer.read("( \"field\" : NULL : INT  ) ");
        Assert.assertEquals(Kind.INT, r.get("field").getKind());
        Assert.assertTrue(r.get("field").isNull());

        r = RecordsSerializer.read("( \"field\" : NULL   ) ");
        Assert.assertEquals(Kind.STRING, r.get("field").getKind());
        Assert.assertTrue(r.get("field").isNull());

        r = RecordsSerializer.read("( \"field\" : \"\"   ) ");
        Assert.assertEquals(Kind.STRING, r.get("field").getKind());
        Assert.assertEquals("", r.get("field").asString());
    }

    @Test
    public void recordsSerializationEmpty() throws IOException {
        Assert.assertEquals("()", RecordsSerializer.write(Record.EMPTY));
    }

    @Test
    public void recordsParseEmpty() throws IOException {
        Assert.assertEquals(Record.EMPTY, RecordsSerializer.read("()"));
    }

    @Test
    public void recordsSerializationList() throws IOException {

        List<Record> records = Arrays.asList(
                Record.builder()
                        .entry("id", "1")
                        .entry("field", "pepeluis")
                        .build(),
                Record.builder()
                        .entry("id", "2")
                        .entry("field", "hilario")
                        .build());
        String recordsstr = "(id: \"1\", field: \"pepeluis\")\n(id: \"2\", field: \"hilario\")";

        Assert.assertEquals(recordsstr, RecordsSerializer.writeList(records));
    }

    @Test
    public void recordsWithIdentifiers() throws IOException {
        Record r = Record.builder()
                .entry("COLLECTION.KEY", "USERNAME")
                .entry("ID.KEY", VariantString.NULL)
                .entry("NAME", VariantString.NULL)
                .entry("NAME..CONTAINS", "a")
                .entry("VISIBLE", VariantBoolean.NULL)
                .entry("CODECARD", VariantString.NULL)
                .entry("..ORDERBY", "NAME")
                .build();

        String s = RecordsSerializer.write(r);
        Record r2 = RecordsSerializer.read(s);
        String s2 = RecordsSerializer.write(r2);
        Assert.assertEquals(s, s2);
    }
}
