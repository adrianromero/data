/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import com.adr.data.DataException;
import com.adr.data.Kind;
import com.adr.data.QueryLink;
import com.adr.data.RecordMap;
import com.adr.data.RecordMapSerializer;
import com.adr.data.ValuesMap;
import com.adr.data.ValuesEntry;
import com.adr.data.sql.SQLQueryLink;
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

        System.out.println(
            RecordMapSerializer.INSTANCE.toSimpleJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "user"),
                        new ValuesEntry("id", "admin")),
                    new ValuesMap(
                        new ValuesEntry("name", Kind.STRING),
                        new ValuesEntry("codecard", Kind.STRING))))));
        
        System.out.println(
            RecordMapSerializer.INSTANCE.toSimpleJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "user"),
                        new ValuesEntry("id", Kind.STRING)),
                    new ValuesMap(
                        new ValuesEntry("name", Kind.STRING),
                        new ValuesEntry("codecard", Kind.STRING))))));

        System.out.println(
            RecordMapSerializer.INSTANCE.toSimpleJSON(
                link.find(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "user"),
                        new ValuesEntry("id", "manager")),
                    new ValuesMap(
                        new ValuesEntry("name", Kind.STRING),
                        new ValuesEntry("visible", Kind.BOOLEAN),
                        new ValuesEntry("codecard", Kind.STRING))))));
        System.out.println(
            RecordMapSerializer.INSTANCE.toSimpleJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "permission_subject"),
                        new ValuesEntry("id", Kind.STRING)),
                    new ValuesMap(
                        new ValuesEntry("role_id", Kind.STRING),
                        new ValuesEntry("subject_name", Kind.STRING),
                        new ValuesEntry("subject_code", Kind.STRING))))));
        System.out.println(
            RecordMapSerializer.INSTANCE.toSimpleJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "subject_byrole"),
                        new ValuesEntry("role_id::PARAM", "m")),
                    new ValuesMap(
                        new ValuesEntry("code", Kind.STRING),
                        new ValuesEntry("name", Kind.STRING))))));
        System.out.println(
            RecordMapSerializer.INSTANCE.toSimpleJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "subject"),
                        new ValuesEntry("id")),
                    new ValuesMap(
                        new ValuesEntry("name::LIKE", "%o%"),
                        new ValuesEntry("name"))))));                    
                    
//                    also create a query ::LIKE because the write() will have to remove the :: 
        Assert.assertEquals("1 + 1 = 2", 2, 1 + 1);
    }
}
