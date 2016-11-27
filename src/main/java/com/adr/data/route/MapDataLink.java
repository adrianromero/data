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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author adrian
 */
public class MapDataLink implements DataLink {

    private final DataLink datalink;
    private final Function<? super Record,? extends Stream<? extends Record>> mapper;

    public MapDataLink(DataLink datalink, Function<? super Record,? extends Stream<? extends Record>> mapper) {
        this.datalink = datalink;
        this.mapper = mapper;
    }
    
    @Override
    public void execute(List<Record> l) throws DataException {
        List<Record> l2 = l.stream().flatMap(mapper).collect(Collectors.toList());
        if (!l2.isEmpty()) {
            datalink.execute(l2);
        }
    }

    @Override
    public void close() throws DataException {
        datalink.close();
    }
    
}
