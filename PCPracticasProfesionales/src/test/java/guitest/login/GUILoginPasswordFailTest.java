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

public class GUILoginPasswordFailTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GUILogin.fxml"));
        Parent root = loader.load();
        stage.setTitle("Prueba GUILogin");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void LoginTestFail() {
        clickOn("#userNameTextField").write("elrevo");
        interact(() ->
                ((PasswordField) lookup("#userPasswordTextField").query()
                ).setText("revo00000"));
        verifyThat("#userNameTextField", hasText("elrevo"));
        verifyThat("#userPasswordTextField", hasText("revo00000"));
        clickOn("#loginButton");

        sleep(2000);
    }
}
