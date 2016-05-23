/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

/**
 *
 * @author adrian
 */
public class QueryTable extends QuerySelect {
    
    private final String name;
    
    public QueryTable(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    protected String getViewName() {
        return name;
    }
}
