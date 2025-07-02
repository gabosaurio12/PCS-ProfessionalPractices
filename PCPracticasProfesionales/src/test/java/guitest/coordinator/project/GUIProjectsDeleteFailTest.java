package guitest.coordinator.project;

import gui.coordinator.project.GUIProjectsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;


public class GUIProjectsDeleteFailTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Project/GUIProjects.fxml"));
        Parent root = loader.load();
        GUIProjectsController controller = loader.getController();
        controller.init();
        stage.setTitle("GUIProjectsDeleteTestFail");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void deleteTestFail() {
        clickOn("#deleteButton");
        sleep(500);
        push(KeyCode.ENTER);
        sleep(500);
    }
}
