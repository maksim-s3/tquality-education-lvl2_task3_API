package utils;

import java.io.File;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;

public class JsonLoader {
    public static final String RESOURCES_PATH = "src/test/resources/%s.json";

    public static JSONObject load(String fileName) {
        JSONObject jsonObject;
        File file = new File(String.format(RESOURCES_PATH, fileName));
        Object o = null;
        try {
            try {
                o = new JSONParser().parse(new FileReader(file));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new FileSystemNotFoundException(String.format("Файл %s.json не найден", fileName));
        }
        return (JSONObject) o;
    }
}
