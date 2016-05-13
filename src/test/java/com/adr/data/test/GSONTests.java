/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import com.adr.data.DataList;
import com.adr.data.JSONBuilder;
import com.adr.data.KeyValue;
import com.adr.data.Kind;
import com.adr.data.MapValue;
import com.adr.data.MapValueEntry;
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
 
        KeyValue keval = new KeyValue(     
            new MapValue(
                    new MapValueEntry("id", Kind.STRING, "1")),
            new MapValue(
                    new MapValueEntry("field", Kind.STRING, "pepeluis"),
                    new MapValueEntry("value", Kind.STRING)));
        
        
        DataList dl = new DataList(
            new KeyValue(     
                new MapValue(
                        new MapValueEntry("id", Kind.STRING, "1")),
                new MapValue(
                        new MapValueEntry("field", Kind.STRING, "pepeluis"))),                
            new KeyValue(     
                new MapValue(
                        new MapValueEntry("id", Kind.STRING, "2")),
                new MapValue(
                        new MapValueEntry("field", Kind.STRING, "hilario"))));
        
        System.out.println(JSONBuilder.INSTANCE.toJSON(keval));  
        System.out.println(JSONBuilder.INSTANCE.toJSON(JSONBuilder.INSTANCE.fromJSON(JSONBuilder.INSTANCE.toJSON(keval), KeyValue.class)));

        System.out.println(JSONBuilder.INSTANCE.toJSON(dl));
        System.out.println(JSONBuilder.INSTANCE.toJSON(JSONBuilder.INSTANCE.fromJSON(JSONBuilder.INSTANCE.toJSON(dl), DataList.class)));
        
        MapValueEntry mve = new MapValueEntry("field", Kind.STRING, "pepeluis");
        System.out.println(JSONBuilder.INSTANCE.toJSON(JSONBuilder.INSTANCE.fromJSON(JSONBuilder.INSTANCE.toJSON(mve), MapValueEntry.class)));
        
        MapValue mv = new MapValue(
            new MapValueEntry("chopped", Kind.STRING, "2"),
            new MapValueEntry("id", Kind.STRING, "2"));
        System.out.println(JSONBuilder.INSTANCE.toJSON(JSONBuilder.INSTANCE.fromJSON(JSONBuilder.INSTANCE.toJSON(mv), MapValue.class)));
        

        System.out.println(JSONBuilder.INSTANCE.toJSON(JSONBuilder.INSTANCE.fromJSON(JSONBuilder.INSTANCE.toJSON(keval), KeyValue.class)));
        
        
    }

}
