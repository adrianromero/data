/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.var;

import com.adr.data.DataException;
import com.adr.data.Parameters;
import com.adr.data.Results;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author adrian
 */
public abstract class Variant {
    
    protected abstract void buildISO(String value) throws DataException;  
    protected abstract void buildRead(Results read, String name) throws DataException;
    
    public abstract Kind getKind();
    
    public abstract String asISO() throws DataException;

    public abstract void write(Parameters write, String name) throws DataException;

    public abstract boolean isNull();
    
    public String asString() {
        throw new UnsupportedOperationException("Variant cannot be converted to string.");
    }
    
    public Number asNumber() {
        throw new UnsupportedOperationException("Variant cannot be converted to number.");
    }
    
    public int asInteger() {
        return asNumber().intValue();
    }
    
    public long asLong() {
        return asNumber().longValue();
    }
    
    public double asDouble() {
        return asNumber().doubleValue();
    }    
    
    public BigDecimal asBigDecimal() {
        return new BigDecimal(asNumber().toString());
    }
    
    public Boolean asBoolean() {
        throw new UnsupportedOperationException("Variant cannot be converted to boolean.");
    }
    
    public LocalTime asLocalTime() {
        throw new UnsupportedOperationException("Variant cannot be converted to local time.");
    }
    
    public LocalDate asLocalDate() {
        throw new UnsupportedOperationException("Variant cannot be converted to local date.");
    }
    
    public LocalDateTime asLocalDateTime() {
        throw new UnsupportedOperationException("Variant cannot be converted to local date time.");
    }
    
    public Instant asInstant() {
        throw new UnsupportedOperationException("Variant cannot be converted to instant.");
    }
    
    public byte[] asBytes() {
        throw new UnsupportedOperationException("Variant cannot be converted to bytes.");
    }
    
    public Object asObject() {
        throw new UnsupportedOperationException("Variant cannot be converted to serialized object.");
    }
    
}
