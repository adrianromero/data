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
import java.io.IOException;
import java.util.List;
import com.adr.data.Link;
import com.adr.data.proto.LinkGrpc;
import com.adr.data.proto.PRequestLink;
import com.adr.data.proto.PResponseLink;

public class GRPCLink implements Link {

    private final LinkGrpc.LinkBlockingStub stub;

    public GRPCLink(LinkGrpc.LinkBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public List<Record> process(Header headers, List<Record> records) throws DataException {
        try {
            String message = new RequestLink(headers, records).write();
            PRequestLink request = PRequestLink.newBuilder()
                    .setMessage(message).build();

            PResponseLink response = stub.query(request);

            ResponseLink envelope = ResponseLink.read(response.getMessage());
            return envelope.getResult();
        } catch (IOException | RuntimeException ex) {
            throw new DataException(ex);
        }
    }
}
