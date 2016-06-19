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
package com.adr.data.utils;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.QueryLink;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public abstract class ProcessRequest {

    public EnvelopeResponse find(RequestFind req) {
        return other(req);
    }

    public EnvelopeResponse query(RequestQuery req) {
        return other(req);
    }

    public EnvelopeResponse execute(RequestExecute req) {
        return other(req);
    }

    public EnvelopeResponse other(EnvelopeRequest env) {
        throw new UnsupportedOperationException("Request type not supported: " + env.getType());
    }

    public static String serverQueryProcess(QueryLink link, String message, Logger logger) {

        EnvelopeRequest envrequest = JSONSerializer.INSTANCE.fromJSONRequest(message);

        logger.log(Level.CONFIG, "Processing {0} : {1}.", new Object[]{envrequest.getType(), message});

        EnvelopeResponse envresponse = envrequest.process(new ProcessRequest() {
            @Override
            public EnvelopeResponse find(RequestFind req) {
                try {
                    return new ResponseRecord(link.find(req.getFilter()));
                } catch (DataException ex) {
                    logger.log(Level.SEVERE, "Cannot execute find request.", ex);
                    return new ResponseError(ex);
                }
            }

            @Override
            public EnvelopeResponse query(RequestQuery req) {
                try {
                    return new ResponseListRecord(link.query(req.getFilter()));
                } catch (DataException ex) {
                    logger.log(Level.SEVERE, "Cannot execute query request.", ex);
                    return new ResponseError(ex);
                }
            }

            @Override
            public EnvelopeResponse other(EnvelopeRequest req) {
                logger.log(Level.SEVERE, "Request type not supported :", new Object[]{req.getType()});
                return new ResponseError(new UnsupportedOperationException("Request type not supported : " + req.getType()));
            }
        });

        return JSONSerializer.INSTANCE.toJSON(envresponse);
    }

    public static String serverDataProcess(DataLink link, String message, Logger logger) {
        
        EnvelopeRequest request = JSONSerializer.INSTANCE.fromJSONRequest(message);

        logger.log(Level.CONFIG, "Processing {0} : {1}.", new Object[]{request.getType(), message});

        EnvelopeResponse response = request.process(new ProcessRequest() {
            @Override
            public EnvelopeResponse execute(RequestExecute req) {
                try {
                    link.execute(req.getListRecord());
                    return new ResponseSuccess();
                } catch (DataException ex) {
                    logger.log(Level.SEVERE, "Cannot execute request.", ex);
                    return new ResponseError(ex);
                }
            }

            @Override
            public EnvelopeResponse other(EnvelopeRequest req) {
                logger.log(Level.SEVERE, "Request type not supported :", new Object[]{req.getType()});
                return new ResponseError(new UnsupportedOperationException("Request type not supported : " + req.getType()));
            }
        });

        return JSONSerializer.INSTANCE.toJSON(response);
    }
}
