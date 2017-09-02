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

/**
 *
 * @author adrian
 */
public class SecureCommands {

    public final static Sentence[] QUERIES = new Sentence[]{
        new SentenceQuery(
        "SUBJECT_ROLE",
        "SELECT S.NAME, S.DISPLAYNAME FROM PERMISSION P JOIN SUBJECT S ON P.SUBJECT_ID = S.ID JOIN ROLE R ON P.ROLE_ID = R.ID WHERE R.NAME = ? AND S.NAME = ?", "ROLE__PARAM", "SUBJECT__PARAM"),
        new SentenceQuery(
        "USERNAME_BYNAME",
        "SELECT U.NAME, U.DISPLAYNAME, R.NAME AS ROLE, R.DISPLAYNAME AS DISPLAYROLE, U.PASSWORD "
        + "FROM USERNAME U JOIN ROLE R ON U.ROLE_ID = R.ID "
        + "WHERE U.NAME = ? AND U.ACTIVE = TRUE", "NAME"),
        new SentenceQuery(
        "USERNAME_VISIBLE",
        "SELECT ID AS \"ID.KEY\", NAME, DISPLAYNAME, IMAGE FROM USERNAME WHERE VISIBLE = TRUE AND ACTIVE = TRUE ORDER BY NAME"),
        new SentenceQuery(
        "SUBJECT_BYROLE",
        "SELECT S.NAME, S.DISPLAYNAME FROM PERMISSION P JOIN SUBJECT S ON P.SUBJECT_ID = S.ID JOIN ROLE R ON P.ROLE_ID = R.ID WHERE R.NAME = ?", "ROLE__PARAM"),
         new SentenceQuery(
        "USERNAMEIMAGE_BYNAME",
        "SELECT IMAGE FROM USERNAME WHERE NAME = ? AND ACTIVE = TRUE", "NAME"),
    };

    public final static Sentence[] COMMANDS = new Sentence[]{
        new SentenceCommand(
        "USERNAME_BYNAME",
        "UPDATE USERNAME SET DISPLAYNAME = ?, VISIBLE = ?, IMAGE = ? WHERE NAME = ?",
        "DISPLAYNAME", "VISIBLE", "IMAGE", "NAME"),
        new SentenceCommand(
        "USERNAMEPASSWORD_BYNAME",
        "UPDATE USERNAME SET PASSWORD = ? WHERE NAME = ?",
        "PASSWORD", "NAME")
    };
}
