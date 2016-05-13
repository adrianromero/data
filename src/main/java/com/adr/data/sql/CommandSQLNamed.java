//    Data CommandSQL is a light JDBC wrapper.
//    Copyright (C) 2014-2015 Adrián Romero Corchado.
//
//    This file is part of Data CommandSQL
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

package com.adr.data.sql;

import com.adr.data.DataException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrian
 */
public class CommandSQLNamed extends CommandSQL {
    
    private static final char CHAR_ETX = '\u0003';
    
    private static final int STATE_LOOK = 0;
    private static final int STATE_SINGLEQUOTE = 1;
    private static final int STATE_DOUBLEQUOTE = 2;
    private static final int STATE_PARAMETERSTART = 3;
    private static final int STATE_PARAMETERPART = 4;

    public CommandSQLNamed(String command) throws DataException {
        
        StringBuilder parsedcommand = new StringBuilder();
        StringBuilder parametername = new StringBuilder();
        List<String> parsedparams = new ArrayList<>();
        
        int i = 0;
        int state = STATE_LOOK;
        char c;
        for (;;) {   
            
            c = i < command.length() ? command.charAt(i) : CHAR_ETX;
                    
            if (state == STATE_LOOK) {
                if (c == '\'') {
                    parsedcommand.append(c);
                    state = STATE_SINGLEQUOTE;
                } else if (c == '\"') {
                    parsedcommand.append(c);
                    state = STATE_DOUBLEQUOTE;
                } else if (c == ':') {
                    state = STATE_PARAMETERSTART;
                } else if (c == CHAR_ETX) {
                    init(parsedcommand.toString(), parsedparams.toArray(new String[parsedparams.size()]));
                    return;
                } else {
                    parsedcommand.append(c);
                }
            } else if (state == STATE_SINGLEQUOTE) {                
                if (c == '\'') {
                    parsedcommand.append(c);
                    state = STATE_LOOK;
                } else if (c == CHAR_ETX) {   
                    throw new DataException("Error parsing command. Invalid literal. Character: " + i);
                } else {
                    parsedcommand.append(c);
                }
            } else if (state == STATE_DOUBLEQUOTE) {                
                if (c == '\"') {
                    parsedcommand.append(c);
                    state = STATE_LOOK;
                } else if (c == CHAR_ETX) {   
                    throw new DataException("Error parsing command Invalid literal. Character: " + i);
                } else {
                    parsedcommand.append(c);
                }    
            } else if (state == STATE_PARAMETERSTART) {
                if (c == CHAR_ETX) {
                    throw new DataException("Error parsing command. Bad identifier. Character: " + i);
                } else if (Character.isJavaIdentifierStart(c)) {
                    parametername = new StringBuilder();
                    parametername.append(c);
                    state = STATE_PARAMETERPART;
                } else {
                    throw new DataException("Error parsing command. Bad identifier. Character: " + i);
                }
            } else if (state == STATE_PARAMETERPART) {
                if (c == CHAR_ETX) { 
                    parsedcommand.append('?');
                    parsedparams.add(parametername.toString());
                    
                    init(parsedcommand.toString(), parsedparams.toArray(new String[parsedparams.size()]));
                    return;   
                } else if (Character.isJavaIdentifierPart(c)) {
                    parametername.append(c);
                } else {
                    parsedcommand.append('?');
                    parsedcommand.append(c);
                    parsedparams.add(parametername.toString());
                    state = STATE_LOOK;
                }
            } else {
                throw new DataException("Error parsing command. Invalid state. Character: " + i);
            }

            i++;
        }
    }        
}
