package com.adr.data.test

import com.adr.data.QueryLink
import com.adr.data.`var`.*
import com.adr.data.record.Entry
import com.adr.data.record.Record
import com.adr.data.record.RecordMap
import com.adr.data.security.ReducerLogin
import org.junit.Assert
import org.junit.Test

/**
 * Created by adrian on 22/09/17.
 */

class QueryAndExec {

    @Test
    fun firstQuery() {
        SourceLink.createDataQueryLink()
        try {
            // Login
            val authorization = ReducerLogin.login(SourceLink.getQueryLink(), "admin", "admin")
            val header = RecordMap(
                    "Authorization" to authorization)

            // Insert
            SourceLink.getDataLink().execute(
                    header,
                    arrayOf<Record>(
                            RecordMap(
                                    "COLLECTION.KEY" to "USERNAME",
                                    "ID.KEY" to "newid",
                                    "NAME" to "newuser",
                                    "DISPLAYNAME" to "New User",
                                    "CODECARD" to "123457",
                                    "ROLE_ID" to "g",
                                    "VISIBLE" to true,
                                    "ACTIVE" to true)))
            val r = loadUser(SourceLink.getQueryLink(), header, "newid")
            Assert.assertEquals("newuser", r.getString("NAME"))
            Assert.assertEquals("New User", r.getString("DISPLAYNAME"))
            // Assert.assertEquals(true, r["VISIBLE"].boolean)

            // Delete newid
            SourceLink.getDataLink().execute(
                    header,
                    arrayOf<Record>(
                            RecordMap(
                                    "COLLECTION.KEY" to "USERNAME",
                                    "ID.KEY" to "newid")));

//            val r2 = loadUser(SourceLink.getQueryLink(), header, "newid");
//            Assert.assertNull(r2);


        } finally {
            SourceLink.destroyDataQueryLink()
        }
    }

    private fun loadUser(link: QueryLink, header: Record, id: String): Record {
        return link.find(header,
                RecordMap(
                        "COLLECTION.KEY" to "USERNAME",
                        "ID.KEY" to id,
                        "NAME" to NULL.STRING,
                        "DISPLAYNAME" to NULL.STRING,
                        "CODECARD" to NULL.STRING,
                        "ROLE_ID" to NULL.STRING,
                        "VISIBLE" to NULL.BOOLEAN,
                        "ACTIVE" to NULL.BOOLEAN))
    }
}





