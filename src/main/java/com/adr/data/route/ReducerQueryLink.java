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
public class ReducerQueryLink implements QueryLink {
    
    private final QueryLink link;
    private final ReducerQuery[] reducers;
    
    public ReducerQueryLink(QueryLink link, ReducerQuery... reducers) {
        this.link = link;
        this.reducers = reducers;
    }

    @Override
    public List<Record> query(Values headers, Record filter) throws DataException {
        List<Record> result;
        for (ReducerQuery r : reducers) {
            result = r.query(link, headers, filter);
            if (result != null) {
                return result;
            }
        }
        throw new DataException("Query cannot be found.");
    }

    @Override
    public void close() throws DataException {
        link.close();
    }
    
}
