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
import com.adr.data.QueryOptions;
import com.adr.data.var.Kind;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.Values;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import com.adr.data.var.Variant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            return new RequestQuery(fromJSONRecord(data.get("filter")), fromJSONOptions(data.get("options")));
        } else if (RequestExecute.NAME.equals(type)) {
            return new RequestExecute(JSON.this.fromJSONListRecord(envelope.get("data")));
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
    
    public List<Record> clone(List<Record> list) {
        return fromJSONListRecord(toJSONElement(list));    
    }

    public List<Record> fromJSONListRecord(String json) {
        return fromJSONListRecord(gsonparser.parse(json));
    }

    public List<Record> fromJSONListRecord(JsonElement element) {
        List<Record> l = new ArrayList<>();
        for (JsonElement r : element.getAsJsonArray()) {
            l.add(fromJSONRecord(r));
        }
        return l;
    }
    
    public Record clone(Record record) {
        return fromJSONRecord(toJSONElement(record));    
    }

    public Record fromJSONRecord(String json) {
        return fromJSONRecord(gsonparser.parse(json));
    }

    public RecordMap fromJSONRecord(JsonElement element) {
        if (element == null || element.equals(JsonNull.INSTANCE)) {
            return null;
        }
        JsonObject o = element.getAsJsonObject();
        return new RecordMap(fromJSONValues(o.get("key")), fromJSONValues(o.get("value")));
    }
    
    public QueryOptions fromJSONOptions(String json) {
        return fromJSONOptions(gsonparser.parse(json));
    }
    
    public QueryOptions fromJSONOptions(JsonElement element) {
        if (element == null || element.equals(JsonNull.INSTANCE)) {
            return QueryOptions.DEFAULT;
        } else {
            return new QueryOptions(element.getAsJsonObject().get("limit").getAsInt());
        }
    }
    
    private ValuesMap fromJSONValues(JsonElement element) {
        if (element == null || element.equals(JsonNull.INSTANCE)) {
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
        String iso;
        JsonElement jvalue = o.get("value");
        if (jvalue == null || jvalue.equals(JsonNull.INSTANCE)) {
            iso = null;
        } else {
            iso = jvalue.getAsString();
        }
        try {
            return new ValuesEntry(o.get("name").getAsString(), k.buildISO(iso));
        } catch (DataException ex) {
            throw new IllegalArgumentException(ex);
        }
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
        for (Record r : obj) {
            array.add(toJSONElement(r));
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
        JsonObject r = new JsonObject();
        r.add("key", toJSONElement(obj.getKey()));
        r.add("value", toJSONElement(obj.getValue()));
        return r;
    }

    public JsonElement toJSONElement(QueryOptions obj) {
        if (QueryOptions.DEFAULT.equals(obj)) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject r = new JsonObject();
            r.addProperty("limit", obj.getLimit());
            return r;
        }
    }
    
    public JsonElement toJSONElement(Values obj) {
        if (obj == null) {
            return JsonNull.INSTANCE;
        }
        JsonArray array = new JsonArray();
        for (String name : obj.getNames()) {
            JsonObject entry = new JsonObject();
            Variant v = obj.get(name);
            entry.addProperty("name", name);
            entry.addProperty("kind", v.getKind().toString());
            try {
                entry.addProperty("value", v.asISO());
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
        for (Record r : obj) {
            array.add(toSimpleJSONElement(r));
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
        populateValues(r, obj.getKey());
        populateValues(r, obj.getValue());
        return r;
    }

    private void populateValues(JsonObject r, Values obj) {
        if (obj == null) {
            return;
        }
        for (String name : obj.getNames()) {
            Variant v = obj.get(name);
            try {
                r.addProperty(name, v.asISO());
            } catch (DataException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }
}