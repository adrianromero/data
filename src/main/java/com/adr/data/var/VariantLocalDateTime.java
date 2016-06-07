/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.var;

import com.adr.data.DataException;
import com.adr.data.Parameters;
import com.adr.data.Results;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantLocalDateTime extends Variant {

    public final static VariantLocalDateTime NULL = new VariantLocalDateTime();

    private LocalDateTime value;
    
    public VariantLocalDateTime(LocalDateTime value) {
        this.value = value;
    }
    
    protected VariantLocalDateTime() {
        this(null);
    }

    @Override
    public Kind getKind() {
        return Kind.LOCALDATETIME;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : value.toString();
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        try {
            this.value = value == null || value.equals("") ? null : LocalDateTime.parse(value);       
        } catch (DateTimeParseException ex) {
            throw new DataException(ex);
        }            
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setLocalDateTime(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getLocalDateTime(name);
    }
    
    @Override
    public LocalDateTime asLocalDateTime() {
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
        final VariantLocalDateTime other = (VariantLocalDateTime) obj;
        return Objects.equals(this.value, other.value);
    }
}
