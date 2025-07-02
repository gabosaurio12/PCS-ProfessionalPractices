package businesslogic.projectresponsible;

import model.ProjectResponsible;

import java.sql.SQLException;
import java.util.List;

public interface ProjectResponsibleDAO {

    List<ProjectResponsible> getProjectResponsibles(String period) throws SQLException;

    int registerProjectResponsible(ProjectResponsible projectResponsible) throws SQLException;

    int updateProjectResponsible(ProjectResponsible projectResponsible) throws SQLException;

    ProjectResponsible getProjectResponsibleByID(int id) throws SQLException;

    int dropProjectResponsibleByID(int id) throws SQLException;

}
