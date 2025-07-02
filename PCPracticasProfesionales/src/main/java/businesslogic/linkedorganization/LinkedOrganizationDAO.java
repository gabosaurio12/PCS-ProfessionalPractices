package businesslogic.linkedorganization;

import model.LinkedOrganization;

import java.sql.SQLException;
import java.util.List;

public interface LinkedOrganizationDAO {

    int registerLinkedOrganization(LinkedOrganization organization) throws SQLException;

    List<LinkedOrganization> getLinkedOrganizations() throws SQLException;

    LinkedOrganization getLinkedOrganizationById(int id) throws SQLException;

    String getLinkedOrganizationNameByID(int linkedOrganizationID) throws SQLException;

    int assignLinkedOrganizationSection(int linkedOrganizationID, int sectionID) throws SQLException;

    int updateLinkedOrganizationByID(LinkedOrganization organization) throws SQLException;

    int dropLinkedOrganization(int organizationID) throws SQLException;

}
