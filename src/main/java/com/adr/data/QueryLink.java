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
public interface QueryLink {
    public RecordMap find(RecordMap filter) throws DataException;
    public DataList query(RecordMap filter) throws DataException;       
}
