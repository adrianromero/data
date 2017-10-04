//     Data Access is a Java library to store data
//     Copyright (C) 2017 Adrián Romero Corchado.
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
package com.adr.data.async;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.record.Record;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by adrian on 4/10/17.
 */
public class AsyncDataLink {

    private final DataLink datalink;
    private final Executor executor;

    public AsyncDataLink(DataLink datalink, Executor executor) {
        this.datalink = datalink;
        this.executor = executor;
    }

    public AsyncDataLink(DataLink datalink) {
        this(datalink, ForkJoinPool.commonPool());
    }

    public CompletableFuture<Void> execute(Record headers, List<Record> records) {
        return CompletableFuture.runAsync(() -> {
            try {
                datalink.execute(headers, records);
            } catch (DataException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    public CompletableFuture<Void> execute(Record... records) {
        return CompletableFuture.runAsync(() -> {
            try {
                datalink.execute(records);
            } catch (DataException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    public CompletableFuture<Void> execute(Record headers, Record[] records) {
        return CompletableFuture.runAsync(() -> {
            try {
                datalink.execute(headers, records);
            } catch (DataException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }
}
