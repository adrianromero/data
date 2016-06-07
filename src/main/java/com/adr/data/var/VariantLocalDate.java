/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.var;

import com.adr.data.DataException;
import com.adr.data.Parameters;
import com.adr.data.Results;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantLocalDate extends Variant {

    public final static VariantLocalDate NULL = new VariantLocalDate();        

    private LocalDate value;
    
    public VariantLocalDate(LocalDate value) {
        this.value = value;
    }
    
    public VariantLocalDate() {
        this(null);
    }

    @Override
    public Kind getKind() {
        return Kind.LOCALDATE;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : value.toString();
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        try {
            this.value = value == null || value.equals("") ? null : LocalDate.parse(value);       
        } catch (DateTimeParseException ex) {
            throw new DataException(ex);
        }            
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setLocalDate(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getLocalDate(name);
    }
    
    @Override
    public LocalDate asLocalDate() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.value);
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
        final VariantLocalDate other = (VariantLocalDate) obj;
        return Objects.equals(this.value, other.value);
    }
}
