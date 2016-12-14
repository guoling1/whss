package com.jkm.base.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangwei on 6/6/15.
 * 基于 jackson 的json序列化 <br>
 * fastjosn 不支持内部类
 */
public final class JsonUtil {
    private static final ObjectMapper OBJ_MAPPER = new ObjectMapper();

    static {
        OBJ_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJ_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtil() {
    }

    /**
     * 对象转json
     *
     * @param obj
     * @return
     */
    public static String toJsonString(final Object obj) {
        try {
            return OBJ_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("parse to json String error", e);
        }
    }

    /**
     * josnStr 转 对象
     *
     * @param jsonString
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T parseObject(final String jsonString, final TypeReference<T> typeReference) {
        try {
            return OBJ_MAPPER.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("parse to object error", e);
        }
    }

    /**
     * object 转 对象
     *
     * @param o     源对象
     * @param clazz 目标对象类型
     * @param <T>   目标对象类型
     * @return 目标对象
     */
    public static <T> T convertObject(final Object o, final Class<T> clazz) {
        return OBJ_MAPPER.convertValue(o, clazz);
    }

    /**
     * list json to list object
     *
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> convertToList(final String jsonString, final Class<T> clazz) {
        if (Strings.isNullOrEmpty(jsonString)) {
            return new ArrayList<>();
        }
        try {
            return OBJ_MAPPER.readValue(jsonString, OBJ_MAPPER.getTypeFactory().constructType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException("parse to object error", e);
        }
    }

    /**
     * list json to list object
     *
     * @param jsonString
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> List<T> convertToListByTypeReference(final String jsonString, final TypeReference<List<T>> typeReference) {
        if (Strings.isNullOrEmpty(jsonString)) {
            return new ArrayList<>();
        }
        try {
            return OBJ_MAPPER.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("parse to object error", e);
        }
    }


}
