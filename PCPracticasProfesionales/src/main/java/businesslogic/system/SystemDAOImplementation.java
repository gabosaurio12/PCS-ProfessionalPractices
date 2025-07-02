package businesslogic.system;

import dataaccess.coordinator.CoordinatorDBConnection;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemDAOImplementation implements SystemDAO {


    @Override
    public int registerSection(Section section) throws SQLException {
        String query = "INSERT INTO Section (nrc, period) VALUES (?, ?)";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, section.getNrc());
        statement.setString(2, section.getPeriod());
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        int sectionID = 0;
        if (result.next()) {
            sectionID = result.getInt(1);
        }

        connection.close();
        statement.close();
        result.close();

        return sectionID;
    }

    @Override
    public List<Section> getSections() throws SQLException {
        List<Section> sections = null;
        String selectQuery = "SELECT sectionID, nrc, period " +
                " FROM Section";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            if (sections == null) {
                sections = new ArrayList<>();
            }
            Section section = new Section();
            section.setSectionID(result.getInt("sectionID"));
            section.setNrc(result.getString("nrc"));
            section.setPeriod(result.getString("period"));
            sections.add(section);
        }

        connection.close();
        statement.close();
        result.close();

        return sections;
    }

    @Override
    public Section getCurrentSection() throws SQLException {
        String query = "SELECT s.sectionID, s.nrc, s.period FROM Section s " +
                "JOIN SystemConfiguration sc ON s.sectionID = sc.currentSectionID;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        Section section = null;
        if (result.next()) {
            section = new Section();
            section.setSectionID(result.getInt("sectionID"));
            section.setNrc(result.getString("nrc"));
            section.setPeriod(result.getString("period"));
        }

        connection.close();
        statement.close();
        result.close();

        return section;
    }

    @Override
    public Section getSectionByID(int sectionID) throws SQLException {
        String query = "SELECT sectionID, nrc, period FROM Section WHERE sectionID = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, sectionID);
        ResultSet result = statement.executeQuery();
        Section section = new Section();
        if (result.next()) {
            section.setSectionID(result.getInt("sectionID"));
            section.setNrc(result.getString("nrc"));
            section.setPeriod(result.getString("period"));
        }

        connection.close();
        statement.close();
        result.close();

        return section;
    }

    @Override
    public int updateSectionByID(Section section) throws SQLException {
        String query = "UPDATE Section SET nrc = ?, period = ? WHERE sectionID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, section.getNrc());
        statement.setString(2, section.getPeriod());
        statement.setInt(3, section.getSectionID());
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public int dropSectionByID(int sectionID) throws SQLException {
        String query = "DELETE FROM Section WHERE sectionID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, sectionID);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public UniversityAffiliate getAffiliateByCredentials(String username, String password, String table) throws SQLException {
        String query = table.equals("Academic") ?
                "SELECT academicID AS id, name, email, username, password FROM Academic WHERE " +
                        "username = ? AND password = ?" :
                "SELECT studentID AS id, name, email, username, password FROM Student WHERE " +
                        "username = ? AND password = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        UniversityAffiliate affiliate = new UniversityAffiliate();
        if (result.next()) {
            affiliate.setId(result.getInt("id"));
            affiliate.setName(result.getString("name"));
            affiliate.setEmail(result.getString("email"));
            affiliate.setUserName(result.getString("username"));
            affiliate.setPassword(result.getString("password"));
        }

        connection.close();
        statement.close();
        result.close();

        return affiliate;
    }
}
