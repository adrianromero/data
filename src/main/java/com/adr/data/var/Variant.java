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
import java.time.ZoneOffset;
import java.util.Date;

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
    
    public Date asDate() {
        throw new UnsupportedOperationException("Variant cannot be converted to date.");
    }
    
    public LocalTime asLocalTime() {
        Date date = asDate();
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneOffset.UTC).toLocalTime();
    }
    
    public LocalDate asLocalDate() {
        Date date = asDate();
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneOffset.UTC).toLocalDate();
    }
    
    public LocalDateTime asLocalDateTime() {
        Date date = asDate();
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneOffset.UTC);
    }
    
    public Instant asInstant() {
        Date date = asDate();
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime());
    }
    
    public byte[] asBytes() {
        throw new UnsupportedOperationException("Variant cannot be converted to bytes.");
    }
    
    public Object asObject() {
        throw new UnsupportedOperationException("Variant cannot be converted to serialized object.");
    }
    
}
