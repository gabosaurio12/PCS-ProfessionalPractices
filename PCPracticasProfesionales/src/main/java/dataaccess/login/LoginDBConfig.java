package dataaccess.login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoginDBConfig {
    private static final String PROPERTIES_FILE = "properties/login_db_config.properties";
    private static final Logger logger = LogManager.getLogger(LoginDBConfig.class);

    public static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = LoginDBConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                props.load(input);
            } else {
                logger.error("No se encontró el archivo de properties: " + PROPERTIES_FILE);
            }
        } catch (IOException e) {
            logger.error("Error al cargar login_db_config.properties: ", e);
        }

        return props;
    }
}
