package guitest.student;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIProjectTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/GUILogin.fxml"));
        Parent root = loader.load();
        stage.setTitle("GUIAvailableProjectsTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void projectTest() {
        String username = "zS25000001";
        String password = "zS25000001345";
        clickOn("#userNameTextField").write(username);
        interact(() ->
                ((PasswordField) lookup("#userPasswordTextField").query()
                ).setText(password));
        verifyThat("#userNameTextField", hasText(username));
        verifyThat("#userPasswordTextField", hasText(password));
        clickOn("#loginButton");
        sleep(1500);

        clickOn("#projectButton");
        sleep(1500);

        clickOn("#closeButton");
        sleep(500);
    }
}
