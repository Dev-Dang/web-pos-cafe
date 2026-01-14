package com.laptrinhweb.zerostarcafe.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Simple JSON utility using Jackson for parsing, extraction, and validation.
 * Provides reusable JSON operations with readable and easy-to-understand methods.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts JSON string to Map for easy access to key-value pairs.
     *
     * @param jsonString the JSON text
     * @return Map with key-value pairs, empty if parsing fails
     */
    public static Map<String, Object> toMap(String jsonString) {
        if (isEmptyJson(jsonString)) {
            return Collections.emptyMap();
        }

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            Map<String, Object> resultMap = new HashMap<>();

            Iterator<Map.Entry<String, JsonNode>> iterator = rootNode.fields();

            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();

                String key = entry.getKey();
                JsonNode value = entry.getValue();

                Object finalValue;

                if (value.isTextual()) {
                    finalValue = value.asText();
                } else if (value.isNumber()) {
                    finalValue = value.numberValue();
                } else if (value.isBoolean()) {
                    finalValue = value.asBoolean();
                } else {
                    finalValue = value.toString();
                }

                resultMap.put(key, finalValue);
            }

            return resultMap;
        } catch (Exception e) {
            LoggerUtil.warn(JsonUtils.class,
                    "Cannot convert JSON to Map: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    /**
     * Gets text value from JSON by key name.
     *
     * @param jsonString the JSON text
     * @param keyName    the key to find
     * @return text value or null if not found
     */
    public static String getText(String jsonString, String keyName) {
        if (isEmptyJson(jsonString) || keyName == null) {
            return null;
        }

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            JsonNode targetNode = rootNode.get(keyName);

            return (targetNode != null && !targetNode.isNull())
                    ? targetNode.asText() : null;
        } catch (Exception e) {
            LoggerUtil.warn(JsonUtils.class, "Cannot get text for key '" + keyName + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if text contains valid JSON format.
     *
     * @param jsonString the text to check
     * @return true if valid JSON, false otherwise
     */
    public static boolean isValidJson(String jsonString) {
        if (isEmptyJson(jsonString)) {
            return false;
        }

        try {
            mapper.readTree(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Converts object to JSON text.
     *
     * @param object the object to convert
     * @return JSON text
     * @throws AppException if conversion fails
     */
    public static String toJsonText(Object object) {
        if (object == null) {
            return null;
        }

        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new AppException("Cannot convert object to JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Converts JSON text to specific object type.
     *
     * @param jsonString  the JSON text
     * @param targetClass the class to convert to
     * @param <T>         the type
     * @return converted object
     * @throws AppException if conversion fails
     */
    public static <T> T toObject(String jsonString, Class<T> targetClass) {
        if (isEmptyJson(jsonString)) {
            throw new AppException("JSON text cannot be empty");
        }

        try {
            return mapper.readValue(jsonString, targetClass);
        } catch (Exception e) {
            throw new AppException("Cannot convert JSON to " + targetClass.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Checks if JSON contains a specific key.
     *
     * @param jsonString the JSON text
     * @param keyName    the key name to check
     * @return true if key exists, false otherwise
     */
    public static boolean hasKey(String jsonString, String keyName) {
        if (isEmptyJson(jsonString) || keyName == null) {
            return false;
        }

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            return rootNode.has(keyName);
        } catch (Exception e) {
            LoggerUtil.warn(JsonUtils.class, "Cannot check key '" + keyName + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to check if JSON string is empty or null.
     */
    private static boolean isEmptyJson(String jsonString) {
        return jsonString == null || jsonString.trim().isEmpty();
    }
}