/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.Record;

/**
 *
 * @author adrian
 */
public interface Query {
    
    public String getName();
    public CommandSQL buildSQLCommand(Record keyval);
    public boolean isField(String name);  
}
