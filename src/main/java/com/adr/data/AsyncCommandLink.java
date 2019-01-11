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
package com.adr.data;

import com.adr.data.record.Header;
import com.adr.data.record.Record;
import java.util.Arrays;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncCommandLink {

    public CompletableFuture<Void> execute(Header headers, List<Record> records);

    public default CompletableFuture<Void> execute(Record... records) {
        return execute(Header.EMPTY, Arrays.asList(records));
    }

    public default CompletableFuture<Void> execute(Header headers, Record... records) {
        return execute(headers, Arrays.asList(records));
    }
}
