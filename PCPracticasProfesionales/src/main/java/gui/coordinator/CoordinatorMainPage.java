package gui.coordinator;

import gui.util.GUIUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class CoordinatorMainPage extends Application {
    private final GUIUtils utils = new GUIUtils();

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                "/fxml/Coordinator/GUIMainPageCoordinator.fxml")));

        Scene scene = new Scene(root);

        stage.setTitle("Menú Principal Coordinador");
        stage.setScene(scene);
        stage.show();
    }
}