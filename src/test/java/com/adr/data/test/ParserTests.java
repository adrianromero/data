//     Data Access is a Java library to store data
//     Copyright (C) 2017 Adrián Romero Corchado.
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

import com.adr.data.record.Entry;
import com.adr.data.record.RecordMap;
import com.adr.data.utils.JSON;
import com.adr.data.var.VariantString;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import com.adr.data.record.Record;
import com.adr.data.recordparser.RecordsSerializer;
import com.adr.data.var.VariantInt;
import com.adr.data.var.VariantLong;

import java.io.IOException;

/**
 * @author adrian
 */
public class ParserTests {

    @Test
    public void testJSONSerialization() throws IOException {

        Record keval = new RecordMap(
                new Entry("id", new VariantString("1")),
                new Entry("field", new VariantString("pepeluis")),
                new Entry("amount", new VariantInt(32)),
                new Entry("añejo", 32.0),
                new Entry("unenter ito", 32),
                new Entry("usvalid \nCon retorno", 32),
                new Entry("unlong", new VariantLong(33L)),
                new Entry("value", VariantString.NULL));

        List<Record> dl = Arrays.asList(
                new RecordMap(
                        new Entry("id", "1"),
                        new Entry("field", "pepeluis")),
                new RecordMap(
                        new Entry("id", "2"),
                        new Entry("field", "hilario")));

        System.out.println(JSON.INSTANCE.toJSON(RecordsSerializer.read(
                "(__ENTITY: \"NICA\\\"SO\", NAME.KEY: \"PEPE\":STRING, \"VALUE\": true, AMOUNT: 234.4, TOTAL: 123:INT, CUSTOMER: :DOUBLE, CHUNGGY: NULL:STRING, RARONULL: NUll )")));
        System.out.println(RecordsSerializer.write(keval));
    }
}
