package ru.todoo.utils;

import com.google.gson.*;

/**
 * Created by Dmitriy Dzhevaga on 28.11.2015.
 */
public class JsonUtil {
    public static <T> T toObject(String json, Class<T> type) {
        return new Gson().fromJson(json, type);
    }

    public static JsonArray toJsonArray(Object object) {
        return new Gson().toJsonTree(object).getAsJsonArray();
    }

    public static JsonObject toJsonObject(Object object) {
        return new Gson().toJsonTree(object).getAsJsonObject();
    }

    public static JsonArray toJsonArray(String json) {
        return new JsonParser().parse(json).getAsJsonArray();
    }

    public static JsonObject toJsonObject(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }

    public static JsonBuilder getBuilder() {
        return new JsonBuilder();
    }

    public static JsonBuilder getBuilder(Object object) {
        return new JsonBuilder(object);
    }

    public static class JsonBuilder {
        private final JsonObject jsonObject;

        public JsonBuilder() {
            jsonObject = new JsonObject();
        }

        public JsonBuilder(Object object) {
            jsonObject = new Gson().toJsonTree(object).getAsJsonObject();
        }

        public JsonBuilder addProperty(String property, String value) {
            jsonObject.addProperty(property, value);
            return this;
        }

        public JsonBuilder addProperty(String property, Number value) {
            jsonObject.addProperty(property, value);
            return this;
        }

        public JsonBuilder addProperty(String property, Boolean value) {
            jsonObject.addProperty(property, value);
            return this;
        }

        public JsonBuilder addProperty(String property, Character value) {
            jsonObject.addProperty(property, value);
            return this;
        }

        public JsonBuilder add(String property, JsonElement value) {
            jsonObject.add(property, value);
            return this;
        }

        public JsonObject build() {
            return jsonObject;
        }
    }
}
