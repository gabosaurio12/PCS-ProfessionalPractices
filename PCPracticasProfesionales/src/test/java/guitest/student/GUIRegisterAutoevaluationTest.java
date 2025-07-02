package guitest.student;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;


import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIRegisterAutoevaluationTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/GUILogin.fxml"));
        Parent root = loader.load();
        stage.setTitle("GUIRegisterAutoevaluationTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void registerAutoevaluationTest() {
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

        clickOn("#autoevaluationButton");
        sleep(250);

        clickOn("#autoEvaluationGradeSpinner");
        for (int i = 0; i < 45; i++) {
            push(KeyCode.UP);
        }

        clickOn("#saveButton");
        sleep(500);
        push(KeyCode.ENTER);
    }
}
