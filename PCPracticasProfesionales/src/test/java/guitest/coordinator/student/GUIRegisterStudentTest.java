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

public class GUIRegisterStudentTest extends ApplicationTest {

    private static final Logger logger = LogManager.getLogger(GUIRegisterStudentTest.class);

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Student/GUIStudents.fxml"));
        Parent root = loader.load();
        GUIStudentsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUIRegisterStudentTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void registrationTest() {
        clickOn("#newButton");
        sleep(500);
        clickOn("#tuitionTextField").write("S23014038");
        clickOn("#nameTextField").write("Gabriel Antonio");
        clickOn("#firstSurnameTextField").write("González");
        clickOn("#secondSurnameTextField").write("López");
        clickOn("#creditAdvanceTextField").write("300");
        clickOn("#academicBox");
        sleep(500);
        clickOn("Remus Lupin");

        verifyThat("#tuitionTextField", hasText("S23014038"));
        verifyThat("#nameTextField", hasText("Gabriel Antonio"));
        verifyThat("#firstSurnameTextField", hasText("González"));
        verifyThat("#secondSurnameTextField", hasText("López"));
        verifyThat("#creditAdvanceTextField", hasText("300"));
        verifyThat("#emailTextField", hasText("zS23014038@estudiantes.uv.mx"));

        clickOn("#saveButton");
        sleep(500);
        push(KeyCode.ENTER);
        sleep(500);
        clickOn("S23014038");
        sleep(2000);
    }

    @AfterAll
    public static void after() {
        StudentDAOImplementation dao = new StudentDAOImplementation();
        try {
            Student student = dao.getStudentByTuition("S23014038");
            if (!student.isNull()) {
                dao.dropStudent(student.getId());
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar estudiante", e);
        }
    }
}
