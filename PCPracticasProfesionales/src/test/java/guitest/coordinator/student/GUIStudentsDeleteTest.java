package guitest.coordinator.student;

import businesslogic.student.StudentDAOImplementation;
import gui.coordinator.student.GUIStudentsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GUIStudentsDeleteTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIStudentsDeleteTest.class);
    private static final StudentDAOImplementation dao = new StudentDAOImplementation();
    private static int studentID;

    @BeforeAll
    public static void setUp() {
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
            studentID = dao.registerStudent(student);
            int sectionID = 1;
            dao.assignSection(studentID, sectionID);
        } catch (SQLException e) {
            logger.error("Error al registrar Estudiante", e);
        }
    }

    @Order(1)
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Student/GUIStudents.fxml"));
        Parent root = loader.load();
        GUIStudentsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUIStudentsDeleteTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Order(2)
    @Test
    public void dropStudentTest() {
        sleep(500);
        clickOn("S23014038");
        clickOn("#deleteButton");

        sleep(500);
        push(KeyCode.ENTER);
    }

    @Order(3)
    @Test
    public void readDeletedStudentTestFail() {
        Student student = new Student();
        try {
            student = dao.getStudentByID(studentID);
        } catch (SQLException e) {
            logger.error("Error al leer estudiante", e);
        }
        Assertions.assertTrue(student.isNull());
    }

    @AfterAll
    public static void deleteStudent() {
        StudentDAOImplementation dao = new StudentDAOImplementation();
        try {
            dao.dropStudent(studentID);
        } catch (SQLException e) {
            logger.error("Error al eliminar estudiante", e);
        }
    }
}
