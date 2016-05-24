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
public class SentenceTable extends SentenceSelect {
    
    @Override
    public String getName() {
        return "TABLE";
    }

    @Override
    protected String getViewName(Record keyval) {
        return getEntity(keyval);
    }
}
