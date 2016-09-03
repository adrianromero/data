/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author adrian
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    // GSON Suite
    GSONTests.class,
    // Cache suites
    // Database suites
    SuiteH2.class, 
    SuiteMYSQL.class, 
    SuitePostgreSQL.class})

public class SuiteAll { 
}
