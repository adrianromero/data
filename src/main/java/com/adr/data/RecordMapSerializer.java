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
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Map;

/**
 *
 * @author adrian
 */
public class RecordMapSerializer {
    
    public final static RecordMapSerializer INSTANCE = new RecordMapSerializer();
    
    private final Gson gson;
    private final Gson gsonsimple;
    
    private RecordMapSerializer() {
        
        GsonBuilder gsonb = new GsonBuilder();
//        gsonb.serializeNulls();
//        gsonb.setPrettyPrinting();
        gsonb.registerTypeAdapter(DataList.class, new DataListAdapter());
        gsonb.registerTypeAdapter(ValuesMap.class, new ValuesMapAdapter());
        gsonb.registerTypeAdapter(Kind.class, new KindAdapter());
        gson = gsonb.create();
        
        gsonb = new GsonBuilder();
//        gsonb.serializeNulls();
//        gsonb.setPrettyPrinting();
        gsonb.registerTypeAdapter(DataList.class, new DataListAdapterSimple());
        gsonb.registerTypeAdapter(RecordMap.class, new RecordAdapterSimple());
        gsonb.registerTypeAdapter(ValuesMap.class, new ValuesMapAdapterSimple());
        gsonb.registerTypeAdapter(ValuesEntry.class, new ValuesEntryAdapterSimple());
        gsonb.registerTypeAdapter(Kind.class, new KindAdapter());
        gsonsimple = gsonb.create();        
    }
    
    public <T> T fromJSON(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz); 
    }
    
    public String toJSON(Object obj) {
        return gson.toJson(obj);
    }
     
    public String toSimpleJSON(Object obj) {
        return gsonsimple.toJson(obj);
    }   
    
    private class DataListAdapter implements JsonDeserializer<DataList> {
        @Override
        public DataList deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return new DataList(gson.fromJson(je.getAsJsonObject().get("data"), RecordMap[].class));
        }
    }
    
    private class ValuesMapAdapter implements JsonSerializer<ValuesMap>, JsonDeserializer<ValuesMap> {
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
    
    private class DataListAdapterSimple implements JsonSerializer<DataList> {
        @Override
        public JsonElement serialize(DataList t, Type type, JsonSerializationContext jsc) {
            return jsc.serialize(t.getData());
        }
    } 
    
    private class RecordAdapterSimple implements JsonSerializer<Record> {
        @Override
        public JsonElement serialize(Record t, Type type, JsonSerializationContext jsc) {
            JsonObject key = jsc.serialize(t.getKey()).getAsJsonObject();
            JsonObject value = jsc.serialize(t.getValue()).getAsJsonObject();
            JsonObject combined = new JsonObject();
            for (Map.Entry<String,JsonElement> e : key.entrySet()) {
                combined.add(e.getKey(), e.getValue());
            }
            for (Map.Entry<String,JsonElement> e : value.entrySet()) {
                combined.add(e.getKey(), e.getValue());
            }
            return combined;
        }
    } 
    
    private class ValuesMapAdapterSimple implements JsonSerializer<ValuesMap> {
        @Override
        public JsonElement serialize(ValuesMap t, Type type, JsonSerializationContext jsc) {
            return jsc.serialize(t.getMapEntries());
        }      
    }    
    
    private class ValuesEntryAdapterSimple implements JsonSerializer<ValuesEntry> {
        @Override
        public JsonElement serialize(ValuesEntry t, Type type, JsonSerializationContext jsc) {
            return jsc.serialize(t.getValue());
        }      
    }    
}
