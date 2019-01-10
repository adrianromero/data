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

import com.adr.data.CommandLink;
import com.adr.data.DataException;
import com.adr.data.proto.CommandLinkGrpc;
import com.adr.data.proto.PCommandLinkRequest;
import com.adr.data.proto.PCommandLinkResponse;
import com.adr.data.record.Header;
import com.adr.data.record.Record;
import com.adr.data.utils.RequestCommand;
import com.adr.data.utils.ResponseCommand;
import java.io.IOException;
import java.util.List;

public class GRPCCommandLink implements CommandLink {

    private final CommandLinkGrpc.CommandLinkBlockingStub stub;

    public GRPCCommandLink(CommandLinkGrpc.CommandLinkBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public void execute(Header headers, List<Record> records) throws DataException {
        try {
            String message = new RequestCommand(headers, records).write();
            PCommandLinkRequest request = PCommandLinkRequest.newBuilder()
                    .setMessage(message).build();

            PCommandLinkResponse response = stub.execute(request);

            ResponseCommand envelope = ResponseCommand.read(response.getMessage());
            envelope.getResult();
        } catch (IOException | RuntimeException ex) {
            throw new DataException(ex);
        }
    }
}
