//     Data Access is a Java library to store data
//     Copyright (C) 2019 Adrián Romero Corchado.
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
package com.adr.data.utils;

import com.adr.data.DataException;
import java.lang.reflect.InvocationTargetException;

public class ResponseErrors {

    private ResponseErrors() {
    }

    public final static Throwable createException(String name, String message) {
        try {
            return (Throwable) Class.forName(name).getConstructor(String.class).newInstance(message);
        } catch (ClassCastException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            return new DataException("Exception name: " + name + ", with message: " + message);
        }
    }

    public final static DataException createDataException(Throwable ex) {
        if (ex instanceof DataException) {
            return (DataException) ex;
        } else if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        } else {
            return new DataException(ex.toString());
        }
    }
}
