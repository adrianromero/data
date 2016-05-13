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
public interface DataLink {
    public void execute(DataList l) throws DataException;
    
    public default void execute(KeyValue keyval) throws DataException {
        execute(new DataList(keyval));
    }
}
