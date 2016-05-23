//    Data SQL is a light JDBC wrapper.
//    Copyright (C) 2015 Adrián Romero Corchado.
//
//    This file is part of Data SQL
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

/**
 *
 * @author adrian
 */
public class QueryView extends QuerySelect {
    private final String name;
    private final String sentence;
    
    public QueryView(String name, String sentence) {
        this.name = name;
        this.sentence = sentence;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    protected String getViewName() {
        return  "(" + sentence + ")";
    }
    
    public String getSentence() {
        return sentence;
    }
}