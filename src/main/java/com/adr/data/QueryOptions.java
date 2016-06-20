/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data;

/**
 *
 * @author adrian
 */
public class QueryOptions {
    
    public final static QueryOptions DEFAULT = new QueryOptions(Integer.MAX_VALUE);
    public final static QueryOptions FIND = new QueryOptions(1);
    
    private final int limit;
    
    public QueryOptions(int limit) {
        this.limit = limit;
    }
    
    public int getLimit() {
        return limit;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + this.limit;
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
        return this.limit == other.limit;
    } 
}
