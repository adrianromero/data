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
public class RecordMap implements Record {
    
    private final ValuesMap key;
    private final ValuesMap value;
    
    public RecordMap(ValuesMap key, ValuesMap value) {
        this.key = key;
        this.value = value;
    }
    
    @Override
    public Values getKey() {
        return key;
    }

    @Override
    public Values getValue() {
        return value;
    }    
}
