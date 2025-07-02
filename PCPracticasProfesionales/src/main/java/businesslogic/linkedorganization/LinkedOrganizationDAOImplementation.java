package businesslogic.linkedorganization;

import dataaccess.coordinator.CoordinatorDBConnection;
import model.LinkedOrganization;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LinkedOrganizationDAOImplementation implements LinkedOrganizationDAO {
    @Override
    public int registerLinkedOrganization(LinkedOrganization organization) throws SQLException {
        String query = "INSERT INTO LinkedOrganization (name, description, address, email, alterContact) " +
                "VALUES (?, ? ,?, ?, ?);";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, organization.getName());
        statement.setString(2, organization.getDescription());
        statement.setString(3, organization.getAddress());
        statement.setString(4, organization.getEmail());
        statement.setString(5, organization.getAlterContact());
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        int organizationID = 0;
        if (result.next()) {
            organizationID = result.getInt(1);
        }

        connection.close();
        statement.close();
        result.close();

        return organizationID;
    }

    @Override
    public List<LinkedOrganization> getLinkedOrganizations() throws SQLException {
        String query = "SELECT linkedOrganizationID, name, description, " +
                "address, email, alterContact FROM LinkedOrganization;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();

        List<LinkedOrganization> organizations = new ArrayList<>();
        while (result.next()) {
            LinkedOrganization organization = new LinkedOrganization();
            organization.setLinkedOrganizationID(result.getInt("linkedOrganizationID"));
            organization.setName(result.getString("name"));
            organization.setDescription(result.getString("description"));
            organization.setAddress(result.getString("address"));
            organization.setEmail(result.getString("email"));
            organization.setAlterContact(result.getString("alterContact"));

            organizations.add(organization);
        }

        connection.close();
        statement.close();
        result.close();

        return organizations;
    }

    @Override
    public LinkedOrganization getLinkedOrganizationById(int id) throws SQLException {
        String searchQuery = "SELECT linkedOrganizationID, description, " +
                "name, address, email, alterContact " +
                "FROM LinkedOrganization WHERE linkedOrganizationID = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(searchQuery);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        LinkedOrganization organization = new LinkedOrganization();
        if (result.next()) {
            organization.setLinkedOrganizationID(result.getInt("linkedOrganizationID"));
            organization.setName(result.getString("name"));
            organization.setDescription(result.getString("description"));
            organization.setAddress(result.getString("address"));
            organization.setEmail(result.getString("email"));
            organization.setAlterContact(result.getString("alterContact"));
        }

        connection.close();
        statement.close();
        result.close();

        return organization;
    }

    @Override
    public String getLinkedOrganizationNameByID(int linkedOrganizationID) throws SQLException {
        String query = "SELECT name " +
                "FROM LinkedOrganization WHERE linkedOrganizationID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, linkedOrganizationID);
        ResultSet result = statement.executeQuery();
        String linkedOrganizationName = "";
        if (result.next()) {
            linkedOrganizationName = result.getString(1);
        }

        connection.close();
        statement.close();
        result.close();

        return linkedOrganizationName;
    }

    @Override
    public int assignLinkedOrganizationSection(int linkedOrganizationID, int sectionID) throws SQLException {
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        String query = "INSERT INTO LinkedOrganizationSection (linkedOrganizationID, sectionID) " +
                "VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, linkedOrganizationID);
        statement.setInt(2, sectionID);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public int updateLinkedOrganizationByID(LinkedOrganization organization) throws SQLException {
        String query = "UPDATE LinkedOrganization SET name = ?, description = ?, address = ?, " +
                "email = ?, alterContact = ? WHERE linkedOrganizationID = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, organization.getName());
        statement.setString(2, organization.getDescription());
        statement.setString(3, organization.getAddress());
        statement.setString(4, organization.getEmail());
        statement.setString(5, organization.getAlterContact());
        statement.setInt(6, organization.getLinkedOrganizationID());
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public int dropLinkedOrganization(int organizationID) throws SQLException {
        String query = "DELETE FROM LinkedOrganization WHERE linkedOrganizationID = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, organizationID);
        int affectedRows = statement.executeUpdate();

        statement.close();
        connection.close();

        return affectedRows;
    }
}
