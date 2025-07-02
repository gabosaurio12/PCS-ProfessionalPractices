package guitest.coordinator.student;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import gui.coordinator.student.GUIStudentDetailsController;
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

public class GUIStudentDetailsFailTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIStudentDetailsFailTest.class);
    private static String tuition;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Student/GUIStudentDetails.fxml"));
        Parent root = loader.load();
        GUIStudentDetailsController controller = loader.getController();
        Student student = registerStudent();
        controller.initComponents(student,
                new SystemDAOImplementation().getCurrentSection().getSectionID());
        tuition = student.getTuition();
        stage.setTitle("GUIUpdateStudentTestFail");
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

        int studentID = 0;
        try {
            StudentDAOImplementation dao = new StudentDAOImplementation();
            studentID = dao.registerStudent(student);
        } catch (SQLException e) {
            logger.error("Error al registrar Estudiante", e);
        }
        student.setId(studentID);
        return student;
    }

    @Test
    void updateSuccess() {
        doubleClickOn("#tuitionTextField").write("S2388888848");
        doubleClickOn("#creditAdvanceTextField").write("270");
        clickOn("#emailTextField");
        doubleClickOn("#emailTextField").write("zS23888888@@estudiantes.uv.mx");

        verifyThat("#tuitionTextField", hasText("S2388888848"));
        verifyThat("#creditAdvanceTextField", hasText("270"));
        verifyThat("#emailTextField", hasText("zS23888888@@estudiantes.uv.mx"));

        clickOn("#saveButton");
        sleep(500);
        push(KeyCode.ENTER);
        sleep(500);
        push(KeyCode.ENTER);
        sleep(1250);
    }

    @AfterAll
    public static void deleteStudent() {
        StudentDAOImplementation dao = new StudentDAOImplementation();
        try {
            Student student = dao.getStudentByTuition(tuition);
            if (student != null) {
                dao.dropStudent(student.getId());
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar estudiante", e);
        }
    }
}
