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

import com.adr.data.record.Record;
import com.adr.data.record.Values;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * @author adrian
 */
public class RequestQuery extends EnvelopeRequest {
    
    public static final String NAME = "QUERY";
    
    private final Values headers;
    private final Record filter;
    
    public RequestQuery(Values headers, Record filter) {
        this.headers = headers;
        this.filter = filter;
    }
    
    public Record getFilter() {
        return filter;
    }
    
    public Values getHeaders() {
        return headers;
    }

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public EnvelopeResponse process(ProcessRequest proc) {
        return proc.query(this);
    }  

    @Override
    public JsonElement dataToJSON() {
        JsonObject obj = new JsonObject();
        obj.add("headers", JSON.INSTANCE.toJSONElement(headers));              
        obj.add("filter", JSON.INSTANCE.toJSONElement(filter));
        return obj;
    }    
}
