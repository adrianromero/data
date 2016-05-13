/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author adrian
 */
public class JSONBuilder {
    
    public final static JSONBuilder INSTANCE = new JSONBuilder();
    
    private final Gson gson;
    
    private JSONBuilder() {
        
        GsonBuilder gsonb = new GsonBuilder();
        gsonb.serializeNulls();
//        gsonb.setPrettyPrinting();
        gsonb.registerTypeAdapter(DataList.class, new DataListAdapter());
        gsonb.registerTypeAdapter(ValuesMap.class, new MapValueAdapter());
        gsonb.registerTypeAdapter(Kind.class, new KindAdapter());
        gson = gsonb.create();
    }
    
    public <T> T fromJSON(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz); 
    }
    
    public String toJSON(Object obj) {
        return gson.toJson(obj);
    }
    
    private class DataListAdapter implements JsonDeserializer<DataList> {
        @Override
        public DataList deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return new DataList(gson.fromJson(je.getAsJsonObject().get("data"), RecordMap[].class));
        }
    }
    
    private class MapValueAdapter implements JsonSerializer<ValuesMap>, JsonDeserializer<ValuesMap> {
        @Override
        public JsonElement serialize(ValuesMap t, Type type, JsonSerializationContext jsc) {
            return gson.toJsonTree(t.getEntries());
        }      

        @Override
        public ValuesMap deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return new ValuesMap(gson.fromJson(je, ValuesEntry[].class));
        }
    }
     
    private class KindAdapter implements JsonSerializer<Kind>, JsonDeserializer<Kind> {
        @Override
        public JsonElement serialize(Kind t, Type type, JsonSerializationContext jsc) {
            return new JsonPrimitive(t.toString());
        }      
        @Override
        public Kind deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return Kind.valueOf(je.getAsJsonPrimitive().getAsString());
        }
    }    
}
