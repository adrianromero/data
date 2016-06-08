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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 *
 * @author adrian
 */
public class VariantLocalTime extends Variant {

    public final static VariantLocalTime NULL = new VariantLocalTime();

    private LocalTime value;
    
    public VariantLocalTime(LocalTime value) {
        this.value = value;
    }
    
    protected VariantLocalTime() {
        this(null);
    }

    @Override
    public Kind getKind() {
        return Kind.LOCALTIME;
    }

    @Override
    public String asISO() throws DataException {
        return value == null ? null : value.toString();
    }
    
    @Override
    protected void buildISO(String value) throws DataException {
        try {
            this.value = value == null || value.equals("") ? null : LocalTime.parse(value);       
        } catch (DateTimeParseException ex) {
            throw new DataException(ex);
        }            
    }
    
    @Override
    public void write(Parameters write, String name) throws DataException {
        write.setLocalTime(name, value);
    }

    @Override
    protected void buildRead(Results read, String name) throws DataException {
        this.value = read.getLocalTime(name);
    }
    
    @Override
    public boolean isNull() {
        return value == null;
    }
    
    @Override
    public LocalTime asLocalTime() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.value);
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
        final VariantLocalTime other = (VariantLocalTime) obj;
        return Objects.equals(this.value, other.value);
    }
}
