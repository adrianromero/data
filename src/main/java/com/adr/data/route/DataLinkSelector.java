/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.route;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public interface DataLinkSelector {
    
    public DataLink getDataLink(Record filter) throws DataException ;
    public void close() throws DataException;    
}
