package guitest.statistics;

import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.presentation.PresentationDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import gui.coordinator.student.GUIStudentsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Academic;
import model.Evaluation;
import model.Presentation;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class GUIPresentationStatisticsFailTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIPresentationStatisticsFailTest.class);
    private static int studentID;
    private static int presentationID;
    private static int academicID;
    private static int evaluationID;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Student/GUIStudents.fxml"));
        Parent root = loader.load();
        GUIStudentsController controller = loader.getController();
        registerStudent();
        registerAcademic();
        registerPresentation();
        registerEvaluation();
        controller.initComponents();
        stage.setTitle("GUIPresentationStatisticsTestFail");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public Student registerStudent() {
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

        studentID = 0;
        try {
            StudentDAOImplementation dao = new StudentDAOImplementation();
            studentID = dao.registerStudent(student);
            int sectionID = 1;
            dao.assignSection(studentID, sectionID);
        } catch (SQLException e) {
            logger.error("Error al registrar Estudiante", e);
        }
        student.setId(studentID);
        return student;
    }

    public void registerPresentation() {
        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 8, 30);
        presentation.setDate(Date.valueOf(date));
        presentation.setStudentId(studentID);
        presentation.setPresentationPath("");

        boolean[] flags = presentation.validateData();
        if (flags[flags.length - 1]) {
            try {
                PresentationDAOImplementation dao = new PresentationDAOImplementation();
                presentationID = dao.createPresentation(presentation);
            } catch (SQLException e) {
                logger.error("Error al crear presentación: ", e);
            }
        } else {
            logger.error("No se registró la presentación");
        }
    }

    public void registerAcademic() {
        Academic academic = new Academic();
        academic.setName("Michael");
        academic.setFirstSurname("Jackson");
        academic.setSecondSurname("");
        academic.setEmail("mj@uv.mx");
        academic.setPersonalNumber("01");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        academicID = 0;
        try {
            AcademicDAOImplementation dao = new AcademicDAOImplementation();
            academicID = dao.registerAcademic(academic);
            int sectionID = 1;
            dao.assignSection(academicID, sectionID);
        } catch (SQLException e) {
            logger.error("Error al registrar Académico", e);
        }
    }

    public void registerEvaluation() {
        Evaluation evaluation = new Evaluation();
        evaluation.setAcademicId(academicID);
        evaluation.setEvaluationPath("/ruta/de/prueba/a/archivo.pdf");
        evaluation.setAverageGrade(100.0);
        evaluation.setPresentationId(presentationID);
        evaluationID = 0;
        try {
            AcademicDAOImplementation dao = new AcademicDAOImplementation();
            evaluationID = dao.addEvaluation(evaluation);
        } catch (SQLException e) {
            logger.error("Error al agregar evaluación", e);
        }
    }

    @Test
    void presentationStatisticsTestFail() {
        clickOn("S23014038");
        sleep(1000);
        clickOn("#statisticsButton");
        sleep(1000);
        clickOn("#detailsButton");
        sleep(1000);
    }

    @AfterAll
    public static void after() {
        StudentDAOImplementation studentDAO = new StudentDAOImplementation();
        PresentationDAOImplementation presentationDAO = new PresentationDAOImplementation();
        AcademicDAOImplementation academicDAO = new AcademicDAOImplementation();
        try {
            academicDAO.dropEvaluation(evaluationID);
            presentationDAO.dropPresentation(presentationID);
            studentDAO.dropStudent(studentID);
            academicDAO.dropAcademic(academicID);
        } catch (SQLException e) {
            logger.error("Error al eliminar estudiante", e);
        }
    }
}
