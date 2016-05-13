/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author adrian
 */
public class DataList {
    
    private final List<Record> data = new ArrayList<>();
       
    public DataList(Record ... keyval) {
        data.addAll(Arrays.asList(keyval));
    }
    
    public List<Record> getData() {
        return data;
    }
}
