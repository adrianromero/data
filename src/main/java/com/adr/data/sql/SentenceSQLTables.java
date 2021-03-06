//     Data Access is a Java library to store data
//     Copyright (C) 2017-2019 Adrián Romero Corchado.
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

import com.adr.data.DataException;
import com.adr.data.record.Record;
import com.adr.data.var.Variant;
import com.adr.data.varrw.Variants;
import com.google.common.collect.ImmutableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author adrian
 */
public class SentenceSQLTables extends Sentence {
    
    private final static String[] TABLE_TYPES = new String[]{"TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM"};

    @Override
    public String getName() {
        return "SQLTABLES";
    }
    
    @Override
    public void query(Connection c, SQLEngine engine, Record val, ImmutableList.Builder<Record> result) throws DataException {
        //    TABLE_CAT String => table catalog (may be null)
        //    TABLE_SCHEM String => table schema (may be null)
        //    TABLE_NAME String => table name
        //    TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
        //    REMARKS String => explanatory comment on the table
        //    TYPE_CAT String => the types catalog (may be null)
        //    TYPE_SCHEM String => the types schema (may be null)
        //    TYPE_NAME String => type name (may be null)
        //    SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null)
        //    REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)   
        
        // (COLLECTION.KEY: "SQLTABLES", TABLE_CAT: NULL, TABLE_SCHEM: null, TABLE_NAME: NULL, TABLE_TYPE: NULL)
        
        Variant varcat = val.get("TABLE_CAT");
        String cat = Variants.isNull(varcat) ? null : varcat.asString();
        
        Variant varschem = val.get("TABLE_SCHEM");
        String schem = Variants.isNull(varschem) ? null : varschem.asString();
        
        Variant varname = val.get("TABLE_NAME");
        String name = Variants.isNull(varname) ? null : varname.asString();
        
        Variant vartype = val.get("TABLE_TYPE");
        String[] tabletypes = Variants.isNull(vartype) ? TABLE_TYPES : new String[]{ vartype.asString() };

        try (ResultSet resultset = c.getMetaData().getTables(cat, schem, name, tabletypes)) {
            while (resultset.next()) {
                result.add(read(resultset, val));
            }     
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }    
}
