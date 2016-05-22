//    Data Access is a Java library to store data
//    Copyright (C) 2016 Adrián Romero Corchado.
//
//    This file is part of Data Access
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//     
//         http://www.apache.org/licenses/LICENSE-2.0
//     
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.

package com.adr.data;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 *
 * @author adrian
 * @param <T>
 */
public abstract class Kind<T> {

    public final static Kind<Void> VOID = new KindVOID();
    public final static Kind<Number> INT = new KindINT();
    public final static Kind<String> STRING = new KindSTRING();
    public final static Kind<Number> DOUBLE = new KindDOUBLE();
    public final static Kind<BigDecimal> DECIMAL = new KindDECIMAL();
    public final static Kind<Boolean> BOOLEAN = new KindBOOLEAN();
    public final static Kind<Date> TIMESTAMP = new KindTIMESTAMP();
    public final static Kind<Date> DATE = new KindDATE();
    public final static Kind<Date> TIME = new KindTIME();
    public final static Kind<byte[]> BYTEA = new KindBYTEA();
    public final static Kind<Object> OBJECT = new KindOBJECT();
    
    public abstract void set(KindParameters write, String name, T value) throws DataException;
    public abstract T get(KindResults write, String name) throws DataException;
    
    public abstract String _formatISO(T value) throws DataException;
    public final String formatISO(T value) throws DataException {
        return value == null ? "" : _formatISO(value);
    }
    public abstract T _parseISO(String value) throws DataException;
    public final T parseISO(String value) throws DataException {
        return value == null || value.equals("") ? null : _parseISO(value);
    }    
    
    private static final DateFormat dateISO = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat timeISO = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final DateFormat datetimeISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");     
    
    public static final Kind<?> valueOf(String kind) {
        if ("VOID".equals(kind)) {
            return Kind.VOID;
        } else if ("INT".equals(kind)) {
            return Kind.INT;
        } else if ("STRING".equals(kind)) {
            return Kind.STRING;
        } else if ("DOUBLE".equals(kind)) {
            return Kind.DOUBLE;
        } else if ("DECIMAL".equals(kind)) {
            return Kind.DECIMAL;
        } else if ("BOOLEAN".equals(kind)) {
            return Kind.BOOLEAN;
        } else if ("TIMESTAMP".equals(kind)) {
            return Kind.TIMESTAMP;
        } else if ("DATE".equals(kind)) {
            return Kind.DATE;
        } else if ("TIME".equals(kind)) {
            return Kind.TIME;
        } else if ("BYTEA".equals(kind)) {
            return Kind.BYTEA;
        } else if ("OBJECT".equals(kind)) {
            return Kind.OBJECT;            
        } else {
            throw new RuntimeException("Cannot get Kind for value " + kind);
        }
    }

    private static final class KindVOID extends Kind<Void> {
        @Override
        public void set(KindParameters write, String name, Void value) throws DataException {
        }
        @Override
        public Void get(KindResults read, String name) throws DataException {
            return null;
        }
        @Override
        public String _formatISO(Void value) throws DataException {
            return "";
        }
        @Override
        public Void _parseISO(String value) throws DataException {          
            return null;
        }
        @Override
        public String toString() {
            return "VOID";
        }       
    }

