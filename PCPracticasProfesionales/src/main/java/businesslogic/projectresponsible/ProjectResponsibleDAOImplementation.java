package businesslogic.projectresponsible;

import dataaccess.coordinator.CoordinatorDBConnection;
import model.ProjectResponsible;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectResponsibleDAOImplementation implements ProjectResponsibleDAO {
    @Override
    public List<ProjectResponsible> getProjectResponsibles(String period) throws SQLException {
        String query = "SELECT * FROM ProjectResponsible WHERE period = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, period);
        ResultSet result = statement.executeQuery();
        List<ProjectResponsible> projectResponsibles = new ArrayList<>();
        while (result.next()) {
            ProjectResponsible projectResponsible = new ProjectResponsible();
            projectResponsible.setProjectResponsibleID(result.getInt("projectResponsibleID"));
            projectResponsible.setName(result.getString("name"));
            projectResponsible.setEmail(result.getString("email"));
            projectResponsible.setAlterContact(result.getString("alterContact"));
            projectResponsible.setLinkedOrganizationId(result.getInt("linkedOrganizationId"));

            projectResponsibles.add(projectResponsible);
        }

        connection.close();
        statement.close();
        result.close();

        return projectResponsibles;
    }

    @Override
    public int registerProjectResponsible(ProjectResponsible projectResponsible) throws SQLException {
        String query = "INSERT INTO ProjectResponsible (" +
                "name, email, alterContact, linkedOrganizationId, period) VALUES " +
                "(?, ?, ?, ?, ?)";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, projectResponsible.getName());
        statement.setString(2, projectResponsible.getEmail());
        statement.setString(3, projectResponsible.getAlterContact());
        statement.setInt(4, projectResponsible.getLinkedOrganizationId());
        statement.setString(5, projectResponsible.getPeriod());
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        int responsibleID = 0;
        if (result.next()) {
            responsibleID = result.getInt(1);
        }

        connection.close();
        statement.close();
        result.close();

        return responsibleID;
    }

    @Override
    public int updateProjectResponsible(ProjectResponsible projectResponsible) throws SQLException {
        String query = "UPDATE ProjectResponsible SET name = ?, email = ?, alterContact = ?, " +
                "linkedOrganizationId = ? WHERE projectResponsibleID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, projectResponsible.getName());
        statement.setString(2, projectResponsible.getEmail());
        statement.setString(3, projectResponsible.getAlterContact());
        statement.setInt(4, projectResponsible.getLinkedOrganizationId());
        statement.setInt(5, projectResponsible.getProjectResponsibleID());
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public ProjectResponsible getProjectResponsibleByID(int id) throws SQLException {
        String query = "SELECT * FROM ProjectResponsible WHERE projectResponsibleID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        ProjectResponsible projectResponsible = new ProjectResponsible();
        if (result.next()) {
            projectResponsible.setProjectResponsibleID(result.getInt("projectResponsibleID"));
            projectResponsible.setName(result.getString("name"));
            projectResponsible.setEmail(result.getString("email"));
            projectResponsible.setAlterContact(result.getString("alterContact"));
            projectResponsible.setLinkedOrganizationId(result.getInt("linkedOrganizationId"));
            projectResponsible.setPeriod(result.getString("period"));
        }

        connection.close();
        statement.close();
        result.close();

        return projectResponsible;
    }

    @Override
    public int dropProjectResponsibleByID(int id) throws SQLException {
        String query = "DELETE FROM ProjectResponsible WHERE projectResponsibleID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }
}
