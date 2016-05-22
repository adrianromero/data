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
        DataSourceH2.setUpDB();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
         DataSourceH2.tearDownDB();
    }
    
    @Test
    public void hello() throws DataException {

        QueryLink link = new SQLQueryLink(DataSourceH2.get());

        System.out.println(
            RecordMapSerializer.INSTANCE.toJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", "003")),
                    new ValuesMap(
                        new ValuesEntry("name", Kind.STRING),
                        new ValuesEntry("countrycode", Kind.STRING))))));
        
        System.out.println(
            RecordMapSerializer.INSTANCE.toJSON(
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", Kind.STRING)),
                    new ValuesMap(
                        new ValuesEntry("name", Kind.STRING),
                        new ValuesEntry("countrycode", Kind.STRING))))));

        System.out.println(
            RecordMapSerializer.INSTANCE.toJSON(
                link.find(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", "004")),
                    new ValuesMap(
                        new ValuesEntry("name", Kind.STRING),
                        new ValuesEntry("countrycode", Kind.STRING))))));


        Assert.assertEquals("1 + 1 = 2", 2, 1 + 1);
    }
}
