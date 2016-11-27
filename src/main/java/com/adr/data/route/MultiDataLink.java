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
public class MultiDataLink implements DataLink {

    private final DataLink[] datalinks;

    public MultiDataLink(DataLink... datalinks) {
        this.datalinks = datalinks;
    }
    
    @Override
    public void execute(List<Record> l) throws DataException {
        for(DataLink d : datalinks) {
            d.execute(l);
        }
    }

    @Override
    public void close() throws DataException {
        DataException t = null;
        for(DataLink d : datalinks) {
            try {
                d.close();
            } catch (DataException e) {
                if (t == null) {
                    t = new DataException();
                }
                t.addSuppressed(e);
            }
        }  
        if (t != null) {
            throw t;
        }
    }   
}
