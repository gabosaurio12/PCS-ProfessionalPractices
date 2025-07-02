package dataaccess.coordinator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CoordinatorDBConfig {
    private static final String PROPERTIES_FILE = "properties/coordinator_db_config.properties";
    private static final Logger logger = LogManager.getLogger(CoordinatorDBConfig.class);

    public static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = CoordinatorDBConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                logger.error("No se encontró el archivo de properties: " + PROPERTIES_FILE);
            }
        } catch (IOException e) {
            logger.error("Error al cargar coordinator_db_config.properties: ", e);
        }

        return properties;
    }
}
