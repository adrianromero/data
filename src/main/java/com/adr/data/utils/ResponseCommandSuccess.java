//     Data Access is a Java library to store data
//     Copyright (C) 2017-2019 Adrián Romero Corchado.
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

package com.adr.data.utils;

import com.adr.data.DataException;
import com.adr.data.recordparser.CodePoint;
import com.adr.data.recordparser.IOExceptionMessage;
import com.adr.data.recordparser.Loader;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author adrian
 */
public class ResponseCommandSuccess extends ResponseCommand {
    
    public static final String NAME = "SUCCESS";
    
    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public void write(Writer w) throws IOException {
        w.append(NAME);
    }
    
    @Override
    public void getResult() throws DataException {
    }
    
    public static ResponseCommand readData(Loader loader) throws IOException {
        if (CodePoint.isEOF(loader.getCP())) {
            return new ResponseCommandSuccess();
        } else {
            throw IOExceptionMessage.createExpected(loader, -1);
        }        
    }
}
