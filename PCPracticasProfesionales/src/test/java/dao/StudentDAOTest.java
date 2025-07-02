package dao;

import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.login.LoginDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import model.Academic;
import model.Report;
import model.Student;
import model.UniversityAffiliate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentDAOTest {
    static StudentDAOImplementation dao = new StudentDAOImplementation();
    private static final Logger logger = LogManager.getLogger(StudentDAOTest.class);
    private static final int[] studentsID = new int[2];
    private static int academicID;
    private static int reportID;

    @BeforeAll
    public static void setUp() {
        Academic academic = new Academic();
        academic.setName("Juan Carlos");
        academic.setFirstSurname("Perez");
        academic.setSecondSurname("Arriaga");
        academic.setEmail("revo@uv.mx");
        academic.setPersonalNumber("99999");
        academic.setUserName("revoover");
        academic.setPassword("pcs");
        academic.setRole("ACADÉMICO");

        try {
            academicID = new AcademicDAOImplementation().registerAcademic(academic);
        } catch (SQLException e) {
            logger.error("Error al registrar Académico", e);
        }

        Student student = new Student();
        student.setTuition("S23014038");
        student.setName("Gabriel Antonio");
        student.setFirstSurname("González");
        student.setSecondSurname("López");
        student.setCreditAdvance(300);
        student.setEmail(student.generateEmail());
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());
        student.setGrade(10F);
        student.setAcademicId(academicID);

        studentsID[0] = 0;
        try {
            studentsID[0] = dao.registerStudent(student);
        } catch (SQLException e) {
            logger.error("Error al registrar Estudiante", e);
        }
    }

    @Test
    public void studentIsNullTest() {
        Student student = new Student();
        Assertions.assertTrue(student.isNull());
    }

    @Test
    public void studentIsNullTestFail() {
        Student student = new Student();
        try {
            student = dao.getStudentByID(studentsID[0]);
        } catch (SQLException e) {
            logger.error("Error al leer Estudiante", e);
        }
        Assertions.assertFalse(student.isNull());
    }

    @Order(1)
    @Test
    public void registerStudentTest() {
        Student student = new Student();
        student.setTuition("S23014040");
        student.setName("Miguel Angel");
        student.setFirstSurname("Tortuga");
        student.setSecondSurname("Ninja");
        student.setCreditAdvance(295);
        student.setEmail(student.generateEmail());
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());
        student.setGrade(10F);
        student.setAcademicId(academicID);

        studentsID[1] = 0;
        try {
            studentsID[1] = dao.registerStudent(student);
        } catch (SQLException e) {
            logger.error("Error al registrar Estudiante", e);
        }
        Assertions.assertNotEquals(0, studentsID[1]);
    }

    @Test
    public void registerStudentDuplicateTuitionTestFailException() {
        Student student = new Student();
        student.setTuition("S23014038");
        student.setName("Gabriel Antonio");
        student.setFirstSurname("González");
        student.setSecondSurname("López");
        student.setEmail(student.generateEmail());
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());
        student.setCreditAdvance(300);
        student.setGrade(10F);
        student.setAcademicId(13);

        Assertions.assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> dao.registerStudent(student));
    }

    @Order(2)
    @Test
    public void assignSectionTest() {
        int sectionId = 1;
        int studentId = studentsID[0];
        int affectedRows = 0;
        try {
            affectedRows = dao.assignSection(studentId, sectionId);
        } catch (SQLException e) {
            logger.error("Hubo un error al asignar sección al estudiante", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void assignSectionTestFailException() {
        int sectionId = 5000;
        int studentId = studentsID[0];
        Assertions.assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> dao.assignSection(studentId, sectionId));
    }

    @Test
    public void readStudentByTuitionTest() {
        Student student = new Student();
        String tuition = "S23014038";
        try {
            student = dao.getStudentByTuition(tuition);
        } catch (SQLException e) {
            logger.error("Error al leer Estudiante", e);
        }
        Assertions.assertFalse(student.isNull());
    }

    @Test
    public void readStudentByTuitionTestFail() {
        Student student = new Student();
        String tuition = "30140";
        try {
            student = dao.getStudentByTuition(tuition);
        } catch (SQLException e) {
            logger.error("Error al leer Estudiante", e);
        }
        Assertions.assertTrue(student.isNull());
    }

    @Test
    public void updateStudentTest() {
        int affectedRows = 0;
        String tuition = "S23014038";
        try {
            Student student = dao.getStudentByTuition(tuition);
            student.setPassword("54321");
            affectedRows = dao.updateStudentByTuition(student, "S23014038");
        } catch (SQLException e) {
            logger.error("Error al actualizar Estudiante", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void updateStudentFailTest() {
        int affectedRows = 0;
        try {
            Student student = dao.getStudentByTuition("S23014038");
            student.setPassword("");
            if (student.validateData()[8]) {
                affectedRows = dao.updateStudentByTuition(student, "S23014038");
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar Estudiante", e);
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @Test
    public void updateAutoevaluationGradeTest() {
        int affectedRows = 0;
        Double grade = 50.0;
        try {
            affectedRows = dao.updateAutoevaluationGradeById(grade, studentsID[0]);
        } catch (SQLException e) {
            logger.error("Error al actualizar calificación de autoevaluación", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void updateAutoevaluationGradeTestFail() {
        int affectedRows = 0;
        double grade = 100.0;
        if (grade > 0 && grade < 51) {
            try {
                affectedRows = dao.updateAutoevaluationGradeById(grade, studentsID[0]);
            } catch (SQLException e) {
                logger.error("Error al actualizar calificación de autoevaluación", e);
            }
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @Order(3)
    @Test
    public void registerReportTest() {
        Report report = new Report();
        report.setReportPath("path/of/the/report.pdf");
        report.setHours(80);
        report.setType("Mensual");
        LocalDate date = LocalDate.of(2025, 6, 23);
        report.setDate(Date.valueOf(date));
        report.setPeriod("202551");
        report.setStudentId(studentsID[0]);

        try {
            reportID = dao.registerReport(report);
        } catch (SQLException e) {
            logger.error("Error al registrar reporte", e);
        }
        Assertions.assertNotEquals(0, reportID);
    }

    @Test
    public void registerReportTestFailException() {
        Report report = new Report();
        report.setReportPath("path/of/the/report.pdf");
        LocalDate date = LocalDate.of(2025, 6, 23);
        report.setDate(Date.valueOf(date));
        report.setPeriod("202551");
        report.setStudentId(studentsID[0]);

        Assertions.assertThrows(
                NullPointerException.class,
                () -> dao.registerReport(report));
    }

    @Test
    public void updateStudentCredentialsByIDTest() {
        UniversityAffiliate affiliate = new UniversityAffiliate();
        affiliate.setEmail("gabriel@estudiantes.uv.mx");
        affiliate.setUserName("gabriel");
        affiliate.setId(studentsID[0]);
        String hashedPassword = null;
        int affectedRows = 0;
        try {
            hashedPassword = new LoginDAOImplementation().getHashedPassword("iloverevo");
            affiliate.setPassword(hashedPassword);
            affectedRows = dao.updateStudentCredentialsById(affiliate);
        } catch (SQLException e) {
            logger.error("Error al hashear contraseña", e);
        }
        affiliate.setUserName(hashedPassword);

        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void updateAcademicCredentialsTestFail() {
        UniversityAffiliate affiliate = new UniversityAffiliate();
        affiliate.setEmail("");
        affiliate.setUserName("gabriel");
        affiliate.setId(studentsID[0]);
        int affectedRows = 0;
        if (!affiliate.getEmail().isBlank()) {
            try {
                String hashedPassword = new LoginDAOImplementation().getHashedPassword("iloverevo");
                affiliate.setUserName(hashedPassword);
                affectedRows = dao.updateStudentCredentialsById(affiliate);
            } catch (SQLException e) {
                logger.error("Error al actualizar credenciales de academico", e);
            }
        }

        Assertions.assertEquals(0, affectedRows);
    }

    @Test
    public void deleteReportTest() {
        int reportAffectedRows = 0;
        try {
            reportAffectedRows = dao.deleteReport(reportID);
        } catch (SQLException e) {
            logger.error("Error al eliminar reporte", e);
        }
        Assertions.assertNotEquals(0, reportAffectedRows);
    }

    @Test
    public void deleteReportTestFail() {
        int reportAffectedRows = 0;
        int fakeReportID = 0;
        try {
            reportAffectedRows = dao.deleteReport(fakeReportID);
        } catch (SQLException e) {
            logger.error("Error al eliminar reporte", e);
        }
        Assertions.assertEquals(0, reportAffectedRows);
    }

    @Test
    public void deleteStudentTest() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropStudent(studentsID[1]);
        } catch (SQLException e) {
            logger.error("Error al eliminar estudiante", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void deleteStudentTestFail() {
        int affectedRows = 0;
        int fakeStudentID = 0;
        try {
            affectedRows = dao.dropStudent(fakeStudentID);
        } catch (SQLException e) {
            logger.error("Error al eliminar estudiante", e);
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @AfterAll
    public static void after() {
        try {
            AcademicDAOImplementation academicDAO = new AcademicDAOImplementation();
            dao.dropStudent(studentsID[0]);
            academicDAO.dropAcademic(academicID);
        } catch (SQLException e) {
            logger.error("Error al eliminar estudiante", e);
        }
    }
}
