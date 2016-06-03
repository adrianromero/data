//     Data Access is a Java library to store data
//     Copyright (C) 2016 Adrián Romero Corchado.
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
import com.adr.data.DataList;
import com.adr.data.Record;
import com.google.gson.JsonElement;

/**
 *
 * @author adrian
 */
public abstract class EnvelopeResponse {
    public abstract String getType();  
    public abstract JsonElement dataToJSON();
    
    public Record getAsRecord() throws DataException {
        throw new DataException("Record response not supported.");
    }
    public DataList getAsDataList() throws DataException {
        throw new DataException("DataList response not supported.");
    }
    public void asSuccess() throws DataException {
        throw new DataException("DataList response not supported.");
    }    
}
