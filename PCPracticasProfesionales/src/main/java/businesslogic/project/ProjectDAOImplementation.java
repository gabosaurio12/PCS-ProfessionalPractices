package businesslogic.project;

import dataaccess.coordinator.CoordinatorDBConnection;
import dataaccess.student.StudentDBConnection;
import model.Project;
import model.ProjectAssignation;
import model.ProjectRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImplementation implements ProjectDAO {
    @Override
    public int registerProjectRequest(ProjectRequest request) throws SQLException {
        String query = "INSERT INTO ProjectRequest (name, documentPath, studentId) " +
                "VALUES (?, ?, ?)";
        Connection connection = StudentDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, request.getName());
        statement.setString(2, request.getDocumentPath());
        statement.setInt(3, request.getStudentId());
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        int requestID = 0;
        if (result.next()) {
            requestID = result.getInt(1);
        }

        statement.close();
        result.close();
        connection.close();

        return requestID;
    }

    @Override
    public int dropProjectRequestByID(int requestID) throws SQLException {
        String query = "DELETE FROM ProjectRequest WHERE id = ?";
        Connection connection = StudentDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, requestID);
        int affectedRows = statement.executeUpdate();

        statement.close();
        connection.close();

        return affectedRows;
    }

    @Override
    public List<ProjectRequest> getRequests() throws SQLException {
        String query = "SELECT * FROM ProjectRequest";
        Connection connection = StudentDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        List<ProjectRequest> requests = new ArrayList<>();
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            ProjectRequest request = new ProjectRequest();
            request.setId(result.getInt("id"));
            request.setName(result.getString("name"));
            request.setDocumentPath(result.getString("documentPath"));
            request.setStudentId(result.getInt("studentId"));
            requests.add(request);
        }

        statement.close();
        result.close();
        connection.close();

        return requests;
    }

    @Override
    public int setProjectsRegistrationStatus(int status) throws SQLException {
        String query = "UPDATE SystemConfiguration SET projectRegistrationStatus = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, status);
        int affectedRows = statement.executeUpdate();

        statement.close();
        connection.close();

        return affectedRows;
    }

    @Override
    public Project getProjectById(int id) throws SQLException {
        String searchQuery = "SELECT projectID, title, category, " +
                "beginningDate, endingDate, status, openSpots, " +
                "linkedOrganizationId, projectResponsibleId, " +
                "documentInfoPath FROM Project WHERE projectID = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(searchQuery);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        Project project = new Project();
        if (result.next()) {
            project.setProjectID(result.getInt("projectID"));
            project.setTitle(result.getString("title"));
            project.setCategory(result.getString("category"));
            project.setBeginningDate(result.getDate("beginningDate"));
            project.setEndingDate(result.getDate("endingDate"));
            project.setStatus(result.getString("status"));
            project.setOpenSpots(result.getInt("openSpots"));
            project.setLinkedOrganizationId(result.getInt("linkedOrganizationId"));
            project.setProjectResponsibleId(result.getInt("projectResponsibleId"));
            project.setDocumentInfoPath(result.getString("documentInfoPath"));
        }

        statement.close();
        result.close();
        connection.close();

        return project;
    }

    @Override
    public int registerProject(Project project) throws SQLException {
        String insertQuery = "INSERT INTO Project (title, category, beginningDate, endingDate, status," +
                "openSpots, linkedOrganizationId, projectResponsibleId, documentInfoPath) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, project.getTitle());
        statement.setString(2, project.getCategory());
        statement.setDate(3, project.getBeginningDate());
        statement.setDate(4, project.getEndingDate());
        statement.setString(5, project.getStatus());
        statement.setInt(6, project.getOpenSpots());
        statement.setInt(7, project.getLinkedOrganizationId());
        statement.setInt(8, project.getProjectResponsibleId());
        statement.setString(9, project.getDocumentInfoPath());
        statement.executeUpdate();
        int projectID = 0;
        ResultSet result = statement.getGeneratedKeys();
        if (result.next()) {
            projectID = result.getInt(1);
        }

        statement.close();
        connection.close();

        return projectID;
    }

    @Override
    public int updateProjectById(Project project) throws SQLException {
        String updateQuery = "UPDATE Project SET " +
                "title = ?, linkedOrganizationId = ?, " +
                "category = ?, beginningDate = ?, endingDate = ?, status = ?, " +
                "openSpots = ?, projectResponsibleId = ?, documentInfoPath = ? " +
                "WHERE projectID = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setString(1, project.getTitle());
        statement.setInt(2, project.getLinkedOrganizationId());
        statement.setString(3, project.getCategory());
        statement.setDate(4, project.getBeginningDate());
        statement.setDate(5, project.getEndingDate());
        statement.setString(6, project.getStatus());
        statement.setInt(7, project.getOpenSpots());
        statement.setInt(8, project.getProjectResponsibleId());
        statement.setString(9, project.getDocumentInfoPath());
        statement.setInt(10, project.getProjectID());
        int affectedRows = statement.executeUpdate();

        statement.close();
        connection.close();

        return affectedRows;
    }

    @Override
    public int dropProject(int projectID) throws SQLException {
        String query = "DELETE FROM Project WHERE projectID = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, projectID);
        int affectedRows = statement.executeUpdate();

        statement.close();
        connection.close();

        return affectedRows;
    }

    @Override
    public int assignProjectSection(int projectID, int sectionID) throws SQLException {
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        String query = "INSERT INTO ProjectSection (projectID, sectionID) " +
                "VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, projectID);
        statement.setInt(2, sectionID);
        int affectedRows = statement.executeUpdate();

        statement.close();
        connection.close();

        return affectedRows;
    }

    @Override
    public int assignProjectById(ProjectAssignation assignation) throws SQLException {
        String insertQuery = "INSERT INTO projectAsignation (" +
                "assignationDocumentPath, coordinatorId, studentId, projectId) " +
                "VALUES (?, ?, ?, ?);";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setString(1, assignation.getDocumentPath());
        statement.setInt(2, assignation.getCoordinatorId());
        statement.setInt(3, assignation.getStudentId());
        statement.setInt(4, assignation.getProjectId());
        int affectedRows = statement.executeUpdate();

        statement.close();
        connection.close();

        return affectedRows;
    }

    @Override
    public int dropAssignationByID(int studentId) throws SQLException {
        String dropQuery = "DELETE FROM projectAsignation WHERE studentId = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(dropQuery);
        statement.setInt(1, studentId);
        int affectedRows = statement.executeUpdate();

        statement.close();
        connection.close();

        return affectedRows;
    }

    @Override
    public List<Project> getProjectsByPeriod(String period) throws SQLException {
        String selectQuery = "SELECT p.projectID, p.title, p.category, p.linkedOrganizationId, p.beginningDate, " +
                "p.endingDate, p.status, p.openSpots, p.linkedOrganizationId, p.projectResponsibleId, " +
                "p.documentInfoPath " +
                "FROM Project p " +
                "JOIN ProjectSection ps ON p.projectID = ps.projectID " +
                "JOIN Section sec ON ps.sectionID = sec.sectionID " +
                "WHERE sec.period = ?;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, period);

        ResultSet result = statement.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (result.next()) {
            Project project = new Project();
            project.setProjectID(result.getInt("projectID"));
            project.setTitle(result.getString("title"));
            project.setCategory(result.getString("category"));
            project.setLinkedOrganizationId(result.getInt("linkedOrganizationId"));
            project.setBeginningDate(result.getDate("beginningDate"));
            project.setEndingDate(result.getDate("endingDate"));
            project.setStatus(result.getString("status"));
            project.setOpenSpots(result.getInt("openSpots"));
            project.setLinkedOrganizationId(result.getInt("linkedOrganizationId"));
            project.setProjectResponsibleId(result.getInt("projectResponsibleId"));
            project.setDocumentInfoPath(result.getString("documentInfoPath"));
            projects.add(project);
        }

        statement.close();
        result.close();
        connection.close();

        return projects;
    }

    @Override
    public List<Project> getProjectsAvailable(String period) throws SQLException {
        String query = "SELECT p.projectID, p.title, p.category, p.beginningDate, " +
                "p.endingDate, p.status, p.openSpots, p.linkedOrganizationId, p.projectResponsibleId, " +
                "p.documentInfoPath " +
                "FROM Project p " +
                "JOIN ProjectSection ps ON p.projectID = ps.projectID " +
                "JOIN Section sec ON ps.sectionID = sec.sectionID " +
                "WHERE sec.period = ? AND p.openSpots > 0";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, period);

        ResultSet result = statement.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (result.next()) {
            Project project = new Project();
            project.setProjectID(result.getInt("projectID"));
            project.setTitle(result.getString("title"));
            project.setCategory(result.getString("category"));
            project.setBeginningDate(result.getDate("beginningDate"));
            project.setEndingDate(result.getDate("endingDate"));
            project.setStatus(result.getString("status"));
            project.setOpenSpots(result.getInt("openSpots"));
            project.setLinkedOrganizationId(result.getInt("linkedOrganizationId"));
            project.setProjectResponsibleId(result.getInt("projectResponsibleId"));
            project.setDocumentInfoPath(result.getString("documentInfoPath"));

            projects.add(project);
        }

        statement.close();
        result.close();
        connection.close();

        return projects;
    }

    @Override
    public int getProjectsRegistrationStatus() throws SQLException {
        String query = "SELECT projectRegistrationStatus FROM SystemConfiguration;";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        int projectRegistrationStatus = 0;
        if (result.next()) {
            projectRegistrationStatus = result.getInt("projectRegistrationStatus");
        }

        statement.close();
        result.close();
        connection.close();

        return projectRegistrationStatus;
    }
}
