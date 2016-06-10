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
import com.adr.data.DataList;
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
        } else if (RequestExecute.NAME.equals(type)) {
            return new RequestExecute(fromJSONDataList(envelope.get("data")));
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

    public DataList fromJSONDataList(JsonElement element) {
        JsonObject list = element.getAsJsonObject();
        List<RecordMap> l = new ArrayList<>();
        for (JsonElement r : list.get("list").getAsJsonArray()) {
            l.add(fromJSONRecord(r));
        }
        return new DataList(l);
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

    public String toJSON(DataList obj) {
        return gson.toJson(JSONSerializer.this.toJSONElement(obj));
    }

    public JsonElement toJSONElement(DataList obj) {
        JsonArray array = new JsonArray();
        for (Record r : obj.getList()) {
            array.add(toJSONElement(r));
        }
        JsonObject datalist = new JsonObject();
        datalist.add("list", array);
        return datalist;
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

    public String toSimpleJSON(DataList obj) {
        return gsonsimple.toJson(toSimpleJSONElement(obj));
    }

    public JsonElement toSimpleJSONElement(DataList obj) {
        JsonArray array = new JsonArray();
        for (Record r : obj.getList()) {
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
