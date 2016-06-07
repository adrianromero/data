/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.var;

import com.adr.data.DataException;
import com.adr.data.Parameters;
import com.adr.data.Results;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantString extends Variant {
    
    public final static VariantString NULL = new VariantString(null);
    
    private String value;
 
    public VariantString(String value) {
        this.value = value;
    }
    
    protected VariantString() {
        this(null);
    }
    
    @Override
    public Kind getKind() {
        return Kind.STRING;
    }

    @Override
    public String asISO() throws DataException {
        return value;
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        this.value = value == null || value.equals("") ? null : value;
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setString(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getString(name);
    }
    
    @Override
    public String asString() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.value);
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
        final VariantString other = (VariantString) obj;
        return Objects.equals(this.value, other.value);
    }
}
