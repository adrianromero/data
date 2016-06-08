/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.var;

import com.adr.data.DataException;
import com.adr.data.Parameters;
import com.adr.data.Results;
import com.adr.data.utils.Serializer;
import java.io.IOException;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantObject extends Variant {

    public final static VariantObject NULL = new VariantObject(null); 
    
    private Object value;
    
    public VariantObject(Object value) {
        this.value = value;
    }
    
    public VariantObject() {
        this(null);
    }

    @Override
    public Kind getKind() {
        return Kind.OBJECT;
    }

    @Override
    public String asISO() throws DataException {
        try {
            return value == null ? null : Serializer.serialize(value);
        } catch (IOException ex) {
            throw new DataException(ex);
        }
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        try {
            this.value = value == null || value.equals("") ? null : Serializer.deserialize(value);       
        } catch (IOException | ClassNotFoundException ex) {
            throw new DataException(ex);
        }            
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setObject(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getObject(name);
    }
    
    @Override
    public boolean isNull() {
        return value == null;
    }
    
    @Override
    public Object asObject() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VariantObject other = (VariantObject) obj;
        return Objects.equals(this.value, other.value);
    }
}
