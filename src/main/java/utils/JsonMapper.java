package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonMapper {
    private static ObjectMapper mapper = new ObjectMapper();
    public static <T> T readValue(File src, Class<T> valueType){
        Object o = null;
        try {
            o = mapper.readValue(src, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (T) o;
    }
}
