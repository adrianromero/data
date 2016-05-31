/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
