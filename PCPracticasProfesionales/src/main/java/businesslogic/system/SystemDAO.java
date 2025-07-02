package businesslogic.system;

import model.*;

import java.sql.SQLException;
import java.util.List;

public interface SystemDAO {

    int registerSection(Section section) throws SQLException;

    List<Section> getSections() throws SQLException;

    Section getCurrentSection() throws SQLException;

    Section getSectionByID(int sectionID) throws SQLException;

    int updateSectionByID(Section section) throws SQLException;

    int dropSectionByID(int sectionID) throws SQLException;

    UniversityAffiliate getAffiliateByCredentials(String username, String password, String table) throws SQLException;

}
