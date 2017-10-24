package com.woders.apiprovider.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.woders.apiprovider.BudPage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * title:
 * desc: json转化工具
 * Created by jiangguangming on 2015/10/29.
 */
public class JsonUtil {

    //json - map
    public static Map<String, Object> jsonToMap(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Map.Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ) {
            Map.Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JsonArray)
                map.put((String) key, toList((JsonArray) value));
            else if (value instanceof JsonObject)
                map.put((String) key, toMap((JsonObject) value));
            else
                map.put((String) key, value);
        }
        return map;
    }

    //json - object
    public static <T> Object jsonToObject(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

    public static Map<String, Object> toMap(JsonObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Map.Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ) {
            Map.Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JsonArray)
                map.put(key, toList((JsonArray) value));
            else if (value instanceof JsonObject)
                map.put(key, toMap((JsonObject) value));
            else
                map.put(key, value);
        }
        return map;
    }


    /**
     * 将JSONArray对象转换成List集合
     *
     * @param json
     * @return
     */
    public static List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < json.size(); i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                list.add(toMap((JsonObject) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }

    public static <T> ArrayList<T> jsonToList(String json, Class<T> classOfT) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjs = new Gson().fromJson(json, type);
        if (json != null && json.length() > 0) {
            ArrayList<T> listOfT = new ArrayList<T>();

            for (JsonObject jsonObj : jsonObjs) {
                listOfT.add(new Gson().fromJson(jsonObj, classOfT));
            }
            return listOfT;
        } else {
            return null;
        }
    }


    public static <T> BudPage<T> jsonToPage(String json, Class<T> classOfT) {
        Type type = new TypeToken<BudPage<JsonObject>>() {
        }.getType();
        BudPage<JsonObject> jsonObjs = new Gson().fromJson(json, type);
        ArrayList<T> listOfT = new ArrayList<T>();
        BudPage<T> budPage = new BudPage<T>();
        budPage.setTimestamp(jsonObjs.getTimestamp());
        budPage.setPagesize(jsonObjs.getPagesize());
        budPage.setStart(jsonObjs.getStart());
        if (jsonObjs.getData() == null) {
            budPage.setData(null);
        } else {
            for (JsonObject jsonObj : jsonObjs.getData()) {
                listOfT.add(new Gson().fromJson(jsonObj, classOfT));
            }
            budPage.setData(listOfT);
        }
        return budPage;
    }



}
