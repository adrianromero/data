//     Data Access is a Java library to store data
//     Copyright (C) 2017 Adrián Romero Corchado.
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
import java.io.Reader;

/**
 *
 * @author adrian
 */
public class StreamLoader implements Loader {
    private final Reader reader;
    private int row;
    private int column;
    private int cp;    
    
    public StreamLoader(Reader reader) {
        this.reader = reader;
        row = 1;
        column = 0;
        cp = -1;
    }    
    
    @Override
    public final void next() throws IOException {
        cp = reader.read();
        if (cp == '\n') {
            row ++;
            column = 0;
        } else {
            column ++;
        }
    }   
    @Override
    public final int getCP() {
        return cp;
    }
    @Override
    public final String messageGeneral(String message) {
        return String.format("Syntax error. Row %1$s, column %2$s. %3$s.", row, column, message);  
    }     
}
