package dao;

import businesslogic.presentation.PresentationDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import model.Presentation;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PresentationDAOTest {
    private static final PresentationDAOImplementation dao = new PresentationDAOImplementation();
    private static final Logger logger = LogManager.getLogger(PresentationDAOTest.class);
    private static final int[] presentationsID = new int[2];
    private static int studentID;

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
        try {
            StudentDAOImplementation studentDAO = new StudentDAOImplementation();
            studentID = studentDAO.registerStudent(student);
            presentation.setStudentId(studentID);
            presentationsID[0] = dao.createPresentation(presentation);
        } catch (SQLException e) {
            logger.error("Error al crear presentación: ", e);
        }
    }

    @Order(1)

    @Test
    public void createPresentationTest() {
        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 8, 30);
        presentation.setDate(Date.valueOf(date));
        presentation.setStudentId(studentID);
        presentation.setPresentationPath("");

        if (presentation.validateData()[3]) {
            try {
                presentationsID[1] = dao.createPresentation(presentation);
            } catch (SQLException e) {
                logger.error("Error al crear presentación: ", e);
            }
        }
        Assertions.assertNotEquals(0, presentationsID[1]);
    }

    @Test
    public void createPresentationTestFail() {
        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 8, 30);
        presentation.setDate(Date.valueOf(date));
        presentation.setPresentationPath("");

        int presentationID = 0;
        if (presentation.validateData()[3]) {
            try {
                presentationID = dao.createPresentation(presentation);
            } catch (SQLException e) {
                logger.error("Error al crear presentación: ", e);
            }
        }

        Assertions.assertEquals(0, presentationID);
    }

   @Test
   public void getStudentPresentationsTest() {
       List<Presentation> presentations = new ArrayList<>();
       try {
           presentations = dao.getStudentPresentations(studentID);
       } catch (SQLException e) {
           logger.error("Error al obtener presentaciones de estudiante", e);
       }
       Assertions.assertFalse(presentations.isEmpty());
   }

   @Test
   public void getStudentPresentationsTestFail() {
       List<Presentation> presentations = new ArrayList<>();
       int studentId = 0;
       try {
           presentations = dao.getStudentPresentations(studentId);
       } catch (SQLException e) {
           logger.error("Error al obtener presentaciones de estudiante", e);
       }
       Assertions.assertTrue(presentations.isEmpty());
   }

   @Test
   public void getPresentationsTest() {
       List<Presentation> presentations = new ArrayList<>();
       try {
           presentations = dao.getPresentations();
       } catch (SQLException e) {
           logger.error("Error al obtener presentaciones", e);
       }
       Assertions.assertFalse(presentations.isEmpty());
   }

   @Test
   public void updatePresentationTest() {
       Presentation presentation = new Presentation();
       try {
           presentation = dao.getPresentationById(presentationsID[0]);
       } catch (SQLException e) {
           logger.error("Error al leer presentación", e);
       }
       if (!presentation.isNull()) {
           presentation.setPresentationGrade(80.0);
       }
       int affectedRows = 0;
       if (presentation.validateData()[3]) {
           try {
               affectedRows = dao.updatePresentation(presentation);
           } catch (SQLException e) {
               logger.error("Error al actualizar presentación", e);
           }
       }
       Assertions.assertNotEquals(0, affectedRows);
   }

    @Test
    public void updatePresentationInvalidGradeTest() {
        Presentation presentation = new Presentation();
        try {
            presentation = dao.getPresentationById(presentationsID[0]);
        } catch (SQLException e) {
            logger.error("Error al leer presentación", e);
        }
        if (!presentation.isNull()) {
            presentation.setPresentationGrade(150.0);
        }
        int affectedRows = 0;
        if (presentation.validateData()[3]) {
            try {
                affectedRows = dao.updatePresentation(presentation);
            } catch (SQLException e) {
                logger.error("Error al actualizar presentación", e);
            }
        }
        Assertions.assertEquals(0, affectedRows);
    }

   @Test
   public void updatePresentationTestFail() {
       Presentation presentation = new Presentation();
       try {
           presentation = dao.getPresentationById(presentationsID[0]);
       } catch (SQLException e) {
           logger.error("Error al leer presentación", e);
       }
       if (!presentation.isNull()) {
           presentation.setDate(null);
       }
       int affectedRows = 0;
       if (presentation.validateData()[3]) {
           try {
               affectedRows = dao.updatePresentation(presentation);
           } catch (SQLException e) {
               logger.error("Error al actualizar presentación", e);
           }
       }
       Assertions.assertEquals(0, affectedRows);
   }

   @Order(2)
   @Test
   public void dropPresentationTest() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropPresentation(presentationsID[1]);
        } catch (SQLException e) {
            logger.error("Error al eliminar presentación", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
   }

    @Test
    public void dropPresentationTestFail() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropPresentation(0);
        } catch (SQLException e) {
            logger.error("Error al eliminar presentación", e);
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @AfterAll
    public static void after() {
        try {
            StudentDAOImplementation studentDAO = new StudentDAOImplementation();
            studentDAO.dropStudent(studentID);
            dao.dropPresentation(presentationsID[0]);
        } catch (SQLException e) {
            logger.error("Hubo un error al eliminar la presentación", e);
        }
    }
}
