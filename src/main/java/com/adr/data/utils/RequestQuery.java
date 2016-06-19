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

import com.adr.data.Record;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;

/**
 *
 * @author adrian
 */
public class RequestQuery extends EnvelopeRequest {
    
    public static final String NAME = "QUERY";
    
    private final Record filter;
    private final Map<String, String> options;
    
    public RequestQuery(Record filter, Map<String, String> options) {
        this.filter = filter;
        this.options = options;
    }
    
    public Record getFilter() {
        return filter;
    }
    
    public Map<String, String> getOptions() {
        return options;
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
        obj.add("filter", JSONSerializer.INSTANCE.toJSONElement(filter));
        obj.add("options", JSONSerializer.INSTANCE.toJSONElement(options));
        return obj;
    }    
}
