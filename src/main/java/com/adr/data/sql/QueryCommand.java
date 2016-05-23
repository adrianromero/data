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
public class QueryCommand implements Query {
    
    private final String name;
    private final CommandSQL commandsql;
    
    public QueryCommand(String name, String command, String... paramnames) {
        this.name = name;
        this.commandsql = new CommandSQL(command, paramnames);
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public CommandSQL buildSQLCommand(Record keyval) {
        return commandsql;
    }

    @Override
    public boolean isField(String name) {
        return true;
    }  
}
