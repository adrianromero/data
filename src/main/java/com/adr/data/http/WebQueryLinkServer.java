/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.http;

import com.adr.data.QueryLink;
import com.adr.data.utils.ProcessRequest;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 * @author adrian
 */
public class WebQueryLinkServer implements Route {
    
    private static final String SESSIONNAME = "DATA_SESSION";
    private static final Logger LOG = Logger.getLogger(WebQueryLinkServer.class.getName());
    
    private final QueryLink link;
    private final AssignableSession assignsession;

    public WebQueryLinkServer(QueryLink link, AssignableSession assignsession) {
        this.link = link;
        this.assignsession = assignsession;
    }
    
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String message;
        if ("GET".equals(request.protocol())) {
            message = request.queryParams("filter");
        } else {
            message = request.body();
        }   

        assignsession.setSerializableSession(request.session().attribute(SESSIONNAME));       
        String result = ProcessRequest.serverQueryProcess(link, message, LOG);           
        request.session().attribute(SESSIONNAME, assignsession.getSerializableSession());
        return result;
    }
}
