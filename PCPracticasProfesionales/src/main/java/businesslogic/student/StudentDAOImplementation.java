package businesslogic.student;

import dataaccess.coordinator.CoordinatorDBConnection;
import dataaccess.student.StudentDBConnection;
import model.Report;
import model.Student;
import model.StudentProgress;
import model.UniversityAffiliate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImplementation implements StudentDAO {
    @Override
    public int registerStudent(Student student) throws SQLException {
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        String insertStudent = "INSERT INTO Student (" +
                "tuition, name, firstSurname, secondSurname, " +
                "email, userName, password, creditAdvance, academicId, " +
                "grade) VALUES (?, ?, ?, ?, ?, ?, SHA2(?, 256), ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(insertStudent, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, student.getTuition());
        statement.setString(2, student.getName());
        statement.setString(3, student.getFirstSurname());
        statement.setString(4, student.getSecondSurname());
        statement.setString(5, student.getEmail());
        statement.setString(6, student.getUserName());
        statement.setString(7, student.getPassword());
        statement.setInt(8, student.getCreditAdvance());
        statement.setInt(9, student.getAcademicId());
        statement.setFloat(10, 10);
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        int generatedID = 0;
        if (result.next()) {
            generatedID = result.getInt(1);
        }

        connection.close();
        statement.close();
        result.close();

        return generatedID;
    }

    @Override
    public Student getStudentByTuition(String tuition) throws SQLException {
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        String readStudent = "SELECT " +
                "studentID, tuition, name, firstSurname, " +
                "secondSurname, email, userName, password, creditAdvance, " +
                "projectId, academicId, grade FROM Student WHERE tuition = ?;";
        PreparedStatement statement = connection.prepareStatement(readStudent);
        statement.setString(1, tuition);
        ResultSet result = statement.executeQuery();
        Student student = new Student();
        if (result.next()) {
            student.setId(result.getInt("studentID"));
            student.setTuition(result.getString("tuition"));
            student.setName(result.getString("name"));
            student.setFirstSurname(result.getString("firstSurname"));
            student.setSecondSurname(result.getString("secondSurname"));
            student.setEmail(result.getString("email"));
            student.setUserName(result.getString("userName"));
            student.setPassword(result.getString("password"));
            student.setCreditAdvance(result.getInt("creditAdvance"));
            student.setProjectId(result.getInt("projectId"));
            student.setAcademicId(result.getInt("academicId"));
            student.setGrade(result.getFloat("grade"));
        }

        connection.close();
        statement.close();
        result.close();

        return student;
    }

    @Override
    public Double getAutoevaluationGradeByID(int id) throws SQLException {
        String query = "SELECT autoevaluationGrade FROM Student WHERE studentID = ?";
        Connection connection = StudentDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        double grade = 1.0;
        if (result.next()) {
            grade = result.getDouble("autoevaluationGrade");
        }

        connection.close();
        statement.close();
        result.close();

        return grade;
    }

    public Student getStudentByID(int id) throws SQLException {
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        String readStudent = "SELECT " +
                "studentID, tuition, name, firstSurname, " +
                "secondSurname, email, userName, password, creditAdvance, " +
                "projectId, academicId, grade FROM Student WHERE studentID = ?;";
        PreparedStatement statement = connection.prepareStatement(readStudent);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        Student student = new Student();
        if (result.next()) {
            student.setId(result.getInt("studentID"));
            student.setTuition(result.getString("tuition"));
            student.setName(result.getString("name"));
            student.setFirstSurname(result.getString("firstSurname"));
            student.setSecondSurname(result.getString("secondSurname"));
            student.setEmail(result.getString("email"));
            student.setUserName(result.getString("userName"));
            student.setPassword(result.getString("password"));
            student.setCreditAdvance(result.getInt("creditAdvance"));
            student.setProjectId(result.getInt("projectId"));
            student.setAcademicId(result.getInt("academicId"));
            student.setGrade(result.getFloat("grade"));
        }

        connection.close();
        statement.close();
        result.close();

        return student;
    }

    @Override
    public int updateStudentByTuition(Student student, String oldTuition) throws SQLException {
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        String updateQuery = "UPDATE Student " +
                "SET name = ?, firstSurname = ?, secondSurname = ?, " +
                "email = ?, userName = ?, password = ?, academicId = ?, creditAdvance = ?," +
                "grade = ?, tuition = ? WHERE tuition = ?;";
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setString(1, student.getName());
        statement.setString(2, student.getFirstSurname());
        statement.setString(3, student.getSecondSurname());
        statement.setString(4, student.getEmail());
        statement.setString(5, student.getUserName());
        statement.setString(6, student.getPassword());
        statement.setInt(7, student.getAcademicId());
        statement.setInt(8, student.getCreditAdvance());
        statement.setFloat(9, student.getGrade());
        statement.setString(10, student.getTuition());
        statement.setString(11, oldTuition);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public int updateAutoevaluationGradeById(Double grade, int id) throws SQLException {
        String query = "UPDATE Student SET autoevaluationGrade = ? WHERE studentID = ?";
        Connection connection = StudentDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDouble(1, grade);
        statement.setInt(2, id);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public List<Student> getStudents(String period) throws SQLException {
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        String selectQuery = "SELECT s.studentID, s.tuition, s.name, " +
                "s.firstSurname, s.secondSurname, s.email, " +
                "s.userName, s.password, s.creditAdvance, " +
                "s.grade, s.projectId, s.academicId " +
                "FROM Student s " +
                "JOIN StudentSection ss ON s.studentID = ss.studentID " +
                "JOIN Section sec ON ss.sectionID = sec.sectionID " +
                "WHERE sec.period = ?;";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, period);
        ResultSet result = statement.executeQuery();
        List<Student> students = new ArrayList<>();
        while (result.next()) {
            Student student = new Student();
            student.setId(result.getInt("studentID"));
            student.setTuition(result.getString("tuition"));
            student.setName(result.getString("name"));
            student.setFirstSurname(result.getString("firstSurname"));
            student.setSecondSurname(result.getString("secondSurname"));
            student.setEmail(result.getString("email"));
            student.setUserName(result.getString("userName"));
            student.setPassword(result.getString("password"));
            student.setCreditAdvance(result.getInt("creditAdvance"));
            student.setGrade(result.getFloat("grade"));
            student.setAcademicId(result.getInt("academicId"));
            student.setProjectId(result.getInt("projectId"));

            students.add(student);
        }

        connection.close();
        statement.close();
        result.close();

        return students;
    }

    @Override
    public List<Student> getAllStudents() throws SQLException {
        String query = "SELECT * FROM Student";
        List<Student> students = new ArrayList<>();
        try (Connection connection = CoordinatorDBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
           ResultSet result = statement.executeQuery();
           while (result.next()) {
               Student student = new Student();
               student.setId(result.getInt("studentID"));
               student.setTuition(result.getString("tuition"));
               student.setName(result.getString("name"));
               student.setFirstSurname(result.getString("firstSurname"));
               student.setSecondSurname(result.getString("secondSurname"));
               student.setEmail(result.getString("email"));
               student.setUserName(result.getString("userName"));
               student.setPassword(result.getString("password"));
               student.setCreditAdvance(result.getInt("creditAdvance"));
               student.setGrade(result.getFloat("grade"));
               student.setAcademicId(result.getInt("academicId"));
               student.setProjectId(result.getInt("projectId"));

               students.add(student);
           }

           result.close();
        }
        return students;
    }

    @Override
    public int dropStudent(int id) throws SQLException {
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        String deleteQuery = "DELETE FROM Student WHERE " +
                "studentID = ?;";
        PreparedStatement statement = connection.prepareStatement(deleteQuery);
        statement.setInt(1, id);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public int assignSection(int studentId, int sectionId) throws SQLException {
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        String query = "INSERT INTO StudentSection (studentID, sectionID) " +
                "VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, studentId);
        statement.setInt(2, sectionId);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public StudentProgress getStudentProgressByTuition(String tuition) throws SQLException {
        String query = "SELECT * FROM StudentProgress WHERE tuition = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, tuition);
        ResultSet result = statement.executeQuery();
        StudentProgress progress = new StudentProgress();
        if (result.next()) {
            progress.setTuition(result.getString("tuition"));
            progress.setHoursValidated(result.getInt("hoursValidated"));
            progress.setRemainingHours(result.getInt("remainingHours"));
        }

        connection.close();
        statement.close();
        result.close();

        return progress;
    }

    @Override
    public int registerReport(Report report) throws SQLException {
        String query = "INSERT INTO Report (date, reportType, hours, studentId, " +
                "reportPath, period) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = StudentDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setDate(1, Date.valueOf(report.getDate().toString()));
        statement.setString(2, report.getType());
        statement.setInt(3, report.getHours());
        statement.setInt(4, report.getStudentId());
        statement.setString(5, report.getReportPath());
        statement.setString(6, report.getPeriod());
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        int reportID = 0;
        if (result.next()) {
            reportID = result.getInt(1);
        }

        connection.close();
        statement.close();
        result.close();

        return reportID;
    }

    @Override
    public List<Report> getReports() throws SQLException {
        String query = "SELECT * FROM Report";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        List<Report> reports = new ArrayList<>();
        while (result.next()) {
            Report report = new Report();
            report.setId(result.getInt("reportID"));
            report.setDate(result.getDate("date"));
            report.setType(result.getString("reportType"));
            report.setHours(result.getInt("hours"));
            report.setPeriod(result.getString("period"));
            report.setStudentId(result.getInt("studentId"));
            report.setReportPath(result.getString("reportPath"));
            reports.add(report);
        }

        connection.close();
        statement.close();
        result.close();

        return reports;
    }

    @Override
    public List<Report> getReportsByStudentID(int id) throws SQLException {
        String query = "SELECT * FROM Report WHERE studentID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        List<Report> reports = new ArrayList<>();
        while (result.next()) {
            Report report = new Report();
            report.setId(result.getInt("reportID"));
            report.setDate(result.getDate("date"));
            report.setType(result.getString("reportType"));
            report.setHours(result.getInt("hours"));
            report.setStudentId(result.getInt("studentID"));
            report.setReportPath(result.getString("reportPath"));
            report.setPeriod(result.getString("period"));
            reports.add(report);
        }

        connection.close();
        statement.close();
        result.close();

        return reports;
    }

    @Override
    public int deleteReport(int id) throws SQLException {
        String query = "DELETE FROM Report WHERE reportID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }

    @Override
    public List<String> getReportsType() throws SQLException {
        String query = "SELECT type FROM ReportTypeCatalog";
        Connection connection = StudentDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        List<String> types = new ArrayList<>();
        while (result.next()) {
            types.add(result.getString("type"));
        }

        connection.close();
        statement.close();
        result.close();

        return types;
    }

    @Override
    public int updateStudentCredentialsById(UniversityAffiliate affiliate) throws SQLException {
        String query = "UPDATE Student SET email = ?, username = ?, password = ? WHERE studentID = ?";
        Connection connection = CoordinatorDBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, affiliate.getEmail());
        statement.setString(2, affiliate.getUserName());
        statement.setString(3, affiliate.getPassword());
        statement.setInt(4, affiliate.getId());
        int affectedRows = statement.executeUpdate();

        connection.close();
        statement.close();

        return affectedRows;
    }
}