    private static final class KindINT extends Kind<Number> {
        @Override
        public void set(KindParameters write, String name, Number value) throws DataException {
            write.setInt(name, value);
        }
        @Override
        public Number get(KindResults read, String name) throws DataException {
            return read.getInt(name);
        }
        @Override
        public String _formatISO(Number value) throws DataException {
            return Long.toString(value.longValue());
        }
        @Override
        public Number _parseISO(String value) throws DataException {          
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                throw new DataException(ex);
            }
        }
        @Override
        public String toString() {
            return "INT";
        }       
    }

    private static final class KindSTRING extends Kind<String> {
        @Override
        public void set(KindParameters write, String name, String value) throws DataException {
            write.setString(name, value);
        }
        @Override
        public String get(KindResults read, String name) throws DataException {
            return read.getString(name);
        }
        @Override
        public String _formatISO(String value) throws DataException {
            return value;
        }
        @Override
        public String _parseISO(String value) throws DataException {
            return value;
        }         
        @Override
        public String toString() {
            return "STRING";
        }          
    }

    private static final class KindDOUBLE extends Kind<Number> {
         @Override
        public void set(KindParameters write, String name, Number value) throws DataException {
            write.setDouble(name, value);
        }
        @Override
        public Number get(KindResults read, String name) throws DataException {
            return read.getDouble(name);
        }
        @Override
        public String _formatISO(Number value) throws DataException {
            return Double.toString(value.doubleValue());
        }
        @Override
        public Number _parseISO(String value) throws DataException {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                throw new DataException(ex);
            }
        }          
        @Override
        public String toString() {
            return "DOUBLE";
        }          
    }

    private static final class KindDECIMAL extends Kind<BigDecimal> {
        @Override
        public void set(KindParameters write, String name, BigDecimal value) throws DataException {
            write.setBigDecimal(name, value);
        }
        @Override
        public BigDecimal get(KindResults read, String name) throws DataException {
            return read.getBigDecimal(name);
        }
        @Override
        public String _formatISO(BigDecimal value) throws DataException {
            return ((BigDecimal) value).toString();
        }
        @Override
        public BigDecimal _parseISO(String value) throws DataException {
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException ex) {
                throw new DataException(ex);
            }
        }         
        @Override
        public String toString() {
            return "DECIMAL";
        }          
    }

    private static final class KindBOOLEAN extends Kind<Boolean> {
        @Override
        public void set(KindParameters write, String name, Boolean value) throws DataException {
            write.setBoolean(name, value);
        }
        @Override
        public Boolean get(KindResults read, String name) throws DataException {
            return read.getBoolean(name);
        }
        @Override
        public String _formatISO(Boolean value) throws DataException {
            return value.toString();
        }
        @Override
        public Boolean _parseISO(String value) throws DataException {
            return Boolean.valueOf(value);
        }             
        @Override
        public String toString() {
            return "BOOLEAN";
        }           
    }

    private static final class KindTIMESTAMP extends Kind<Date> {
        @Override
        public void set(KindParameters write, String name, Date value) throws DataException {
            write.setTimestamp(name, value);
        }
        @Override
        public Date get(KindResults read, String name) throws DataException {
            return read.getTimestamp(name);
        }
        @Override
        public String _formatISO(Date value) throws DataException {          
            return datetimeISO.format(value);
        }
        @Override
        public Date _parseISO(String value) throws DataException {        
            try {
                return datetimeISO.parse(value);
            } catch (ParseException e) {
                throw new DataException(e);                
            }
        }          
        @Override
        public String toString() {
            return "TIMESTAMP";
        }        
    }  
    
    private static final class KindDATE extends Kind<Date> {
        @Override
        public void set(KindParameters write, String name, Date value) throws DataException {
            write.setDate(name, value);
        }
        @Override
        public Date get(KindResults read, String name) throws DataException {
            return read.getDate(name);
        }
        @Override
        public String _formatISO(Date value) throws DataException {          
            return dateISO.format(value);
        }
        @Override
        public Date _parseISO(String value) throws DataException {        
            try {
                return dateISO.parse(value);
            } catch (ParseException e) {
                throw new DataException(e);                
            }
        }          
        @Override
        public String toString() {
            return "DATE";
        }          
    }
    
    private static final class KindTIME extends Kind<Date> {
        @Override
        public void set(KindParameters write, String name, Date value) throws DataException {
            write.setTime(name, value);
        }
        @Override
        public Date get(KindResults read, String name) throws DataException {
            return read.getTime(name);
        }
        @Override
        public String _formatISO(Date value) throws DataException {          
            return timeISO.format(value);
        }
        @Override
        public Date _parseISO(String value) throws DataException {        
            try {
                return timeISO.parse(value);
            } catch (ParseException e) {
                throw new DataException(e);                
            }
        }          
        @Override
        public String toString() {
            return "TIME";
        }          
    }

    private static final class KindBYTEA extends Kind<byte[]> {
        @Override
        public void set(KindParameters write, String name, byte[] value) throws DataException {
            write.setBytes(name, value);
        }
        @Override
        public byte[] get(KindResults read, String name) throws DataException {
            return read.getBytes(name);
        }
        @Override
        public String _formatISO(byte[] value) throws DataException {          
            return Base64.getEncoder().encodeToString(value);
        }
        @Override
        public byte[] _parseISO(String value) throws DataException {           
            try {
                return Base64.getDecoder().decode(value);
            } catch(IllegalArgumentException e) {
                throw new DataException(e);
            }

        }         
        @Override
        public String toString() {
            return "BYTEA";
        }          
    }

    private static final class KindOBJECT extends Kind<Object> {
        @Override
        public void set(KindParameters write, String name, Object value) throws DataException {
            write.setObject(name, value);
        }
        @Override
        public Object get(KindResults read, String name) throws DataException {
            return read.getObject(name);
        }
        @Override
        public String _formatISO(Object value) throws DataException {          
            throw new UnsupportedOperationException("Cannot parse using Kind.OBJECT");
        }
        @Override
        public Object _parseISO(String value) throws DataException {           
            throw new UnsupportedOperationException("Cannot parse using Kind.OBJECT");
        }         
        @Override
        public String toString() {
            return "OBJECT";
        }          
    }        
}
