//     Data Access is a Java library to store data
//     Copyright (C) 2018 Adrián Romero Corchado.
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

package com.adr.data.sql;

import com.adr.data.sqlh2.SentenceH2Put;
import com.adr.data.sqlpg.SentencePGPut;

/**
 *
 * @author adrian
 */
public enum SQLEngine {
    
    GENERIC(
            new SentencePut(),
            " LIKE CONCAT('%', REPLACE(REPLACE(?, '%', '$%'), '_', '$_'), '%') {escape '$'}",
            " LIKE CONCAT(REPLACE(REPLACE(?, '%', '$%'), '_', '$_'), '%') {escape '$'}",
            " LIKE CONCAT('%', REPLACE(REPLACE(?, '%', '$%'), '_', '$_')) {escape '$'}"),
    POSTGRESQL(
            new SentencePGPut(), 
            " LIKE CONCAT('%', REPLACE(REPLACE(?, '%', '$%'), '_', '$_'), '%') {escape '$'}",
            " LIKE CONCAT(REPLACE(REPLACE(?, '%', '$%'), '_', '$_'), '%') {escape '$'}",
            " LIKE CONCAT('%', REPLACE(REPLACE(?, '%', '$%'), '_', '$_')) {escape '$'}"),
    MYSQL(
            new SentencePut(), 
            " LIKE CONCAT('%', REPLACE(REPLACE(?, '%', '$%'), '_', '$_'), '%') escape '$'",
            " LIKE CONCAT(REPLACE(REPLACE(?, '%', '$%'), '_', '$_'), '%') escape '$'",
            " LIKE CONCAT('%', REPLACE(REPLACE(?, '%', '$%'), '_', '$_')) escape '$'"),
    H2(
            new SentenceH2Put(), 
            " LIKE CONCAT('%', REPLACE(REPLACE(?, '%', '$%'), '_', '$_'), '%') {escape '$'}",
            " LIKE CONCAT(REPLACE(REPLACE(?, '%', '$%'), '_', '$_'), '%') {escape '$'}",
            " LIKE CONCAT('%', REPLACE(REPLACE(?, '%', '$%'), '_', '$_')) {escape '$'}");
    
    private final Sentence putsent;
    private final String containsexpression;
    private final String startsexpression;
    private final String endsexpression;
    
    private SQLEngine(Sentence putsent, String containsexpression, String startsexpression, String endsexpression) {
        this.putsent = putsent;
        this.containsexpression = containsexpression;
        this.startsexpression = startsexpression;
        this.endsexpression = endsexpression;
    }
    
    public Sentence getPutSentence() {
        return putsent;
    }
    
    public String getContainsExpression() {
        return containsexpression;
    }
    
    public String getStartsExpression() {
        return startsexpression;
    }
    
    public String getEndsExpression() {
        return endsexpression;
    }
}
