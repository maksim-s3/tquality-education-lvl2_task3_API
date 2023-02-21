package logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final String LOGGER_NAME = "Log";
    private static org.apache.logging.log4j.Logger INSTANCE = null;

    private Log() {
    }

    private static Logger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = LogManager.getLogger(LOGGER_NAME);
        }
        return INSTANCE;
    }

    public static void info(String message){
        getInstance().info(message);
    }

    public static void debug(String message){
        getInstance().debug(message);
    }
}