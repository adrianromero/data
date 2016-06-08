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
public class VariantDouble extends Variant {
    
    public final static VariantDouble NULL = new VariantDouble(null);
    
    private Double value;
    
    public VariantDouble(Double value) {
        this.value = value;
    }
    
    protected VariantDouble() {
        this(null);
    }
    
    
    @Override
    public Kind getKind() {
        return Kind.DOUBLE;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : value.toString();
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        try {
            this.value = value == null || value.equals("") ? null : Double.parseDouble(value);       
        } catch (NumberFormatException ex) {
            throw new DataException(ex);
        }            
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setDouble(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getDouble(name);
    }
    
    @Override
    public boolean isNull() {
        return value == null;
    }
    
    @Override
    public Number asNumber() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.value);
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
        final VariantDouble other = (VariantDouble) obj;
        return Objects.equals(this.value, other.value);
    } 
}
