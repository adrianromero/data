/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

/**
 *
 * @author adrian
 */
public class LoginCommands {

    public final static Sentence[] QUERIES = new Sentence[]{
        new SentenceView(
        "permission_subject",
        "select permission.id, permission.role_id, subject.id as subject_id, subject.name as subject_name, subject.code as subject_code "
        + "from permission join subject on permission.subject_id = subject.id"),
        new SentenceQuery(
        "user_visible",
        "select id, name, displayname, image from user where visible = true and active = true order by name"),
        new SentenceQuery(
        "subject_byrole",
        "select s.code, s.name from subject s join permission p on s.id = p.subject_id where p.role_id = ?", "role_id"),
        new SentenceQuery(
        "user_byname",
        "select u.id, u.name, u.displayname, u.password, u.codecard, u.role_id, r.name as role, u.visible, u.image "
        + "from user u join role r on u.role_id = r.id "
        + "where u.name = ? and u.active = true", "name")
    };
}
