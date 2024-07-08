package com.wallet.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {

    private JsonConverter() {

    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new Gson();

    public static <T> T convertJsonToJavaUsingJackson(String json, Class<T> targetClass) throws Exception {
        return objectMapper.readValue(json, targetClass);
    }

    public static <T> T convertJsonToJavaUsingGson(String json, Class<T> targetClass) {
        return gson.fromJson(json, targetClass);
    }

    public static <T> String convertObjectToJsonString(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}