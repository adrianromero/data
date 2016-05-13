/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.DataList;
import com.adr.data.KeyValue;
import com.adr.data.MapValue;
import com.adr.data.MapValueEntry;
import com.adr.data.sql.SQLLink;
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
     public void hello() throws DataException {
         
        DataLink link = new SQLLink(PGTestSuite.getDataSource());
//
        link.execute(new DataList(
            new KeyValue(     
                new MapValue(
                        new MapValueEntry("_ENTITY", "c_country"),
                        new MapValueEntry("c_country_id", "003")),
                new MapValue(
                        new MapValueEntry("name", "Alemania"),
                        new MapValueEntry("countrycode", "DE"))),
            new KeyValue(     
                new MapValue(
                        new MapValueEntry("_ENTITY", "c_country"),
                        new MapValueEntry("c_country_id", "004")),
                new MapValue(
                        new MapValueEntry("name", "Italia"),
                        new MapValueEntry("countrycode", "IT"))),
            new KeyValue(     
                new MapValue(
                        new MapValueEntry("_ENTITY", "c_country"),
                        new MapValueEntry("c_country_id", "005")),
                new MapValue(
                        new MapValueEntry("name", "Portugal"),
                        new MapValueEntry("hasRegion", "N"),
                        new MapValueEntry("countrycode", "PT")))));
//        link.execute(new KeyValue(     
//            new MapValue(
//                    new MapValueEntry("_ENTITY", "c_country"),
//                    new MapValueEntry("c_country_id", "003")),
//            null));        
   
         Assert.assertEquals("1 + 1 = 2", 2, 1 + 1);
     }
}
