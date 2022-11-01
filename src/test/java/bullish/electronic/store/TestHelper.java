package bullish.electronic.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class TestHelper {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static String objToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T jsonToObj(String str, Class<T> cls) {
        try {
            return mapper.readValue(str, cls);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
