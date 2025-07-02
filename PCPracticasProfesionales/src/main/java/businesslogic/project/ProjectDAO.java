package businesslogic.project;

import model.Project;
import model.ProjectAssignation;
import model.ProjectRequest;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDAO {

    int registerProjectRequest(ProjectRequest request) throws SQLException;

    int dropProjectRequestByID(int requestID) throws SQLException;

    List<ProjectRequest> getRequests() throws SQLException;


    int registerProject(Project project) throws SQLException;

    int updateProjectById(Project project) throws SQLException;

    int dropProject(int projectID) throws SQLException;

    int dropAssignationByID(int studentId) throws SQLException;

    Project getProjectById(int id) throws SQLException;

    List<Project> getProjectsByPeriod(String period) throws SQLException;

    List<Project> getProjectsAvailable(String period) throws SQLException;



    int assignProjectSection(int projectID, int sectionID) throws SQLException;

    int assignProjectById(ProjectAssignation assignation) throws SQLException;



    int getProjectsRegistrationStatus() throws SQLException;

    int setProjectsRegistrationStatus(int status) throws SQLException;
}
