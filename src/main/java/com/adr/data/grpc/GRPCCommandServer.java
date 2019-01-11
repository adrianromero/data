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
import com.adr.data.CommandLink;
import com.adr.data.proto.CommandLinkGrpc;
import com.adr.data.proto.PCommandLinkRequest;
import com.adr.data.proto.PCommandLinkResponse;
import com.adr.data.utils.RequestCommand;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class GRPCCommandServer extends CommandLinkGrpc.CommandLinkImplBase {
    
    private static final Logger LOG = Logger.getLogger(GRPCCommandServer.class.getName());
    
    private final CommandLink commandlink;

    public GRPCCommandServer(CommandLink commandlink) {
        this.commandlink = commandlink;
    }
    
    @Override
    public void execute(PCommandLinkRequest request, StreamObserver<PCommandLinkResponse> responseObserver) {
        
        try {
            String result = RequestCommand.serverCommandProcess(commandlink, request.getMessage(), LOG);
            
            PCommandLinkResponse response = PCommandLinkResponse.newBuilder()
                    .setMessage(result)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();           
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
