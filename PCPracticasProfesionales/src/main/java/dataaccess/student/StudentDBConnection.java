package dataaccess.student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class StudentDBConnection {
    private static StudentDBConnection instance;
    private final Connection connection;

    private StudentDBConnection() throws SQLException {
        Properties props = StudentDBConfig.loadProperties();
        String URL = props.getProperty("db.url");
        String USER = props.getProperty("db.user");
        String PASSWORD = props.getProperty("db.password");
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static StudentDBConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new StudentDBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
