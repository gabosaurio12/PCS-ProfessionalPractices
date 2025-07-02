package businesslogic.academic;

import model.Academic;
import model.Evaluation;
import model.UniversityAffiliate;

import java.sql.SQLException;
import java.util.List;

public interface AcademicDAO {

    int registerAcademic(Academic academic) throws SQLException;

    Academic getAcademicByPersonalNumber(String tuition) throws SQLException;

    String getAcademicNameByID(int academicID) throws SQLException;

    Academic getAcademicByCredentials(String username, String password) throws SQLException;

    int updateAcademicCredentialsById(UniversityAffiliate affiliate) throws SQLException;

    int updateAcademicByID(Academic academic) throws SQLException;

    Academic getAcademicByID(int id) throws SQLException;

    int dropAcademic(int id) throws SQLException;

    List<Academic> getAcademics(String period) throws SQLException;

    List<Academic> getAcademicsByRole(String role, String period) throws SQLException;

    int assignSection(int academicId, int sectionId) throws SQLException;



    int addEvaluation(Evaluation evaluation) throws SQLException;

    int dropEvaluation(int evaluationID) throws SQLException;

    List<Evaluation> getEvaluationsFromPresentation(int presentationId) throws SQLException;



    List<String> getRoles() throws SQLException;

}
