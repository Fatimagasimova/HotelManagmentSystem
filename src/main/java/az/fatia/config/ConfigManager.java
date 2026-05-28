package az.fatia.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Config file not found, using default settings: " + e.getMessage());
        }
    }

    public static String getSerializationPath() {
        return properties.getProperty("app.serialization.path", "apartments.ser");
    }

    public static boolean isHotelChangeStatusEnabled() {
        return Boolean.parseBoolean(properties.getProperty("hotel.feature.change-apartment-status.enabled", "true"));
    }

}