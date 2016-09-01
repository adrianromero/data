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
package com.adr.data.security;

import com.adr.data.sql.Sentence;
import com.adr.data.sql.SentenceCommand;
import com.adr.data.sql.SentenceQuery;
import com.adr.data.sql.SentenceView;

/**
 *
 * @author adrian
 */
public class SecureCommands {

    public final static Sentence[] QUERIES = new Sentence[]{
        new SentenceView(
        "PERMISSION_SUBJECT",
        "SELECT PERMISSION.ID, PERMISSION.ROLE_ID, SUBJECT.ID AS SUBJECT_ID, SUBJECT.NAME AS SUBJECT_NAME, SUBJECT.CODE AS SUBJECT_CODE "
        + "FROM PERMISSION JOIN SUBJECT ON PERMISSION.SUBJECT_ID = SUBJECT.ID"),
        new SentenceQuery(
        "USERNAME_VISIBLE",
        "SELECT ID, NAME, DISPLAYNAME, IMAGE FROM USERNAME WHERE VISIBLE = TRUE AND ACTIVE = TRUE ORDER BY NAME"),
        new SentenceQuery(
        "SUBJECT_BYROLE",
        "SELECT S.CODE, S.NAME FROM SUBJECT S JOIN PERMISSION P ON S.ID = P.SUBJECT_ID WHERE P.ROLE_ID = ?", "ROLE_ID::PARAM"),
        new SentenceQuery(
        "USERNAME_BYNAME",
        "SELECT U.ID, U.NAME, U.DISPLAYNAME, U.PASSWORD, U.CODECARD "
        + "FROM USERNAME U "
        + "WHERE U.NAME = ? AND U.ACTIVE = TRUE", "NAME"),
        new SentenceQuery(
        "USERNAME_BYID",
        "SELECT U.ID, U.NAME, U.DISPLAYNAME, U.CODECARD, U.ROLE_ID, R.NAME AS ROLE, U.VISIBLE, U.IMAGE "
        + "FROM USERNAME U JOIN ROLE R ON U.ROLE_ID = R.ID "
        + "WHERE U.ID = ? AND U.ACTIVE = TRUE", "ID")
    };

    public final static Sentence[] COMMANDS = new Sentence[]{
        new SentenceCommand(
        "USERNAME_BYID",
        "UPDATE USERNAME SET DISPLAYNAME = ?, VISIBLE = ?, IMAGE = ? WHERE ID = ?",
        "DISPLAYNAME", "VISIBLE", "IMAGE", "ID"),
        new SentenceCommand(
        "USERNAME_PASSWORD",
        "UPDATE USERNAME SET PASSWORD = ? WHERE ID = ?",
        "PASSWORD", "ID")
    };
}
