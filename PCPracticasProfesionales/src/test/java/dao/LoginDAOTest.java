package dao;

import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.login.LoginDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import model.Academic;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class LoginDAOTest {

    private static final Logger logger = LogManager.getLogger(LoginDAOTest.class);
    private static String studentUsername;
    private static String studentPassword;
    private static String academicUsername;
    private static String academicPassword;
    private static int studentID;
    private static int academicID;
    private static final LoginDAOImplementation dao = new LoginDAOImplementation();

    @BeforeAll
    public static void setUp() {
        Student student = new Student();
        student.setTuition("S23014038");
        student.setName("Gabriel Antonio");
        student.setFirstSurname("González");
        student.setSecondSurname("López");
        student.setCreditAdvance(300);
        student.setEmail(student.generateEmail());
        studentUsername = student.generateUserName();
        student.setUserName(studentUsername);
        studentPassword = student.generatePassword();
        student.setPassword(studentPassword);
        student.setGrade(10.0F);
        student.setAcademicId(13);

        Academic academic = new Academic();
        academic.setName("Juan Carlos");
        academic.setFirstSurname("Perez");
        academic.setSecondSurname("Arriaga");
        academic.setEmail("revo@uv.mx");
        academic.setPersonalNumber("99999");
        academicUsername = academic.generateUserName();
        academic.setUserName(academicUsername);
        academicPassword = academic.generatePassword();
        academic.setPassword(academicPassword);
        academic.setRole("ACADÉMICO");

        try {
            StudentDAOImplementation studentDAO = new StudentDAOImplementation();
            AcademicDAOImplementation academicDAO = new AcademicDAOImplementation();
            studentID = studentDAO.registerStudent(student);
            academicID = academicDAO.registerAcademic(academic);
        } catch (SQLException e) {
            logger.error("Error al registrar Estudiante o Academico", e);
        }
    }

    @Test
    public void getUserStudentTest() {
        boolean foundFlag = false;
        try {
            foundFlag = dao.getUser(studentUsername);
        } catch (SQLException e) {
            logger.error("Error al buscar usuario", e);
        }
        Assertions.assertTrue(foundFlag);
    }

    @Test
    public void getUserStudentTestFail() {
        boolean foundFlag = false;
        try {
            String fakeUsername = "zsO0000000";
            foundFlag = dao.getUser(fakeUsername);
        } catch (SQLException e) {
            logger.error("Error al buscar usuario", e);
        }
        Assertions.assertFalse(foundFlag);
    }

    @Test
    public void getUserRoleStudentTest() {
        String role = "";
        try {
            String hashedPassword = dao.getHashedPassword(studentPassword);
            role = dao.getUserRole(studentUsername, hashedPassword);
        } catch (SQLException e) {
            logger.error("Error al obtener rol del usuario", e);
        }
        String expectedRole = "ESTUDIANTE";
        Assertions.assertEquals(expectedRole, role);
    }

    @Test
    public void getUserRoleTestStudentFail() {
        String role = "";
        try {
            role = dao.getUserRole(studentUsername, studentPassword);
        } catch (SQLException e) {
            logger.error("Error al obtener rol del usuario", e);
        }
        Assertions.assertNull(role);
    }

    @Test
    public void getUserAcademicTest() {
        boolean foundFlag = false;
        try {
            foundFlag = dao.getUser(academicUsername);
        } catch (SQLException e) {
            logger.error("Error al buscar usuario", e);
        }
        Assertions.assertTrue(foundFlag);
    }

    @Test
    public void getUserAcademicTestFail() {
        boolean foundFlag = false;
        try {
            String fakeUsername = "makina";
            foundFlag = dao.getUser(fakeUsername);
        } catch (SQLException e) {
            logger.error("Error al buscar usuario", e);
        }
        Assertions.assertFalse(foundFlag);
    }

    @Test
    public void getUserRoleAcademicTest() {
        String role = "";
        try {
            String hashedPassword = dao.getHashedPassword(academicPassword);
            role = dao.getUserRole(academicUsername, hashedPassword);
        } catch (SQLException e) {
            logger.error("Error al obtener rol del usuario", e);
        }
        String expectedRole = "ACADÉMICO";
        Assertions.assertEquals(expectedRole, role);
    }

    @Test
    public void getUserRoleTestAcademicFail() {
        String role = "";
        try {
            role = dao.getUserRole(academicUsername, academicPassword);
        } catch (SQLException e) {
            logger.error("Error al obtener rol del usuario", e);
        }
        Assertions.assertNull(role);
    }

    @Test
    public void getHashedPasswordTest() {
        String hashedPassword = "";
        try {
            hashedPassword = dao.getHashedPassword(studentPassword);
        } catch (SQLException e) {
            logger.error("Error al obtener contraseña hasheada", e);
        }
        String expectedPassword = "4baf529d5415468db9d7a338aa09fab470df9f2f9ca90dc8c6275215cf1b93b5";
        Assertions.assertEquals(expectedPassword, hashedPassword);
    }

    @Test
    public void getHashedPasswordEmptyTest() {
        String hashedPassword = "";
        try {
            String fakePassword = "";
            hashedPassword = dao.getHashedPassword(fakePassword);
        } catch (SQLException e) {
            logger.error("Error al obtener contraseña hasheada", e);
        }
        String expectedPassword = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        Assertions.assertEquals(expectedPassword, hashedPassword);
    }

    @AfterAll
    public static void after() {
        try {
            StudentDAOImplementation studentDAO = new StudentDAOImplementation();
            studentDAO.dropStudent(studentID);
            AcademicDAOImplementation academicDAO = new AcademicDAOImplementation();
            academicDAO.dropAcademic(academicID);
        } catch (SQLException e) {
            logger.error("Error al eliminar estudiante", e);
        }
    }
}
