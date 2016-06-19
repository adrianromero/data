/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.http;

import com.adr.data.DataLink;
import com.adr.data.utils.ProcessRequest;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 * @author adrian
 */
public class WebDataLinkServer implements Route {
    
    private static final Logger LOG = Logger.getLogger(WebDataLinkServer.class.getName());
    
    private final DataLink link;

    public WebDataLinkServer(DataLink link) {
        this.link = link;
    }
    
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String message;
        if ("GET".equals(request.protocol())) {
            message = request.queryParams("filter");
        } else {
            message = request.body();
        }        
        return ProcessRequest.serverDataProcess(link, message, LOG);        
    } 
}
