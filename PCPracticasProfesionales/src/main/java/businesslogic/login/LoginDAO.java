package businesslogic.login;

import java.sql.SQLException;

public interface LoginDAO {

    boolean getUser(String userName) throws SQLException;

    String getUserRole(String userName, String userPassword) throws SQLException;

    String getHashedPassword(String password) throws SQLException;
}