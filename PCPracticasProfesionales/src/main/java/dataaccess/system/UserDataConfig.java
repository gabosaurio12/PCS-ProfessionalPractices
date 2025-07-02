package dataaccess.system;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UserDataConfig {
    private static final String PROPERTIES_FILE = "/Users/gabosaurio/Documents/UV/4° Semestre/" +
            "PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/properties/user.properties";
    private static final Logger logger = LogManager.getLogger(UserDataConfig.class);

    public static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            logger.error("Error al cargar user.properties: ", e);
        }

        return properties;
    }
}
