//     Data Access is a Java library to store data
//     Copyright (C) 2016-2017 Adrián Romero Corchado.
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
import com.adr.data.var.Kind;
import com.adr.data.record.RecordMap;
import com.adr.data.var.ISOParameters;
import com.adr.data.var.ISOResults;
import com.adr.data.var.Variant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import com.adr.data.record.Record;

/**
 *
 * @author adrian
 */
public class JSON {

    public final static JSON INSTANCE = new JSON();

    private final Gson gson;
    private final Gson gsonsimple;
    private final JsonParser gsonparser;

    private JSON() {

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
        if (RequestQuery.NAME.equals(type)) {
            JsonObject data = envelope.get("data").getAsJsonObject();
            return new RequestQuery(JSON.this.fromJSONRecord(data.get("headers")), JSON.this.fromJSONRecord(data.get("filter")));
        } else if (RequestExecute.NAME.equals(type)) {
            JsonObject data2 = envelope.get("data").getAsJsonObject();
            return new RequestExecute(JSON.this.fromJSONRecord(data2.get("headers")), JSON.this.fromJSONListRecord(data2.get("list")));
        } else {
            throw new IllegalStateException("Envelope type invalid: " + type);
        }
    }

    public EnvelopeResponse fromJSONResponse(String json) {
        JsonObject envelope = gsonparser.parse(json).getAsJsonObject();
        String type = envelope.get("type").getAsString();
        if (ResponseListRecord.NAME.equals(type)) {
            return new ResponseListRecord(JSON.this.fromJSONListRecord(envelope.get("data")));
        } else if (ResponseError.NAME.equals(type)) {
            JsonObject jsonex = envelope.get("data").getAsJsonObject();
            String name = jsonex.get("exception").getAsString();
            String message = jsonex.get("message").getAsString();
            Throwable t;
            try {
                t = (Throwable) Class.forName(name).getConstructor(String.class).newInstance(message);
            } catch (ClassCastException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                t = new DataException("Exception name: " + name + ", with message: " + message);
            }
            return new ResponseError(t);
        } else if (ResponseSuccess.NAME.equals(type)) {
            return new ResponseSuccess();
        } else {
            throw new IllegalStateException("Envelope type invalid: " + type);
        }
    }

    public List<Record> fromJSONListRecord(String json) {
        return JSON.this.fromJSONListRecord(gsonparser.parse(json));
    }

    public List<Record> fromJSONListRecord(JsonElement element) {
        List<Record> l = new ArrayList<>();
        for (JsonElement r : element.getAsJsonArray()) {
            l.add(JSON.this.fromJSONRecord(r));
        }
        return l;
    }
    
    public Record fromResRecord(String res) throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/" + res + ".json")) {
            Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
            return fromJSONRecord(s.next());
        }         
    }

    public Record fromJSONRecord(String json) {
        return JSON.this.fromJSONRecord(gsonparser.parse(json));
    }

    private Record fromJSONRecord(JsonElement element) {
        if (element == null || element.equals(JsonNull.INSTANCE)) {
            return null;
        }
        JsonArray array = element.getAsJsonArray();
        LinkedHashMap<String, Variant> entries = new LinkedHashMap<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject o = array.get(i).getAsJsonObject();
            Kind k = Kind.valueOf(o.get("kind").getAsString());
            String iso;
            JsonElement jvalue = o.get("value");
            if (jvalue == null || jvalue.equals(JsonNull.INSTANCE)) {
                iso = null;
            } else {
                iso = jvalue.getAsString();
            }
            try {
                entries.put(o.get("name").getAsString(), k.read(new ISOResults(iso)));
            } catch (DataException ex) {
                throw new IllegalArgumentException(ex);
            }            
        }
        return new RecordMap(entries);
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

    public String toJSON(List<Record> obj) {
        return gson.toJson(toJSONElement(obj));
    }

    public JsonElement toJSONElement(List<Record> obj) {
        JsonArray array = new JsonArray();
        for (Record v : obj) {
            array.add(toJSONElement(v));
        }
        return array;
    }
    
    public String toJSON(Record obj) {
        return gson.toJson(toJSONElement(obj));
    }

    public JsonElement toJSONElement(Record obj) {
        if (obj == null) {
            return JsonNull.INSTANCE;
        }
        JsonArray array = new JsonArray();
        ISOParameters params = new ISOParameters();
        for (String name : obj.getNames()) {
            JsonObject entry = new JsonObject();
            Variant v = obj.get(name);
            entry.addProperty("name", name);
            entry.addProperty("kind", v.getKind().toString());
            try {             
                v.write(params);
                entry.addProperty("value", params.toString());
            } catch (DataException ex) {
                throw new IllegalArgumentException(ex);
            }
            array.add(entry);
        }
        return array;
    }

    public String toSimpleJSON(List<Record> obj) {
        return gsonsimple.toJson(toSimpleJSONElement(obj));
    }

    public JsonElement toSimpleJSONElement(List<Record> obj) {
        JsonArray array = new JsonArray();
        for (Record v : obj) {
            array.add(toSimpleJSONElement(v));
        }
        return array;
    }

    public String toSimpleJSON(Record obj) {
        return gsonsimple.toJson(toSimpleJSONElement(obj));
    }
    
    private JsonElement toSimpleJSONElement(Record obj) {
        if (obj == null) {
            return JsonNull.INSTANCE;
        }
        JsonObject r = new JsonObject();
        populateValues(r, obj);
        return r;
    }

    private void populateValues(JsonObject r, Record obj) {
        if (obj == null) {
            return;
        }
        ISOParameters params = new ISOParameters();
        for (String name : obj.getNames()) {
            Variant v = obj.get(name);
            try {
                v.write(params);
                r.addProperty(name, params.toString());
            } catch (DataException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }
}
