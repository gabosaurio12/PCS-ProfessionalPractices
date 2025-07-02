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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIStudentDetailsTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIStudentDetailsTest.class);
    private static String tuition;
    private static int studentID;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Student/GUIStudents.fxml"));
        Parent root = loader.load();
        GUIStudentsController controller = loader.getController();
        Student student = registerStudent();
        controller.initComponents();
        tuition = student.getTuition();
        stage.setTitle("GUIUpdateStudentTest");
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

    @Test
    void updateTest() {
        clickOn("S23014038");
        sleep(500);
        clickOn("#editButton");
        doubleClickOn("#tuitionTextField").write("S23888888");
        doubleClickOn("#creditAdvanceTextField").write("350");

        verifyThat("#tuitionTextField", hasText("S23888888"));
        verifyThat("#creditAdvanceTextField", hasText("350"));

        clickOn("#saveButton");
        sleep(250);
        push(KeyCode.ENTER);
        tuition = "S23888888";
        clickOn(tuition);
        sleep(1500);
    }

    @AfterAll
    public static void after() {
        StudentDAOImplementation dao = new StudentDAOImplementation();
        try {
            dao.dropStudent(studentID);
        } catch (SQLException e) {
            logger.error("Error al eliminar estudiante", e);
        }
    }
}
