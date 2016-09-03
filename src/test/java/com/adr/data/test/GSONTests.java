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

import com.adr.data.QueryOptions;
import com.adr.data.Record;
import com.adr.data.utils.JSON;
import com.adr.data.RecordMap;
import com.adr.data.ValuesMap;
import com.adr.data.ValuesEntry;
import com.adr.data.var.VariantString;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author adrian
 */
public class GSONTests {
    
    public GSONTests() {
    }
    
    @Test
    public void testJSONSerialization() {
 
        RecordMap keval = new RecordMap(     
            new ValuesMap(
                    new ValuesEntry("id", new VariantString("1"))),
            new ValuesMap(
                    new ValuesEntry("field", new VariantString("pepeluis")),
                    new ValuesEntry("value", VariantString.NULL)));

        List<Record> dl = Arrays.asList(
            new RecordMap(     
                new ValuesMap(
                        new ValuesEntry("id", "1")),
                new ValuesMap(
                        new ValuesEntry("field", "pepeluis"))),                
            new RecordMap(     
                new ValuesMap(
                        new ValuesEntry("id", "2")),
                new ValuesMap(
                        new ValuesEntry("field", "hilario"))));
        
        System.out.println(JSON.INSTANCE.toJSON(keval));  
        System.out.println(JSON.INSTANCE.toJSON(JSON.INSTANCE.fromJSONRecord(JSON.INSTANCE.toJSON(keval))));

        System.out.println(JSON.INSTANCE.toJSON(dl));
        System.out.println(JSON.INSTANCE.toJSON(JSON.INSTANCE.fromJSONListRecord(JSON.INSTANCE.toJSON(dl))));      

        System.out.println(JSON.INSTANCE.toJSON(JSON.INSTANCE.fromJSONRecord(JSON.INSTANCE.toJSON(keval))));
        
        System.out.println(JSON.INSTANCE.toJSON(keval));
        System.out.println(JSON.INSTANCE.toSimpleJSON(keval));
    }
     
    @Test
    public void testQueryOptions() {        
        Assert.assertEquals("{\"limit\":1}", JSON.INSTANCE.toJSONElement(QueryOptions.FIND).toString());
        Assert.assertEquals("null", JSON.INSTANCE.toJSONElement(QueryOptions.DEFAULT).toString());
        Assert.assertEquals("{\"limit\":100,\"offset\":10}", JSON.INSTANCE.toJSONElement(QueryOptions.limit(100, 10)).toString());
        Assert.assertEquals("null", JSON.INSTANCE.toJSONElement(QueryOptions.limit(Integer.MAX_VALUE)).toString());
        Assert.assertEquals("{\"orderby\":[\"COLUMN1\",\"COLUMN2\"]}", JSON.INSTANCE.toJSONElement(QueryOptions.orderBy("COLUMN1", "COLUMN2")).toString());
    }
}
