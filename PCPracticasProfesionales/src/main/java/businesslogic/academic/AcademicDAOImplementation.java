package businesslogic.academic;

import dataaccess.coordinator.CoordinatorDBConnection;
import model.Academic;
import model.Evaluation;
import model.UniversityAffiliate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcademicDAOImplementation implements AcademicDAO {

    @Override
    public int registerAcademic(Academic academic) throws SQLException {
        String insertQuery = "INSERT INTO Academic (" +
                "personalNumber, name, firstSurname, secondSurname, " +
                "email, userName, password, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, SHA2(?, 256), ?);";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, academic.getPersonalNumber());
        statement.setString(2, academic.getName());
        statement.setString(3, academic.getFirstSurname());
        statement.setString(4, academic.getSecondSurname());
        statement.setString(5, academic.getEmail());
        statement.setString(6, academic.getUserName());
        statement.setString(7, academic.getPassword());
        statement.setString(8, academic.getRole());
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        int generatedID = 0;
        if (result.next()) {
            generatedID = result.getInt(1);
        }

        connection.close();
        statement.close();
        result.close();

        return generatedID;
    }

    @Override
    public Academic getAcademicByPersonalNumber(String personalNumber) throws SQLException {
        String readAcademic = "SELECT " +
                "academicID, personalNumber, name, firstSurname, " +
                "secondSurname, email, userName, password, role " +
                "FROM Academic WHERE personalNumber = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(readAcademic);
        statement.setString(1, personalNumber);
        ResultSet result = statement.executeQuery();
        Academic academic = new Academic();
        if (result.next()) {
            academic.setId(result.getInt(1));
            academic.setPersonalNumber(result.getString(2));
            academic.setName(result.getString(3));
            academic.setFirstSurname(result.getString(4));
            academic.setSecondSurname(result.getString(5));
            academic.setEmail(result.getString(6));
            academic.setUserName(result.getString(7));
            academic.setPassword(result.getString(8));
            academic.setRole(result.getString(9));
        }

        connection.close();
        statement.close();
        result.close();

        return academic;
    }

    @Override
    public String getAcademicNameByID(int academicID) throws SQLException {
        String query = "SELECT name, firstSurname " +
                "FROM Academic WHERE academicID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, academicID);
        ResultSet result = statement.executeQuery();
        String name = "";
        if (result.next()) {
            name = name.concat(result.getString(1));
            name = name.concat(" ");
            name = name.concat(result.getString(2));
        }

        connection.close();
        statement.close();
        result.close();

        return name;
    }

    @Override
    public Academic getAcademicByCredentials(String username, String password) throws SQLException {
        String readAcademic = "SELECT " +
                "academicID, personalNumber, name, firstSurname, " +
                "secondSurname, email, userName, password, role " +
                "FROM Academic WHERE userName = ? AND password = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(readAcademic);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        Academic academic = new Academic();
        if (result.next()) {
            academic.setId(result.getInt(1));
            academic.setPersonalNumber(result.getString(2));
            academic.setName(result.getString(3));
            academic.setFirstSurname(result.getString(4));
            academic.setSecondSurname(result.getString(5));
            academic.setEmail(result.getString(6));
            academic.setUserName(result.getString(7));
            academic.setPassword(result.getString(8));
            academic.setRole(result.getString(9));
        }

        connection.close();
        statement.close();
        result.close();

        return academic;
    }

    @Override
    public int updateAcademicCredentialsById(UniversityAffiliate affiliate) throws SQLException {
        String query = "UPDATE Academic SET email = ?, username = ?, password = ? WHERE academicID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, affiliate.getEmail());
        statement.setString(2, affiliate.getUserName());
        statement.setString(3, affiliate.getPassword());
        statement.setInt(4, affiliate.getId());
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public int updateAcademicByID(Academic academic) throws SQLException {
        String updateQuery = "UPDATE Academic " +
                "SET name = ?, firstSurname = ?, secondSurname = ?, " +
                "email = ?, username = ?, password = ?, role = ?, personalNumber = ?" +
                "WHERE academicID = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setString(1, academic.getName());
        statement.setString(2, academic.getFirstSurname());
        statement.setString(3, academic.getSecondSurname());
        statement.setString(4, academic.getEmail());
        statement.setString(5, academic.getUserName());
        statement.setString(6, academic.getPassword());
        statement.setString(7, academic.getRole());
        statement.setString(8, academic.getPersonalNumber());
        statement.setInt(9, academic.getId());
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public Academic getAcademicByID(int id) throws SQLException {
        String searchQuery = "SELECT academicID, personalNumber, name, " +
                "firstSurname, secondSurname, email, userName," +
                "password, role FROM Academic WHERE academicID = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(searchQuery);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        Academic academic = new Academic();
        if (result.next()) {
            academic.setId(result.getInt(1));
            academic.setPersonalNumber(result.getString(2));
            academic.setName(result.getString(3));
            academic.setFirstSurname(result.getString(4));
            academic.setSecondSurname(result.getString(5));
            academic.setEmail(result.getString(6));
            academic.setUserName(result.getString(7));
            academic.setPassword(result.getString(8));
            academic.setRole(result.getString(9));
        }

        connection.close();
        statement.close();
        result.close();

        return academic;
    }

    @Override
    public int dropAcademic(int id) throws SQLException {
        String query = "DELETE FROM Academic WHERE academicID = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public List<Academic> getAcademics(String period) throws SQLException {
        String selectAcademicQuery = "SELECT a.academicID, a.personalNumber, " +
                "a.name, a.firstSurname, a.secondSurname, a.email, " +
                "a.userName, a.password, a.role " +
                "FROM Academic a " +
                "JOIN AcademicSection asec ON a.academicID = asec.academicID " +
                "JOIN Section sec ON asec.sectionID = sec.sectionID " +
                "WHERE sec.period = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(selectAcademicQuery);
        statement.setString(1, period);
        ResultSet result = statement.executeQuery();
        List<Academic> academics = new ArrayList<>();
        while (result.next()) {
            Academic academic = new Academic();
            academic.setId(result.getInt("academicID"));
            academic.setPersonalNumber(result.getString("personalNumber"));
            academic.setName(result.getString("name"));
            academic.setFirstSurname(result.getString("firstSurname"));
            academic.setSecondSurname(result.getString("secondSurname"));
            academic.setEmail(result.getString("email"));
            academic.setUserName(result.getString("userName"));
            academic.setPassword(result.getString("password"));
            academic.setRole(result.getString("role"));

            academics.add(academic);
        }

        connection.close();
        statement.close();
        result.close();

        return academics;
    }

    @Override
    public List<Academic> getAcademicsByRole(String role, String period) throws SQLException {
        String query = "SELECT a.academicID, a.personalNumber, " +
                "a.name, a.firstSurname, a.secondSurname, a.email, " +
                "a.userName, a.password, a.role " +
                "FROM Academic a " +
                "JOIN AcademicSection asec ON a.academicID = asec.academicID " +
                "JOIN Section sec ON asec.sectionID = sec.sectionID " +
                "WHERE sec.period = ? AND a.role = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, period);
        statement.setString(2, role);
        ResultSet result = statement.executeQuery();
        List<Academic> academics = new ArrayList<>();
        while (result.next()) {
            Academic academic = new Academic();
            academic.setId(result.getInt("academicID"));
            academic.setPersonalNumber(result.getString("personalNumber"));
            academic.setName(result.getString("name"));
            academic.setFirstSurname(result.getString("firstSurname"));
            academic.setSecondSurname(result.getString("secondSurname"));
            academic.setEmail(result.getString("email"));
            academic.setUserName(result.getString("userName"));
            academic.setPassword(result.getString("password"));
            academic.setRole(result.getString("role"));

            academics.add(academic);
        }

        connection.close();
        statement.close();
        result.close();

        return academics;
    }

    @Override
    public int assignSection(int academicId, int sectionId) throws SQLException {
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        String query = "INSERT INTO AcademicSection (academicID, sectionID) " +
                "VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, academicId);
        statement.setInt(2, sectionId);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public int addEvaluation(Evaluation evaluation) throws SQLException {
        String query = "INSERT INTO Evaluation (evaluationPath, academicId, averageGrade, presentationId) " +
                "VALUES (?, ?, ?, ?)";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, evaluation.getEvaluationPath());
        statement.setInt(2, evaluation.getAcademicId());
        statement.setDouble(3, evaluation.getAverageGrade());
        statement.setInt(4, evaluation.getPresentationId());
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        int evaluationId = 0;
        if (result.next()) {
            evaluationId = result.getInt(1);
        }

        connection.close();
        statement.close();
        result.close();

        return evaluationId;
    }

    @Override
    public int dropEvaluation(int evaluationID) throws SQLException {
        String query = "DELETE FROM Evaluation WHERE evaluationID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, evaluationID);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public List<Evaluation> getEvaluationsFromPresentation(int presentationId) throws SQLException {
        String query = "SELECT * FROM Evaluation WHERE presentationId = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, presentationId);
        ResultSet result = statement.executeQuery();
        List<Evaluation> evaluations = new ArrayList<>();
        while (result.next()) {
            Evaluation evaluation = new Evaluation();
            evaluation.setEvaluationID(result.getInt("evaluationID"));
            evaluation.setEvaluationPath(result.getString("evaluationPath"));
            evaluation.setAcademicId(result.getInt("academicId"));
            evaluation.setAverageGrade(result.getDouble("averageGrade"));
            evaluation.setPresentationId(result.getInt("presentationId"));

            evaluations.add(evaluation);
        }

        connection.close();
        statement.close();
        result.close();

        return evaluations;
    }

    @Override
    public List<String> getRoles() throws SQLException {
        String query = "SELECT role FROM RolesCatalog;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        List<String> roles = new ArrayList<>();
        while (result.next()) {
            roles.add(result.getString("role"));
        }

        return roles;
    }
}
