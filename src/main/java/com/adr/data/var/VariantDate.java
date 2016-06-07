/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.var;

import com.adr.data.DataException;
import com.adr.data.Parameters;
import com.adr.data.Results;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantDate extends Variant {

    public final static VariantDate NULL = new VariantDate(null);    
    
    private static final DateFormat DATEISO = new SimpleDateFormat("yyyy-MM-dd");     

    private Date value;
    
    public VariantDate(Date value) {
        this.value = value;
    }
    
    public VariantDate() {
        this(null);
    }

    @Override
    public Kind getKind() {
        return Kind.DATE;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : DATEISO.format(value);
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        try {
            this.value = value == null || value.equals("") ? null : DATEISO.parse(value);       
        } catch (ParseException ex) {
            throw new DataException(ex);
        }            
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setDate(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getDate(name);
    }
    
    @Override
    public Date asDate() {
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
        final VariantDate other = (VariantDate) obj;
        return Objects.equals(this.value, other.value);
    }
}
