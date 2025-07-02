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

public class GUIRegisterStudentFailTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIRegisterStudentFailTest.class);

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Student/GUIStudents.fxml"));
        Parent root = loader.load();
        GUIStudentsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUIRegisterStudentFailTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void registrationTestFail() {
        clickOn("#newButton");
        sleep(500);
        clickOn("#tuitionTextField").write("S230140380");
        clickOn("#creditAdvanceTextField").write("262");

        verifyThat("#tuitionTextField", hasText("S230140380"));
        verifyThat("#creditAdvanceTextField", hasText("262"));
        verifyThat("#emailTextField", hasText("zS230140380@estudiantes.uv.mx"));

        clickOn("#saveButton");
        sleep(1000);
        push(KeyCode.ENTER);
        sleep(1000);
        push(KeyCode.ENTER);
        sleep(1000);
    }

    @AfterAll
    public static void after() {
        StudentDAOImplementation dao = new StudentDAOImplementation();
        try {
            Student student = dao.getStudentByTuition("S23014038");
            if (student != null) {
                dao.dropStudent(student.getId());
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar estudiante", e);
        }
    }
}
