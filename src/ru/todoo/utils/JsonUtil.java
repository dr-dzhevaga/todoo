package ru.todoo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.todoo.dao.generic.Identified;
import ru.todoo.dao.generic.Listed;

import java.util.List;
import java.util.stream.Collectors;

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

    public static JsonBuilder getBuilder() {
        return new JsonBuilder();
    }

    public static JsonBuilder getBuilder(Object object) {
        return new JsonBuilder(object);
    }

    public static <T extends Identified<Integer> & Listed<Integer>> JsonArray toJsonArray(List<T> objects, Integer root) {
        JsonArray result = new JsonArray();
        getChildren(objects, root).forEach(firstLevelChild -> {
            List<T> secondLevelChildren = getChildren(objects, firstLevelChild.getId());
            result.add(getBuilder(firstLevelChild).add("data", toJsonArray(secondLevelChildren)).build());
        });
        return result;
    }

    private static <T extends Identified<Integer> & Listed<Integer>> List<T> getChildren(List<T> objects, Integer rootId) {
        return objects.stream().filter(object -> rootId.equals(object.getParentId())).collect(Collectors.toList());
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
