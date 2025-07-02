package dataaccess.coordinator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CoordinatorDBConnection {
    private static CoordinatorDBConnection instance;
    private final Connection connection;

    private CoordinatorDBConnection() throws SQLException {
        Properties props = CoordinatorDBConfig.loadProperties();
        String URL = props.getProperty("db.url");
        String USER = props.getProperty("db.user");
        String PASSWORD = props.getProperty("db.password");
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static CoordinatorDBConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new CoordinatorDBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
