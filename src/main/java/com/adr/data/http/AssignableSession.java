/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author adrian
 */
public interface AssignableSession {
    public void readSerializedSession(DataInput session) throws IOException;
    public void writeSerializedSession(DataOutput session) throws IOException;
    
    public default void setSerializableSession(byte[] session) throws IOException {
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(session))) {
            readSerializedSession(in);
        }       
    }
    public default byte[] getSerializableSession() throws IOException {
        ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(bytearray)) {
            writeSerializedSession(out);
        }  
        return bytearray.toByteArray();
    }
}
