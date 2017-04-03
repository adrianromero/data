/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.route;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.record.Record;
import com.adr.data.record.Values;
import java.util.List;

/**
 *
 * @author adrian
 */
public class ReducerQueryIdentity implements ReducerQuery {
    
    public static final ReducerQuery INSTANCE = new ReducerQueryIdentity();
    
    private ReducerQueryIdentity() {}

    @Override
    public List<Record> query(QueryLink link, Values headers, Record filter) throws DataException {
        return link.query(headers, filter);
    }
    
}
