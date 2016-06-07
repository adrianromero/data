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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantTimestamp extends Variant {

    public final static VariantTimestamp NULL = new VariantTimestamp();
    
    private static final DateFormat DATETIMEISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");     

    private Date value;
    
    public VariantTimestamp(Date value) {
        this.value = value;
    }
    
    public VariantTimestamp(LocalDateTime value) {
        this.value = value == null ? null : Date.from(value.toInstant(ZoneOffset.UTC));
    }
    
    public VariantTimestamp(Instant value) {
        this.value = value == null ? null : Date.from(value);
    }
    
    protected VariantTimestamp() {
        this((Date) null);
    }

    @Override
    public Kind getKind() {
        return Kind.TIMESTAMP;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : DATETIMEISO.format(value);
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        try {
            this.value = value == null || value.equals("") ? null : DATETIMEISO.parse(value);       
        } catch (ParseException ex) {
            throw new DataException(ex);
        }            
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setTimestamp(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getTimestamp(name);
    }
    
    @Override
    public Date asDate() {
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
        final VariantTimestamp other = (VariantTimestamp) obj;
        return Objects.equals(this.value, other.value);
    }
}
