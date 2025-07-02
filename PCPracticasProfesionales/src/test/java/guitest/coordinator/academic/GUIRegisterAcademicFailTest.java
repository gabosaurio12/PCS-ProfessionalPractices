package guitest.coordinator.academic;

import gui.coordinator.academics.GUIAcademicsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIRegisterAcademicFailTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Academic/GUIAcademics.fxml"));
        Parent root = loader.load();
        GUIAcademicsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUIRegisterAcademicTestFail");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void registrationTestFail() {
        clickOn("#newButton");
        sleep(500);
        clickOn("#personalNumberTextField").write("999995");
        clickOn("#emailTextField").write("gabriela@@gmail.com");

        verifyThat("#personalNumberTextField", hasText("999995"));
        verifyThat("#emailTextField", hasText("gabriela@@gmail.com"));

        clickOn("#saveButton");
        sleep(1000);
        push(KeyCode.ENTER);
        sleep(1000);
        push(KeyCode.ENTER);
        sleep(2000);
    }
}
