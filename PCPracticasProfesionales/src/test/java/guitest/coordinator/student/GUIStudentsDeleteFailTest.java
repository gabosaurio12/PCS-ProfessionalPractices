package guitest.coordinator.student;

import gui.coordinator.student.GUIStudentsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;


public class GUIStudentsDeleteFailTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Student/GUIStudents.fxml"));
        Parent root = loader.load();
        GUIStudentsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUIStudentsDeleteTestFail");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void dropStudentTestFail() {
        sleep(500);
        clickOn("#deleteButton");
        sleep(1000);
        push(KeyCode.ENTER);
        sleep(300);
    }
}
