/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.DataList;
import com.adr.data.Kind;
import com.adr.data.RecordMap;
import com.adr.data.ValuesMap;
import com.adr.data.ValuesEntry;
import com.adr.data.sql.SQLDataLink;
import com.adr.data.sqlh2.SentenceH2Put;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author adrian
 */
public class ProcessTests {
    
    public ProcessTests() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        DataSourceLink.setUpDB();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        DataSourceLink.tearDownDB();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
     @Test
     public void hello() throws DataException {
         
        DataLink link = DataSourceLink.getDataLink();

        link.execute(new DataList(
            new RecordMap(     
                new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", "001")),
                new ValuesMap(
                        new ValuesEntry("name", "Spain"),
                        new ValuesEntry("hasRegion", Kind.BOOLEAN, true),                    
                        new ValuesEntry("countrycode", "ES"))),
            new RecordMap(     
                new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", "003")),
                new ValuesMap(
                        new ValuesEntry("name", "Germany"),
                        new ValuesEntry("hasRegion", Kind.BOOLEAN, true),                    
                        new ValuesEntry("countrycode", "DE"))),
            new RecordMap(     
                new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", "004")),
                new ValuesMap(
                        new ValuesEntry("name", "Italy"),
                        new ValuesEntry("hasRegion", Kind.BOOLEAN, true),                    
                        new ValuesEntry("countrycode", "IT"))),
            new RecordMap(     
                new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", "005")),
                new ValuesMap(
                        new ValuesEntry("name", "Portugal"),
                        new ValuesEntry("hasRegion", Kind.BOOLEAN, true),
                        new ValuesEntry("countrycode", "PT")))));     
   
         Assert.assertEquals("1 + 1 = 2", 2, 1 + 1);
     }
}
