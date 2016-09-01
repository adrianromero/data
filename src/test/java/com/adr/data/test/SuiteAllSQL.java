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
@Suite.SuiteClasses({com.adr.data.test.SuiteH2.class, com.adr.data.test.SuiteMYSQL.class, com.adr.data.test.SuitePostgreSQL.class})
public class SuiteAllSQL { 
}
