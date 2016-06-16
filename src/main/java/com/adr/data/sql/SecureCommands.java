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

package com.adr.data.sql;

/**
 *
 * @author adrian
 */
public class SecureCommands {

    public final static Sentence[] QUERIES = new Sentence[]{
        new SentenceView(
        "permission_subject",
        "select permission.id, permission.role_id, subject.id as subject_id, subject.name as subject_name, subject.code as subject_code "
        + "from permission join subject on permission.subject_id = subject.id"),
        new SentenceQuery(
        "username_visible",
        "select id, name, displayname, image from username where visible = true and active = true order by name"),
        new SentenceQuery(
        "subject_byrole",
        "select s.code, s.name from subject s join permission p on s.id = p.subject_id where p.role_id = ?", "role_id::PARAM"),
        new SentenceQuery(
        "username_byname",
        "select u.id, u.name, u.displayname, u.password, u.codecard, u.role_id, r.name as role, u.visible, u.image "
        + "from username u join role r on u.role_id = r.id "
        + "where u.name = ? and u.active = true", "name")
    };

    public final static Sentence[] COMMANDS = new Sentence[]{
        new SentenceCommand(
        "username_byname",
        "update username set displayname = ?, password = ?, visible = ?, image = ? where id = ?",
        "displayname", "password", "visible", "image", "id")
    };
}
