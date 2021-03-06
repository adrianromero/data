//     Data Access is a Java library to store data
//     Copyright (C) 2019 Adrián Romero Corchado.
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

import com.adr.data.record.Header;
import com.adr.data.record.Record;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BasicLink implements Link {

    private final AsyncLink link;

    public BasicLink(AsyncLink link) {
        this.link = link;
    }

    @Override
    public List<Record> process(Header headers, List<Record> records) throws DataException {
        try {
            return link.process(headers, records).get();
        } catch (InterruptedException ex) {
            throw new DataException(ex);
        } catch (ExecutionException ex) {
            if (ex.getCause() instanceof DataException) {
                throw (DataException) ex.getCause();
            } else {
                throw new DataException(ex);
            }
        }
    }
}
