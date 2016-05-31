/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.utils;

import com.google.gson.JsonElement;

/**
 *
 * @author adrian
 */
public abstract class EnvelopeRequest {

    public abstract String getType();
    public abstract EnvelopeResponse process(ProcessRequest proc);
    public abstract JsonElement dataToJSON();
}
