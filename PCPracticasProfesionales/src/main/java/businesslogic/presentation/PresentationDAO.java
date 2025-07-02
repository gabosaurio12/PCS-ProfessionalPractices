package businesslogic.presentation;

import model.Academic;
import model.Presentation;

import java.sql.SQLException;
import java.util.List;

public interface PresentationDAO {

    int createPresentation(Presentation presentation) throws SQLException;

    int updatePresentation(Presentation presentation) throws SQLException;

    Presentation getPresentationById(int presentationId) throws SQLException;

    int dropPresentation(int presentationId) throws SQLException;

    List<Presentation> getStudentPresentations(int id) throws SQLException;

    List<Presentation> getPresentations() throws SQLException;

    List<Academic> getPresentationEvaluators(int presentationId) throws SQLException;

}
