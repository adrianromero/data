/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.route;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.record.Record;
import java.util.List;

/**
 *
 * @author adrian
 */
public class RouteDataLink implements DataLink {

    private final QueryLinkSelector selector;

    public RouteDataLink(QueryLinkSelector selector) {
        this.selector = selector;
    }
    
    @Override
    public void execute(List<Record> l) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() throws DataException {
        
    }
    
}
