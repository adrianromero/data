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
public interface Values {
    public String [] getNames();   
    public Object getValue(String name);   
    public Kind getKind(String name);
}
