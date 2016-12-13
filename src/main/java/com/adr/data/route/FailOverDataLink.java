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
package com.adr.data.route;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.record.Record;
import java.util.List;

/**
 *
 * @author adrian
 */
public class FailOverDataLink implements DataLink {

    private final DataLink[] datalinks;

    public FailOverDataLink(DataLink... datalinks) {
        this.datalinks = datalinks;
    }
    
    @Override
    public void execute(List<Record> l) throws DataException {
        for(DataLink d : datalinks) {
            try {
                d.execute(l);
                return;
            } catch (DataException e) {
                // Ignore and go to next
            }
        }
        throw new DataException("Failed all DataLinks");
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