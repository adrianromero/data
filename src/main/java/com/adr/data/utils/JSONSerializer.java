/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data.utils;

import com.adr.data.DataException;
import com.adr.data.DataList;
import com.adr.data.Kind;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.Values;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrian
 */
public class JSONSerializer {
    
    public final static JSONSerializer INSTANCE = new JSONSerializer();
    
    private final Gson gson;
    private final Gson gsonsimple;
    private final JsonParser gsonparser;
    
    private JSONSerializer() {
        
        GsonBuilder gsonb = new GsonBuilder();
//        gsonb.serializeNulls();
//        gsonb.setPrettyPrinting();
        gson = gsonb.create();
        
        gsonb = new GsonBuilder();
        gsonb.serializeNulls();
//        gsonb.setPrettyPrinting();
        gsonsimple = gsonb.create();       
        
        gsonparser = new JsonParser();
    }
    
    public EnvelopeRequest fromJSONRequest(String json) {
        JsonObject envelope = gsonparser.parse(json).getAsJsonObject();
        String type = envelope.get("type").getAsString();
        if (RequestFind.NAME.equals(type)) {
            return new RequestFind(fromJSONRecord(envelope.get("data")));
        } else if (RequestQuery.NAME.equals(type)) {
            return new RequestQuery(fromJSONRecord(envelope.get("data")));
        } else {
            throw new IllegalStateException("Envelope type invalid: " + type);
        }
    }
    
    public EnvelopeResponse fromJSONResponse(String json) {
        JsonObject envelope = gsonparser.parse(json).getAsJsonObject();
        String type = envelope.get("type").getAsString();
        if (ResponseRecord.NAME.equals(type)) {
            return new ResponseRecord(fromJSONRecord(envelope.get("data")));
        } else if (ResponseDataList.NAME.equals(type)) {
            return new ResponseDataList(fromJSONDataList(envelope.get("data")));
        } else if (ResponseError.NAME.equals(type)) {
            return new ResponseError(new DataException(envelope.get("data").getAsString()));
        } else if (ResponseSuccess.NAME.equals(type)) {
            return new ResponseSuccess();
        } else {
            throw new IllegalStateException("Envelope type invalid: " + type);
        }
    }
    
    public DataList fromJSONDataList(String json) {     
        return fromJSONDataList(gsonparser.parse(json));       
    }
    
    public Record fromJSONRecord(String json) {
        return fromJSONRecord(gsonparser.parse(json));
    }  
    
    public DataList fromJSONDataList(JsonElement element) {              
        JsonObject list = element.getAsJsonObject();
        List<RecordMap> l = new ArrayList<>();
        for (JsonElement r : list.get("list").getAsJsonArray()) {
           l.add(fromJSONRecord(r));
        }
        return new DataList(l);
    }
    
    private RecordMap fromJSONRecord(JsonElement element) {
        if (JsonNull.INSTANCE.equals(element)) {
            return null;
        }
        JsonObject o = element.getAsJsonObject();
        return new RecordMap(fromJSONValues(o.get("key")), fromJSONValues(o.get("value")));
    }
    
    private ValuesMap fromJSONValues(JsonElement element) {
        if (element == null) {
            return null;
        }
        List<ValuesEntry> l = new ArrayList<>();
        for (JsonElement r : element.getAsJsonArray()) {
           l.add(fromJSONValuesEntry(r));
        }
        return new ValuesMap(l);
    }
    
    private ValuesEntry fromJSONValuesEntry(JsonElement element) {
        JsonObject o = element.getAsJsonObject();
        Kind k = Kind.valueOf(o.get("kind").getAsString());
        Object v;
        JsonElement jvalue = o.get("value");
        if (jvalue == null) {
            v = null;
        } else {
            try {
                v = k.parseISO(jvalue.getAsString());
            } catch (DataException ex) {
                v = null;
            }
        }
        return new ValuesEntry(o.get("name").getAsString(), k, v);
    }    
    
    public String toJSON(EnvelopeRequest obj) {
        JsonObject json = new JsonObject();
        json.addProperty("type", obj.getType());
        json.add("data", obj.dataToJSON());
        return gson.toJson(json);
    }
    
    public String toJSON(EnvelopeResponse obj) {
        JsonObject json = new JsonObject();
        json.addProperty("type", obj.getType());
        json.add("data", obj.dataToJSON());
        return gson.toJson(json);
    }
    
    public String toJSON(DataList obj) {
        return gson.toJson(toJsonElement(obj));
    }
    
    public String toJSON(Record obj) {
        return gson.toJson(toJsonElement(obj));
    }
    
    public String toSimpleJSON(Record obj) {
        return gsonsimple.toJson(toSimpleJsonElement(obj));
    }   

    
    public String toSimpleJSON(DataList obj) {
        JsonArray array = new JsonArray();
        for (Record r : obj.getList()) {
            array.add(toSimpleJsonElement(r));
        }
        return gsonsimple.toJson(array);
    }   
    
    public JsonElement toJsonElement(DataList obj) {
        JsonArray array = new JsonArray();
        for (Record r : obj.getList()) {
            array.add(toJsonElement(r));
        }            
        JsonObject datalist = new JsonObject();
        datalist.add("list", array);
        return datalist;
    }
    
    public JsonElement toJsonElement(Record obj) {
        if (obj == null) {
            return JsonNull.INSTANCE;
        }
        JsonObject r = new JsonObject();
        r.add("key", toJsonElement(obj.getKey()));
        r.add("value", toJsonElement(obj.getValue()));
        return r;
    }
    
    public JsonElement toJsonElement(Values obj) {
        if (obj == null) {
            return JsonNull.INSTANCE;
        }
        JsonArray array = new JsonArray();
        for (String name: obj.getNames()) {
            JsonObject entry = new JsonObject();
            entry.addProperty("name", name);
            entry.addProperty("kind", obj.getKind(name).toString());
            try {
                entry.addProperty("value", obj.getKind(name).formatISO(obj.getValue(name)));
            } catch (DataException ex) {
                entry.add("value", JsonNull.INSTANCE);
            }
            array.add(entry);
        }
        return array;
    }
    
    private JsonElement toSimpleJsonElement(Record obj) {
        JsonObject r = new JsonObject();
        Values key = obj.getKey();
        for (String name: key.getNames()) {
            try {
                r.addProperty(name, key.getKind(name).formatISO(key.getValue(name)));
            } catch (DataException ex) {
                r.add(name, JsonNull.INSTANCE);
            }
        }
        Values value = obj.getValue();
        for (String name: value.getNames()) {
            try {
                r.addProperty(name, value.getKind(name).formatISO(value.getValue(name)));
            } catch (DataException ex) {
                r.add(name, JsonNull.INSTANCE);
            }
        }
        return r;
    }     
}
