package configuration;

import org.json.simple.JSONObject;
import utils.JsonLoader;

public class Configuration {
    private static final String NAME_CONFIG_FILE = "config";
    private static JSONObject config = JsonLoader.load(NAME_CONFIG_FILE);

    public static String getStartUrl() {
        return (String) config.get("start_url");
    }
}
