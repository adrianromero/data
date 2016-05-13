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
public class DataException extends Exception {

    public DataException() {
    }

    public DataException(String msg) {
        super(msg);
    }
    public DataException(Throwable t) {
        super(t);
    }
    public DataException(String msg, Throwable t) {
        super(msg, t);
    }  
}
