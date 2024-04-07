package com.ocado.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonReader {

    private JsonReader() {
    }

    public static Map<String, List<String>> readConfigJson(String absolutePathToConfigFile) {
        Map<String, List<String>> configMap = new HashMap<>();

        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(absolutePathToConfigFile)));
            ObjectMapper objectMapper = new ObjectMapper();
            configMap = objectMapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return configMap;
    }

    public static List<String> readBasketFromJson(String absolutePathToBasketFile) {
        List<String> basketList = new ArrayList<>();

        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(absolutePathToBasketFile)));
            ObjectMapper objectMapper = new ObjectMapper();
            basketList = objectMapper.readValue(jsonString, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return basketList;
    }
}
