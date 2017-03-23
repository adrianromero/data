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

package com.adr.data;

import java.util.Arrays;

/**
 *
 * @author adrian
 */
public class QueryOptions {
    
    public final static QueryOptions DEFAULT = new QueryOptions(Integer.MAX_VALUE, 0, new String[0]);
    public final static QueryOptions FIND = new QueryOptions(1, 0, new String[0]);
    
    private final int limit;
    private final int offset;
    
    private final String[] orderby;
    
    public QueryOptions(int limit, int offset, String[] orderby) {
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby; // Assume not null array
    }
    
    public static QueryOptions limit(int limit) {
        return new QueryOptions(limit, 0, new String[0]);
    }
    
    public static QueryOptions limit(int limit, int offset) {
        return new QueryOptions(limit, offset, new String[0]);
    }

    public static QueryOptions orderBy(String ... orderby) {
        return new QueryOptions(Integer.MAX_VALUE, 0, orderby);
    }
    
    public int getLimit() {
        return limit;
    }
    
    public int getOffset() {
        return offset;
    }
    
    public String[] getOrderBy() {
        return orderby;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.limit;
        hash = 23 * hash + this.offset;
        hash = 23 * hash + Arrays.deepHashCode(this.orderby);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QueryOptions other = (QueryOptions) obj;
        if (this.limit != other.limit) {
            return false;
        }
        if (this.offset != other.offset) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderby, other.orderby)) {
            return false;
        }
        return true;
    }
}
