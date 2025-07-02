package guitest.coordinator.academic;

import businesslogic.academic.AcademicDAOImplementation;
import gui.coordinator.academics.GUIAcademicsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Academic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIRegisterAcademicTest extends ApplicationTest {

    private static final Logger logger = LogManager.getLogger(GUIRegisterAcademicTest.class);

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Academic/GUIAcademics.fxml"));
        Parent root = loader.load();
        GUIAcademicsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUIRegisterAcademicTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void registrationTest() {
        clickOn("#newButton");
        sleep(500);
        clickOn("#personalNumberTextField").write("99999");
        clickOn("#nameTextField").write("Gabriel Antonio");
        clickOn("#firstSurnameTextField").write("González");
        clickOn("#secondSurnameTextField").write("Andrade");
        clickOn("#emailTextField").write("gabriel@gmail.com");
        clickOn("#academicPositionBox");
        sleep(500);
        clickOn("ACADEMICO");

        verifyThat("#personalNumberTextField", hasText("99999"));
        verifyThat("#nameTextField", hasText("Gabriel Antonio"));
        verifyThat("#firstSurnameTextField", hasText("González"));
        verifyThat("#secondSurnameTextField", hasText("Andrade"));
        verifyThat("#emailTextField", hasText("gabriel@gmail.com"));

        clickOn("#saveButton");
        sleep(500);
        press(KeyCode.ENTER);
        sleep(500);
        clickOn("99999");
        sleep(2000);
    }

    @AfterAll
    public static void after() {
        AcademicDAOImplementation dao = new AcademicDAOImplementation();
        try {
            Academic academic = dao.getAcademicByPersonalNumber("99999");
            if (academic != null) {
                dao.dropAcademic(academic.getId());
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar academico", e);
        }
    }
}
