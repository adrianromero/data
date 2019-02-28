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

import io.grpc.stub.StreamObserver;
import com.adr.data.utils.RequestLink;
import java.io.IOException;
import java.util.logging.Logger;
import com.adr.data.Link;
import com.adr.data.proto.LinkGrpc;
import com.adr.data.proto.PRequestLink;
import com.adr.data.proto.PResponseLink;

/**
 *
 * @author adrian
 */
public class GRPCServer extends LinkGrpc.LinkImplBase {
    
    private static final Logger LOG = Logger.getLogger(GRPCServer.class.getName());
    
    private final Link link;

    public GRPCServer(Link link) {
        this.link = link;
    }
    
    @Override
    public void query(PRequestLink request, StreamObserver<PResponseLink> responseObserver) {
            
        try {
            String result = RequestLink.serverProcess(link, request.getMessage(), LOG);
            
            PResponseLink response = PResponseLink.newBuilder()
                    .setMessage(result)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();           
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
