//     Data Access is a Java library to store data
//     Copyright (C) 2016-2018 Adrián Romero Corchado.
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

package com.adr.data.record;

import com.adr.data.DataException;
import com.adr.data.var.Variant;

/**
 *
 * @author adrian
 */
public class Records {
   
    public static String getCollection(Record record) throws DataException {
        Variant collectionkey = record.get("COLLECTION.KEY");
        if (collectionkey.isNull()) {
            throw new DataException("COLLECTION.KEY does not exists for record.");
        }
        return collectionkey.asString();
    }    
   
    public static String getCollection(Record record, String defaultcollection) throws DataException {
        Variant collectionkey = record.get("COLLECTION.KEY");
        if (collectionkey.isNull()) {
            return defaultcollection;
        }
        return collectionkey.asString();
    }    
    
    public static boolean isDeleteSentence(Record val) {
        for (String name : val.getNames()) {
            if (!name.contains("..") && !name.endsWith(".KEY")) {
                return false;
            }
        }
        return true;
    }  
    
    public static int getLimit(Record record) {
        Variant v = record.get("..LIMIT");
        return v.isNull() ? Integer.MAX_VALUE : v.asInteger();
    }
    
    public static int getOffset(Record record) {
        Variant v = record.get("..OFFSET");
        return v.isNull() ? 0 : v.asInteger();
    }
    
    public static String[] getOrderBy(Record record) {
        Variant v = record.get("..ORDERBY");
        return v.isNull() ? new String[0] : v.asString().split("\\s+");
    }
}
