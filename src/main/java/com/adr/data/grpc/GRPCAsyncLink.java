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
package com.adr.data.grpc;

import com.adr.data.DataException;
import com.adr.data.record.Header;
import com.adr.data.record.Record;
import com.adr.data.utils.RequestLink;
import com.adr.data.utils.ResponseLink;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.adr.data.AsyncLink;
import com.adr.data.proto.LinkGrpc;
import com.adr.data.proto.PRequestLink;
import com.adr.data.proto.PResponseLink;

public class GRPCAsyncLink implements AsyncLink {

    private final LinkGrpc.LinkStub stub;

    public GRPCAsyncLink(LinkGrpc.LinkStub stub) {
        this.stub = stub;
    }

    @Override
    public CompletableFuture<List<Record>> process(Header headers, List<Record> records) {

        CompletableFuture<List<Record>> completable = new CompletableFuture<>();

        try {
            String message = new RequestLink(headers, records).write();
            PRequestLink request = PRequestLink.newBuilder()
                    .setMessage(message).build();

            stub.query(request, new ResponseObserver(completable));
            
        } catch (IOException ex) {
            completable.completeExceptionally(new DataException(ex));
        }

        return completable;
    }
    
    private static class ResponseObserver implements StreamObserver<PResponseLink> {
        
        private final CompletableFuture<List<Record>> completable;
        private PResponseLink result = null;
        
        public ResponseObserver(CompletableFuture<List<Record>> completable) {
            this.completable = completable;
        }
        
        @Override
        public void onNext(PResponseLink v) {
            result = v;
        }

        @Override
        public void onError(Throwable thrwbl) {
            completable.completeExceptionally(new DataException(thrwbl));
        }

        @Override
        public void onCompleted() {
            try {
                ResponseLink envelope = ResponseLink.read(result.getMessage());
                completable.complete(envelope.getResult());
            } catch (IOException ex) {
                completable.completeExceptionally(new DataException(ex));
            } catch (DataException ex) {
                completable.completeExceptionally(ex);
            }
        }    
    }
}
