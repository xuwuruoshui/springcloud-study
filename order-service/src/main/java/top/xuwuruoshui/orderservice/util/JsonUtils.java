package top.xuwuruoshui.orderservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json utils
 */
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Json--->JsonNode
     */
    public static JsonNode str2JsonNode(String str){
        try {
            return objectMapper.readTree(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
