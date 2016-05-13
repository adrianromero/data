/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data;

/**
 *
 * @author adrian
 */
public class ValuesEntry {
    private String name;
    private Kind kind;
    private Object value;
    
    public ValuesEntry(String name, String value) {
        this(name, Kind.STRING, value);
    }
    
    public ValuesEntry(String name, Kind kind) {
        this(name, kind, null);
    }
    
    public ValuesEntry(String name, Kind kind, Object value) {
        this.name = name;
        this.value = value;
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public Kind getKind() {
        return kind;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    } 
}
