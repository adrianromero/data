//     Data Access is a Java library to store data
//     Copyright (C) 2017-2019 Adrián Romero Corchado.
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

package com.adr.data;

public class DataException extends Exception {
    
    private static final long serialVersionUID = 8719551478674716662L;
    
    public DataException(String msg) {
        super(msg);
    }
    public DataException(Throwable t) {
        super(t);
    }
    public DataException(String msg, Throwable t) {
        super(msg, t);
    }  
}
