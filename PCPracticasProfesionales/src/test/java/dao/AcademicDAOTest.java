package dao;

import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.login.LoginDAOImplementation;
import businesslogic.presentation.PresentationDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AcademicDAOTest {
    static AcademicDAOImplementation dao = new AcademicDAOImplementation();
    private static final Logger logger = LogManager.getLogger(AcademicDAOTest.class);
    private static final int[] academicsID = new int[2];
    private static int presentationID;
    private static int studentID;
    private static int evaluationID;

    @BeforeAll
    public static void setUp() {
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
        student.setAcademicId(13);

        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 6, 23);
        presentation.setDate(Date.valueOf(date));
        presentation.setPresentationPath("/ruta/a/presentacion/pdf");

        Academic academic = new Academic();
        academic.setName("Juan Carlos");
        academic.setFirstSurname("Perez");
        academic.setSecondSurname("Arriaga");
        academic.setEmail("revoover@uv.mx");
        academic.setPersonalNumber("99999");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        academicsID[0] = 0;
        try {
            academicsID[0] = dao.registerAcademic(academic);
            PresentationDAOImplementation presentationDAO = new PresentationDAOImplementation();
            StudentDAOImplementation studentDAO = new StudentDAOImplementation();
            studentID = studentDAO.registerStudent(student);
            presentation.setStudentId(studentID);
            presentationID = presentationDAO.createPresentation(presentation);
        } catch (SQLException e) {
            logger.error("Error al registrar Académico, presentación o estudiante", e);
        }
        Assertions.assertNotEquals(0, academicsID[0]);
    }

    @Order(1)
    @Test
    public void registerAcademicShortPersonalNumberTest() {
        Academic academic = new Academic();
        academic.setName("Michael");
        academic.setFirstSurname("Jackson");
        academic.setSecondSurname("");
        academic.setEmail("mj@uv.mx");
        academic.setPersonalNumber("01");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        academicsID[1] = 0;
        try {
            academicsID[1] = dao.registerAcademic(academic);
        } catch (SQLException e) {
            logger.error("Error al registrar Académico", e);
        }
        Assertions.assertNotEquals(0, academicsID[1]);
    }

    @Test
    public void registerAcademicLongPersonalNumberTestFail() {
        Academic academic = new Academic();
        academic.setName("Junior");
        academic.setFirstSurname("Sanim");
        academic.setSecondSurname("");
        academic.setEmail("junior@uv.mx");
        academic.setPersonalNumber("123456");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        int academicID = 0;
        boolean[] flags = academic.validateData();
        if (flags[flags.length - 1]) {
            try {
                academicID= dao.registerAcademic(academic);
            } catch (SQLException e) {
                logger.error("Error al registrar Académico", e);
            }
        }
        Assertions.assertEquals(0, academicID);
    }

    @Test
    public void registerAcademicLongPersonalNumberDataValidationTestFail() {
        Academic academic = new Academic();
        academic.setName("Quico");
        academic.setFirstSurname("Marinero");
        academic.setSecondSurname("");
        academic.setEmail("quico@uv.mx");
        academic.setPersonalNumber("123456");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        int academicID = 0;
        boolean[] flags = academic.validateData();
        if (flags[flags.length - 1]) {
            try {
                academicID= dao.registerAcademic(academic);
            } catch (SQLException e) {
                logger.error("Error al registrar Académico", e);
            }
        }
        Assertions.assertEquals(0, academicID);
    }

    @Test
    public void registerAcademicTestFailException() {
        Academic academic = new Academic();
        academic.setName("Juan Carlos");
        academic.setFirstSurname("Perez");
        academic.setSecondSurname("Arriaga");
        academic.setEmail("revo@uv.mx");
        academic.setUserName("elrevo");
        academic.setPassword("pcs");
        academic.setRole("ACADÉMICO");

        Assertions.assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> dao.registerAcademic(academic));
    }

    @Test
    public void registerAcademicMissingDataTestFail() {
        Academic academic = new Academic();
        academic.setPersonalNumber("");
        academic.setName("Juan Carlos");
        academic.setFirstSurname("Perez");
        academic.setSecondSurname("Arriaga");
        academic.setEmail("revo@uv.mx");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        boolean[] flags = academic.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }

    @Test
    public void registerAcademicInvalidEmailTestFail() {
        Academic academic = new Academic();
        academic.setName("Michael");
        academic.setFirstSurname("Jordan");
        academic.setSecondSurname("");
        academic.setEmail("michj@@uv.mx");
        academic.setPersonalNumber("99999");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        boolean[] flags = academic.validateData();

        Assertions.assertFalse(flags[flags.length - 1]);
    }

    @Test
    public void getAcademicByCredentialsTest() {
        Academic academic = null;
        String username = "elrevo";
        String password = "pcs";
        try {
            String hashedPassword = new LoginDAOImplementation().getHashedPassword(password);
            academic = dao.getAcademicByCredentials(username, hashedPassword);
        } catch (SQLException e) {
            logger.error("Hubo un error al buscar académico por credenciales", e);
        }
        Assertions.assertNotNull(academic);
    }

    @Test
    public void getAcademicByCredentialsTestFail() {
        Academic academic = new Academic();
        String username = "elrevo";
        String password = "154687521";
        try {
            String hashedPassword = new LoginDAOImplementation().getHashedPassword(password);
            academic = dao.getAcademicByCredentials(username, hashedPassword);
        } catch (SQLException e) {
            logger.error("Hubo un error al buscar académico por credenciales", e);
        }
        Assertions.assertTrue(academic.isNull());
    }

    @Test
    public void readAcademicTest() {
        Academic academic = null;
        try {
            academic = dao.getAcademicByPersonalNumber("99999");
        } catch (SQLException e) {
            logger.error("Error al leer Académico", e);
        }
        Assertions.assertNotNull(academic);
    }

    @Test
    public void readAcademicByIDTest() {
        Academic academic = null;
        try {
            int id = dao.getAcademicByPersonalNumber("99999").getId();
            academic = dao.getAcademicByID(id);
        } catch (SQLException e) {
            logger.error("Error al leer Académico", e);
        }
        Assertions.assertNotNull(academic);
    }

    @Test
    public void updateAcademicTest() {
        int affectedRows = 0;
        try {
            Academic academic = dao.getAcademicByID(academicsID[0]);
            academic.setPassword("isof");
            affectedRows = dao.updateAcademicByID(academic);
        } catch (SQLException e) {
            logger.error("Error al actualizar Académico", e);
        }

        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void updateAcademicCredentialsTest() {
        UniversityAffiliate affiliate = new UniversityAffiliate();
        affiliate.setEmail("elrevo@uv.mx");
        affiliate.setUserName("revolover");
        affiliate.setId(academicsID[0]);
        String hashedPassword = "";
        int affectedRows = 0;
        try {
            hashedPassword = new LoginDAOImplementation().getHashedPassword("iloverevo");
            affiliate.setPassword(hashedPassword);
            affectedRows = dao.updateAcademicCredentialsById(affiliate);
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
        affiliate.setUserName("revolover");
        affiliate.setId(academicsID[0]);
        int affectedRows = 0;
        if (!affiliate.getEmail().isBlank()) {
            try {
                String hashedPassword = new LoginDAOImplementation().getHashedPassword("iloverevo");
                affiliate.setUserName(hashedPassword);
                affectedRows = dao.updateAcademicCredentialsById(affiliate);
            } catch (SQLException e) {
                logger.error("Error al actualizar credenciales de academico", e);
            }
        }

        Assertions.assertEquals(0, affectedRows);
    }

    @Test
    public void readAcademicByPersonalNumberNotFoundTestFail() {
        Academic academic = new Academic();
        try {
            academic = dao.getAcademicByPersonalNumber("77361");
        } catch (SQLException e) {
            logger.error("Error al leer Académico", e);
        }

        Assertions.assertTrue(academic.isNull());
    }

    @Test
    public void readAcademicByIDNotFoundTestFail() {
        Academic academic = new Academic();
        try {
            academic = dao.getAcademicByID(8000);
        } catch (SQLException e) {
            logger.error("Error al leer Académico", e);
        }
        Assertions.assertTrue(academic.isNull());
    }

    @Test
    public void updateAcademicTestFail() {
        int affectedRows = 0;
        try {
            Academic academic = dao.getAcademicByID(academicsID[0]);
            academic.setName("");
            if (academic.validateData()[5]) {
                affectedRows = dao.updateAcademicByID(academic);
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar Académico", e);
        }

        Assertions.assertEquals(0, affectedRows);
    }

    @Test
    public void assignSectionTest() {
        int sectionId = 1;
        int affectedRows = 0;
        try {
            affectedRows = dao.assignSection(academicsID[0], sectionId);
        } catch (SQLException e) {
            logger.error("Hubo un error al asignar la sección", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void assignSectionTestFailException() {
        int sectionId = 5;
        Assertions.assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> dao.assignSection(academicsID[0], sectionId));
    }

    @Order(2)
    @Test
    public void addEvaluationTest() {
        Evaluation evaluation = new Evaluation();
        evaluation.setAcademicId(academicsID[0]);
        evaluation.setEvaluationPath("/ruta/de/prueba/a/archivo.pdf");
        evaluation.setAverageGrade(48.0);
        evaluation.setPresentationId(presentationID);
        evaluationID = 0;
        try {
            evaluationID = dao.addEvaluation(evaluation);
        } catch (SQLException e) {
            logger.error("Error al agregar evaluación", e);
        }
        Assertions.assertNotEquals(0, evaluationID);
    }

    @Test
    public void addEvaluationTestFailException() {
        Evaluation evaluation = new Evaluation();
        evaluation.setAcademicId(academicsID[0]);
        evaluation.setEvaluationPath("/ruta/de/prueba/a/archivo.pdf");
        evaluation.setAverageGrade(48.0);
        evaluation.setPresentationId(0);
        Assertions.assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> dao.addEvaluation(evaluation));
    }

    @Order(3)
    @Test
    public void dropEvaluationTest() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropEvaluation(evaluationID);
        } catch (SQLException e) {
            logger.error("Error al eliminar evaluación", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void dropEvaluationTestFail() {
        int affectedRows = 0;
        int fakeEvaluationID = 0;
        try {
            affectedRows = dao.dropEvaluation(fakeEvaluationID);
        } catch (SQLException e) {
            logger.error("Error al eliminar evaluación", e);
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @Test
    public void dropAcademicTest() {
        int affectedRows = 0;
        int academicID = academicsID[1];
        try {
            affectedRows = dao.dropAcademic(academicID);
        } catch (SQLException e) {
            logger.error("Error al eliminar Académico", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void dropAcademicTestFail() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropAcademic(0);
        } catch (SQLException e) {
            logger.error("Error al eliminar Académico", e);
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @AfterAll
    public static void after() {
        try {
            dao.dropAcademic(academicsID[0]);
            StudentDAOImplementation studentDAO = new StudentDAOImplementation();
            studentDAO.dropStudent(studentID);
            PresentationDAOImplementation presentationDAO = new PresentationDAOImplementation();
            presentationDAO.dropPresentation(presentationID);
        } catch (SQLException e) {
            logger.error("Error al eliminar Académico", e);
        }
    }
}