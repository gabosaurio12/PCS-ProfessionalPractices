package gui.academic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class AcademicMainPage extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                "/fxml/Academic/GUIMainPageAcademic.fxml")));

        Scene scene = new Scene(root);

        stage.setTitle("Página Principal Académico");
        stage.setScene(scene);
        stage.show();
    }
}
