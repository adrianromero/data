/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.sql;

import com.adr.data.sqlstrategy.SQLStrategy;
import com.adr.data.DataLink;
import com.adr.data.DataException;
import com.adr.data.DataList;
import com.adr.data.Record;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author adrian
 */
public class SQLDataLink implements DataLink {

    private final DataSource ds;
    private final SQLStrategy strategy;

    public SQLDataLink(DataSource ds, SQLStrategy strategy) {
        this.ds = ds;
        this.strategy = strategy;
    }

    public SQLDataLink(DataSource ds) {
        this(ds, new SQLStrategy());
    }

    @Override
    public void execute(DataList l) throws DataException {
        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            for (Record keyval : l.getData()) {
                strategy.execute(c, keyval);
            }
            c.commit();
        } catch (SQLException ex) {
            throw new DataException(ex);
        }
    }
}
