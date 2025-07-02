package guitest.coordinator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;


public class GUICronogramTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/GUIMainPageCoordinator.fxml"));
        Parent root = loader.load();
        stage.setTitle("GUICronogramTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void presentationStatisticsTestFail() {
        clickOn("#cronogramButton");
        sleep(2000);
        clickOn("#closeButton");
    }
}
