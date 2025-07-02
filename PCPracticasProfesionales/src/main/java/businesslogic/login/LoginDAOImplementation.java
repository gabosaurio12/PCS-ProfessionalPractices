package businesslogic.login;

import dataaccess.login.LoginDBConnection;

import java.sql.*;

public class LoginDAOImplementation implements LoginDAO {
    @Override
    public boolean getUser(String userName) throws SQLException {
        String query = "SELECT isUserFromLogin(?)";
        Connection connection = LoginDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, userName);
        ResultSet result = statement.executeQuery();
        boolean isUser = true;
        if (result.next()) {
            if (result.getInt(1) == 0) {
                isUser = false;
            }
        }
        connection.close();
        statement.close();
        result.close();

        return isUser;
    }

    public String getHashedPassword(String password) throws SQLException {
        String query = "SELECT SHA2(?, 256)";
        Connection connection = LoginDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, password);
        ResultSet result = statement.executeQuery();
        String hashedPassword = null;
        if (result.next()) {
            hashedPassword = result.getString(1);
        }

        connection.close();
        statement.close();

        return hashedPassword;
    }

    @Override
    public String getUserRole(String userName, String userPassword) throws SQLException {
        String query = "SELECT findUserRole(?, ?) AS role";
        Connection connection = LoginDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, userName);
        statement.setString(2, userPassword);
        ResultSet result = statement.executeQuery();
        String role = "";
        if (result.next()) {
            role = result.getString(1);
        }

        statement.close();
        connection.close();

        return role;
    }
}
