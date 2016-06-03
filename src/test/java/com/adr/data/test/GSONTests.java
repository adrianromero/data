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

import com.adr.data.DataList;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.RecordMap;
import com.adr.data.Kind;
import com.adr.data.ValuesMap;
import com.adr.data.ValuesEntry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adrian
 */
public class GSONTests {
    
    public GSONTests() {

    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }


     @Test
     public void hello() {
 
        RecordMap keval = new RecordMap(     
            new ValuesMap(
                    new ValuesEntry("id", Kind.STRING, "1")),
            new ValuesMap(
                    new ValuesEntry("field", Kind.STRING, "pepeluis"),
                    new ValuesEntry("value", Kind.STRING)));

        DataList dl = new DataList(
            new RecordMap(     
                new ValuesMap(
                        new ValuesEntry("id", Kind.STRING, "1")),
                new ValuesMap(
                        new ValuesEntry("field", Kind.STRING, "pepeluis"))),                
            new RecordMap(     
                new ValuesMap(
                        new ValuesEntry("id", Kind.STRING, "2")),
                new ValuesMap(
                        new ValuesEntry("field", Kind.STRING, "hilario"))));
        
        System.out.println(JSONSerializer.INSTANCE.toJSON(keval));  
        System.out.println(JSONSerializer.INSTANCE.toJSON(JSONSerializer.INSTANCE.fromJSONRecord(JSONSerializer.INSTANCE.toJSON(keval))));

        System.out.println(JSONSerializer.INSTANCE.toJSON(dl));
        System.out.println(JSONSerializer.INSTANCE.toJSON(JSONSerializer.INSTANCE.fromJSONDataList(JSONSerializer.INSTANCE.toJSON(dl))));      

        System.out.println(JSONSerializer.INSTANCE.toJSON(JSONSerializer.INSTANCE.fromJSONRecord(JSONSerializer.INSTANCE.toJSON(keval))));
        
        System.out.println(JSONSerializer.INSTANCE.toJSON(keval));
        System.out.println(JSONSerializer.INSTANCE.toSimpleJSON(keval));
        
        
    }
}
