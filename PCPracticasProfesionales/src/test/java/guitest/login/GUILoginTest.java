package guitest.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

class GUILoginTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GUILogin.fxml"));
        Parent root = loader.load();
        stage.setTitle("Prueba GUILogin");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void LoginTest() {
        clickOn("#userNameTextField").write("angel");
        interact(() ->
                ((PasswordField) lookup("#userPasswordTextField").query()
        ).setText("angel54321"));
        verifyThat("#userNameTextField", hasText("angel"));
        verifyThat("#userPasswordTextField", hasText("angel54321"));
        clickOn("#loginButton");

        sleep(1500);
    }
}
