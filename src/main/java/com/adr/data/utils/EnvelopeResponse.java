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
public abstract class EnvelopeResponse {
    public abstract String getType();  
    public abstract JsonElement dataToJSON();
}
