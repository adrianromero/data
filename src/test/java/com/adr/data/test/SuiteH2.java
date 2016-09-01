/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import com.adr.data.testlinks.DataQueryLinkSQL;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author adrian
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({QueryTests.class, SecurityTests.class})
public class SuiteH2 {

    @BeforeClass
    public static void setUpClass() throws Exception {
        SourceLink.setBuilder(new DataQueryLinkSQL("h2"));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        SourceLink.setBuilder(null);
    }       
}
