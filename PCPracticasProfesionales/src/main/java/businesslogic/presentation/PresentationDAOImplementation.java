package businesslogic.presentation;

import dataaccess.coordinator.CoordinatorDBConnection;
import model.Academic;
import model.Presentation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PresentationDAOImplementation implements PresentationDAO {

    @Override
    public int createPresentation(Presentation presentation) throws SQLException {
        String query = "INSERT INTO Presentation (presentationPath, date, studentId) " +
                "VALUES (?, ?, ?)";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, presentation.getPresentationPath());
        statement.setDate(2, presentation.getDate());
        statement.setInt(3, presentation.getStudentId());
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        int presentationId = 0;
        if (result.next()) {
            presentationId = result.getInt(1);
        }

        connection.close();
        statement.close();
        result.close();

        return presentationId;
    }

    @Override
    public int updatePresentation(Presentation presentation) throws SQLException {
        String query = "UPDATE Presentation SET presentationPath = ?, date = ?, studentId = ?," +
                "presentationGrade = ? WHERE presentationId = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, presentation.getPresentationPath());
        statement.setDate(2, presentation.getDate());
        statement.setInt(3, presentation.getStudentId());
        statement.setDouble(4, presentation.getPresentationGrade());
        statement.setInt(5, presentation.getPresentationID());
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public Presentation getPresentationById(int presentationId) throws SQLException {
        String query = "SELECT * FROM Presentation WHERE presentationId = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, presentationId);
        ResultSet result = statement.executeQuery();
        Presentation presentation = new Presentation();
        if (result.next()) {
            presentation.setPresentationID(result.getInt("presentationID"));
            presentation.setPresentationPath(result.getString("presentationPath"));
            presentation.setDate(result.getDate("date"));
            presentation.setPresentationGrade(result.getDouble("presentationGrade"));
            presentation.setStudentId(result.getInt("studentId"));
        }

        connection.close();
        statement.close();
        result.close();

        return presentation;
    }

    @Override
    public int dropPresentation(int presentationId) throws SQLException {
        String query = "DELETE FROM Presentation WHERE presentationID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, presentationId);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public List<Presentation> getStudentPresentations(int id) throws SQLException {
        String query = "SELECT * FROM Presentation WHERE studentId = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        List<Presentation> presentations = new ArrayList<>();
        while (result.next()) {
            Presentation presentation = new Presentation();
            presentation.setPresentationID(result.getInt("presentationID"));
            presentation.setPresentationGrade(result.getDouble("presentationGrade"));
            presentation.setPresentationPath(result.getString("presentationPath"));
            presentation.setDate(result.getDate("date"));
            presentation.setStudentId(result.getInt("studentId"));
            presentations.add(presentation);
        }

        connection.close();
        statement.close();
        result.close();

        return presentations;
    }

    @Override
    public List<Presentation> getPresentations() throws SQLException {
        String query = "SELECT * FROM Presentation";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        List<Presentation> presentations = new ArrayList<>();
        while (result.next()) {
            Presentation presentation = new Presentation();
            presentation.setPresentationID(result.getInt("presentationID"));
            presentation.setPresentationGrade(result.getDouble("presentationGrade"));
            presentation.setPresentationPath(result.getString("presentationPath"));
            presentation.setDate(result.getDate("date"));
            presentation.setStudentId(result.getInt("studentId"));
            presentations.add(presentation);
        }

        connection.close();
        statement.close();
        result.close();

        return presentations;
    }

    @Override
    public List<Academic> getPresentationEvaluators(int presentationId) throws SQLException {
        String query = "SELECT ";
        return List.of();
    }
}
