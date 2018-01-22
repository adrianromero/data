//     Data Access is a Java library to store data
//     Copyright (C) 2017-2018 Adrián Romero Corchado.
//
//     This file is part of Data Access
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
package com.adr.data.recordparser;

import java.io.IOException;
import java.io.StringReader;

/**
 *
 * @author adrian
 */
public class CommonParsers {
    
    public final static int TYPE_INT = 0;
    public final static int TYPE_DOUBLE = 1;
    
    public final static class Numeric{     
        private final String value;
        private final int type;
        public Numeric(String value, int type) {
            this.value = value;
            this.type = type;
        }
        public String getValue() {
            return value;
        }
        public int getType() {
            return type;
        }
       
    }

    public static Numeric parseNumber(Loader loader) throws IOException {
        int type = TYPE_INT;
        StringBuilder b = new StringBuilder();
        if (CodePoint.isInitNumber(loader.getCP())) {
            b.appendCodePoint(loader.getCP());
            loader.next();
        } else {       
            throw new IOException(loader.messageExpected("Number"));
        }
        
        for(;;)  {
            if (CodePoint.isDouble(loader.getCP())) {
                if (!CodePoint.isInt(loader.getCP())) {
                    type = TYPE_DOUBLE;
                }
                b.appendCodePoint(loader.getCP());
                loader.next();
            } else {
                return new Numeric(b.toString(), type);
            }
        }
    } 
    
    public static String parseWord(Loader loader) throws IOException {
       
        StringBuilder b = new StringBuilder();  
        
        if (CodePoint.isAlpha(loader.getCP())) {
            b.appendCodePoint(loader.getCP());
            loader.next();
        } else {       
            throw new IOException(loader.messageExpected("Alphabetic"));
        } 
        
        for(;;)  {
            if (CodePoint.isAlpha(loader.getCP())) {
                b.appendCodePoint(loader.getCP());
                loader.next();
            } else {
                return b.toString();
            }
        }
    }   
    
    public static String parseIdentifier(Loader loader) throws IOException {
       
        StringBuilder b = new StringBuilder();         
        
        if (CodePoint.isInitIdentifier(loader.getCP())) {
            b.appendCodePoint(loader.getCP());
            loader.next();
        } else {
            throw new IOException(loader.messageExpected("Identifier"));
        }
        
        for(;;)  {
            if (CodePoint.isIdentifier(loader.getCP())) {
                b.appendCodePoint(loader.getCP());
                loader.next();
            } else {
                return b.toString();
            }
        }
    } 
      
    private final static int STRING_BODY = 0;
    private final static int STRING_ESCAPE = 1;
    private final static int STRING_UNICODE1 = 2;
    private final static int STRING_UNICODE2 = 3;
    private final static int STRING_UNICODE3 = 4;
    private final static int STRING_UNICODE4 = 5; 
    
    public static String parseString(Loader loader) throws IOException {
       
        StringBuilder b = new StringBuilder();         
        StringBuilder unicodeBuffer = null;
        int state;
        
        if (loader.getCP() == '\"') {
            state = STRING_BODY;
            loader.next();
        } else {
            throw new IOException(loader.messageExpected('\"'));
        }
        
        for(;;)  {
            if (state == STRING_BODY) {
                if (loader.getCP() == '\\') {
                    loader.next();
                    state = STRING_ESCAPE;
                } else if (loader.getCP() == '\"') {
                    loader.next();
                    return b.toString();
                } else if (!CodePoint.isControl(loader.getCP())) {
                    b.appendCodePoint(loader.getCP());
                    loader.next();
                } else {
                   throw new IOException(loader.messageExpected("Non control"));
                }
            } else if (state == STRING_ESCAPE) {
                if (loader.getCP() == 't') {
                    b.append('\t');
                    loader.next();
                    state = STRING_BODY;
                } else if (loader.getCP() == 'b') {
                    b.append('\b');
                    loader.next();
                    state = STRING_BODY;      
                } else if (loader.getCP() == 'n') {
                    b.append('\n');
                    loader.next();
                    state = STRING_BODY;  
                } else if (loader.getCP() == 'r') {
                    b.append('\r');
                    loader.next();
                    state = STRING_BODY;   
                } else if (loader.getCP() == 'f') {
                    b.append('\f');
                    loader.next();
                    state = STRING_BODY;  
                } else if (loader.getCP() == '\'') {
                    b.append('\'');
                    loader.next();
                    state = STRING_BODY;   
                } else if (loader.getCP() == '\"') {
                    b.append('\"');
                    loader.next();
                    state = STRING_BODY;         
                } else if (loader.getCP() == '\\') {
                    b.append('\\');
                    loader.next();
                    state = STRING_BODY;      
                } else if (loader.getCP() == 'u') {
                    unicodeBuffer = new StringBuilder();
                    loader.next();
                    state = STRING_UNICODE1;
                } else {
                    throw new IOException(loader.messageExpected("Escape sequence"));                    
                }
            } else if (state == STRING_UNICODE1) {
                if (CodePoint.isHex(loader.getCP())) {
                    unicodeBuffer.appendCodePoint(loader.getCP());
                    loader.next();
                    state = STRING_UNICODE2;
                } else {
                    throw new IOException(loader.messageExpected("Hexadecimal"));
                }
            } else if (state == STRING_UNICODE2) {
                if (CodePoint.isHex(loader.getCP())) {
                    unicodeBuffer.appendCodePoint(loader.getCP());
                    loader.next();
                    state = STRING_UNICODE3;
                } else {
                    throw new IOException(loader.messageExpected("Hexadecimal"));
                }
            } else if (state == STRING_UNICODE3) {
                if (CodePoint.isHex(loader.getCP())) {
                    unicodeBuffer.appendCodePoint(loader.getCP());
                    loader.next();
                    state = STRING_UNICODE4;
                } else {
                    throw new IOException(loader.messageExpected("Hexadecimal"));
                }   
            } else if (state == STRING_UNICODE4) {
                if (CodePoint.isHex(loader.getCP())) {
                    unicodeBuffer.appendCodePoint(loader.getCP());
                    b.appendCodePoint(Integer.parseInt(unicodeBuffer.toString(), 16));
                    unicodeBuffer = null;
                    loader.next();
                    state = STRING_BODY;
                } else {
                    throw new IOException(loader.messageExpected("Hexadecimal"));
                }                
            } else {
                throw new IOException("Unexpected error");
            }            
        }    
    }
    
    public final static boolean isIdentifier(String value) throws IOException {
        Loader loader = new StreamLoader(new StringReader(value));
        loader.next();
        CommonParsers.parseIdentifier(loader);
        return CodePoint.isEOF(loader.getCP());
    }
    
    public final static String quote(String value) {
        
        StringBuilder b = new StringBuilder();
        b.append('\"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\"') {
                b.append("\\\"");
            } else if (c == '\t') {
                b.append("\\t");
            } else if (c == '\b') {
                b.append("\\b");
            } else if (c == '\n') {
                b.append("\\n");
            } else if (c == '\r') {
                b.append("\\r");
            } else if (c == '\f') {
                b.append("\\f");
            } else if (c == '\'') {
                b.append("\\\'");
            } else if (c == '\\') {
                b.append("\\\\");
            } else if (CodePoint.isControl(c) || c > 0xFF) {
                b.append("\\u");
                b.append(String.format("%04X", (int) c));
            } else {
                b.append(c);
            }
        }
        b.append('\"');
        return b.toString();
    }   
}
