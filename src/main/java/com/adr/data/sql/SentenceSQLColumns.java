//     Data Access is a Java library to store data
//     Copyright (C) 2017-2018 Adrián Romero Corchado.
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrian
 */
public class SentenceSQLColumns extends Sentence {
    @Override
    public String getName() {
        return "SQLCOLUMNS";
    }
    
    public List<Record> query(Connection c, SQLEngine engine, Record val) throws DataException {
        
        //    TABLE_CAT String => table catalog (may be null)
        //    TABLE_SCHEM String => table schema (may be null)
        //    TABLE_NAME String => table name
        //    COLUMN_NAME String => column name
        //    DATA_TYPE int => SQL type from java.sql.Types
        //    TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
        //    COLUMN_SIZE int => column size.
        //    BUFFER_LENGTH is not used.
        //    DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
        //    NUM_PREC_RADIX int => Radix (typically either 10 or 2)
        //    NULLABLE int => is NULL allowed.
        //    columnNoNulls - might not allow NULL values
        //    columnNullable - definitely allows NULL values
        //    columnNullableUnknown - nullability unknown
        //    REMARKS String => comment describing column (may be null)
        //    COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
        //    SQL_DATA_TYPE int => unused
        //    SQL_DATETIME_SUB int => unused
        //    CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
        //    ORDINAL_POSITION int => index of column in table (starting at 1)
        //    IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
        //    YES --- if the column can include NULLs
        //    NO --- if the column cannot include NULLs
        //    empty string --- if the nullability for the column is unknown
        //    SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
        //    SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
        //    SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
        //    SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
        //    IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
        //    YES --- if the column is auto incremented
        //    NO --- if the column is not auto incremented
        //    empty string --- if it cannot be determined whether the column is auto incremented
        //    IS_GENERATEDCOLUMN String => Indicates whether this is a generated column
        //    YES --- if this a generated column
        //    NO --- if this not a generated column
        //    empty string --- if it cannot be determined whether this is a generated column        
        
        // (COLLECTION.KEY: "SQLCOLUMNS", TABLE_CAT: NULL, TABLE_SCHEM: null, TABLE_NAME: "username", COLUMN_NAME: NULL, DATA_TYPE: NULL, TYPE_NAME: NULL)
        
        Variant varcat = val.get("TABLE_CAT");
        String cat = varcat.isNull() ? null : varcat.asString();
        
        Variant varschem = val.get("TABLE_SCHEM");
        String schem = varschem.isNull() ? null : varschem.asString();
        
        Variant varname = val.get("TABLE_NAME");
        String name = varname.isNull() ? null : varname.asString();
        
        Variant varcolumnname = val.get("COLUMN_NAME");
        String columnname = varcolumnname.isNull() ? null : varcolumnname.asString();

        try (ResultSet resultset = c.getMetaData().getColumns(cat, schem, name, columnname)) {
            List<Record> r = new ArrayList<>();
            while (resultset.next()) {
                r.add(new Record(read(resultset, val)));
            }
            return r;      
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }    
}
