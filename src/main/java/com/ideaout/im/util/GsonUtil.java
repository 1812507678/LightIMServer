package com.ideaout.im.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class GsonUtil {
    private static Gson gson = new Gson();

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        try {
            return gson.fromJson(json, typeOfT);
        }
        catch (Exception e) {
            System.out.println("json转换异常"+e);
            return null;
        }
    }

    public static Map<String, Object> toMap(String json) {
        return (Map)(new Gson()).fromJson(json, (new TypeToken<Map<String, Object>>() {
        }).getType());
    }



}