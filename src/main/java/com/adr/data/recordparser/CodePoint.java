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

/**
 *
 * @author adrian
 */
public final class CodePoint {
    
    private CodePoint() {}

    public static final boolean isHex(int cp) {
        return (cp >= '0' && cp <= '9') || (cp >= 'a' && cp <= 'f') || (cp >= 'A' && cp <= 'F');
    }
    public static final boolean isAlpha(int cp) {
        return Character.isAlphabetic(cp);
    }
    public static final boolean isInitIdentifier(int cp) {
        return Character.isUnicodeIdentifierStart(cp) || cp == '.';
    }
    public static final boolean isIdentifier(int cp) {
        return Character.isUnicodeIdentifierPart(cp) || cp == '.';
    }
    public static final boolean isControl(int cp) {
        return Character.isISOControl(cp);
    }
    public static final boolean isWhite(int cp) {
        return Character.isWhitespace(cp);
    }
    public static final boolean isEOF(int cp) {
        return cp == -1;
    }
    public static final boolean isInitNumber(int cp) {
        return (cp >= '\u0030' && cp <= '\u0039') || cp == '-';
    }
    public static final boolean isDouble(int cp) {
        return (cp >= '\u0030' && cp <= '\u0039') || cp == '+' || cp == '-' || cp == '.' || cp == 'e' || cp == 'E';
    }
    public static final boolean isInt(int cp) {
        return (cp >= '\u0030' && cp <= '\u0039') || cp == '+' || cp == '-';
    }
    
    public static final String toString(int cp) {
        if (isEOF(cp)) {
            return "<EOF>";
        } else if (isControl(cp)) {
            return "<CTRL>";
        } else {
            return new String(new int[]{ cp }, 0, 1);
        }
    }    
}
