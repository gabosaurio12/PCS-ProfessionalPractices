package businesslogic.student;

import model.Report;
import model.Student;
import model.StudentProgress;
import model.UniversityAffiliate;

import java.sql.SQLException;
import java.util.List;

public interface StudentDAO {
    int registerStudent(Student student) throws SQLException;

    Student getStudentByTuition(String tuition) throws SQLException;

    Double getAutoevaluationGradeByID(int id) throws SQLException;

    int updateStudentByTuition(Student student, String oldTuition) throws SQLException;

    int updateAutoevaluationGradeById(Double grade, int id) throws SQLException;

    List<Student> getStudents(String period) throws SQLException;

    int dropStudent(int id) throws SQLException;

    int assignSection(int studentId, int sectionId) throws SQLException;

    StudentProgress getStudentProgressByTuition(String tuition) throws SQLException;



    int registerReport(Report report) throws SQLException;

    List<Report> getReports() throws SQLException;

    List<Report> getReportsByStudentID(int id) throws SQLException;

    int deleteReport(int id) throws SQLException;

    List<String> getReportsType() throws SQLException;

    int updateStudentCredentialsById(UniversityAffiliate affiliate) throws SQLException;
}
