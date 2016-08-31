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

/**
 *
 * @author adrian
 */
public class QueryOptions {
    
    public final static QueryOptions DEFAULT = new QueryOptions(Integer.MAX_VALUE);
    public final static QueryOptions FIND = new QueryOptions(1);
    
    private final int limit;
    private final int offset;
    
    public QueryOptions(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }
    
    public QueryOptions(int limit) {
        this(limit, 0);
    }
    
    public int getLimit() {
        return limit;
    }
    
    public int getOffset() {
        return offset;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.limit;
        hash = 17 * hash + this.offset;
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
        return true;
    }
}
