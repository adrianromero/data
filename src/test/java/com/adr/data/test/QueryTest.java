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
import com.adr.data.QueryLink;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.ValuesMap;
import com.adr.data.ValuesEntry;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantBytes;
import com.adr.data.var.VariantString;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adrian
 */
public class QueryTest {

    public QueryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        DataSourceLink.setUpDB();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        DataSourceLink.tearDownDB();
    }
    
    @Test
    public void hello() throws DataException {

        QueryLink link = DataSourceLink.getQueryLink();

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
                link.find(new RecordMap(
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
                        new ValuesEntry("_ENTITY", "permission_subject"),
                        new ValuesEntry("id")),
                    new ValuesMap(
                        new ValuesEntry("role_id"),
                        new ValuesEntry("subject_name"),
                        new ValuesEntry("subject_code"))))));
        System.out.println(JSONSerializer.INSTANCE.toSimpleJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "subject_byrole"),
                        new ValuesEntry("role_id::PARAM", "m")),
                    new ValuesMap(
                        new ValuesEntry("code"),
                        new ValuesEntry("name"))))));
        System.out.println(JSONSerializer.INSTANCE.toSimpleJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "subject"),
                        new ValuesEntry("id")),
                    new ValuesMap(
                        new ValuesEntry("name::LIKE", "%o%"),
                        new ValuesEntry("name"))))));   
              
        System.out.println(JSONSerializer.INSTANCE.toSimpleJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "username_visible"),
                        new ValuesEntry("id")),
                    new ValuesMap(
                        new ValuesEntry("name"),
                        new ValuesEntry("displayname"),
                        new ValuesEntry("image", VariantBytes.NULL))))));
                    
//                    also create a query ::LIKE because the write() will have to remove the :: 
        Assert.assertEquals("1 + 1 = 2", 2, 1 + 1);
    }
}
