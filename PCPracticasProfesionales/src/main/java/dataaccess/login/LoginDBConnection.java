package dataaccess.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class LoginDBConnection {
    private static LoginDBConnection instance;
    private final Connection connection;

    private LoginDBConnection() throws SQLException {
        Properties props = LoginDBConfig.loadProperties();
        String URL = props.getProperty("db.url");
        String USER = props.getProperty("db.user");
        String PASSWORD = props.getProperty("db.password");
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static LoginDBConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new LoginDBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
