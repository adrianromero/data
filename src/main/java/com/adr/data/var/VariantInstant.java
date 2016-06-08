/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.var;

import com.adr.data.DataException;
import com.adr.data.Parameters;
import com.adr.data.Results;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantInstant extends Variant {

    public final static VariantInstant NULL = new VariantInstant();

    private Instant value;
    
    public VariantInstant(Instant value) {
        this.value = value;
    }
    
    protected VariantInstant() {
        this(null);
    }

    @Override
    public Kind getKind() {
        return Kind.INSTANT;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : value.toString();
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        try {
            this.value = value == null || value.equals("") ? null : Instant.parse(value);       
        } catch (DateTimeParseException ex) {
            throw new DataException(ex);
        }            
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setInstant(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getInstant(name);
    
    }
    @Override
    public boolean isNull() {
        return value == null;
    }
      
    @Override
    public Instant asInstant() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.value);
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
        final VariantInstant other = (VariantInstant) obj;
        return Objects.equals(this.value, other.value);
    }
}
