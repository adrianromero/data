/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.testlinks;

import com.adr.data.DataQueryLink;
import com.adr.data.http.WebDataQueryLink;

/**
 *
 * @author adrian
 */
public class DataQueryLinkHTTP implements DataQueryLinkBuilder {

    @Override
    public DataQueryLink createDataQueryLink() {
        return new WebDataQueryLink(System.getProperty("http.url"));
    }
}
