package skillapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jun
 * @date 2020/8/31.
 */
public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
