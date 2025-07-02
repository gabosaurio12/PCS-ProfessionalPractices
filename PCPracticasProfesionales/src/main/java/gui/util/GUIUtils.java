package gui.util;

import gui.login.LoginPage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIUtils {
    private static final Logger logger = LogManager.getLogger(GUIUtils.class);

    public final Paint DEFAULT_COLOUR = Paint.valueOf("#000000");
    public final Paint ERROR_COLOUR = Paint.valueOf("#dd0000");

    public void createAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public <T> T openWindow(String fxmlPath, String title, Class<T> controllerClass, Rectangle  blueFringe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            closeWindow(blueFringe);

            T loaderController;
            loaderController = loader.getController();
            if (controllerClass.isInstance(loaderController)) {
                return controllerClass.cast(loaderController);
            } else {
                logger.error("El controlador no es del tipo esperdado: {}",
                        controllerClass.getSimpleName());
                return null;
            }
        } catch (Exception e) {
            createAlert("Error al abrir" + title,
                    "Volverá a la ventana anterior");
            logger.error("Error al abrir {}", title, e);
            return null;
        }
    }

    public void openWindow(String fxmlPath, String title, Rectangle blueFringe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            closeWindow(blueFringe);
        } catch (Exception e) {
            createAlert("Error al abrir" + title,
                    "Volverá a la ventana anterior");
            logger.error("Error al abrir {}", title, e);
        }
    }

    public void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            createAlert("Error al abrir" + title,
                    "Volverá a la ventana anterior");
            logger.error("Error al abrir {}", title, e);
        }
    }

    public <T> T openWindow(String fxmlPath, String title, Class<T> controllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            T loaderController;
            loaderController = loader.getController();
            if (controllerClass.isInstance(loaderController)) {
                return controllerClass.cast(loaderController);
            } else {
                logger.error("El controlador no es del tipo esperdado {}",
                        controllerClass.getSimpleName());
                return null;
            }
        } catch (Exception e) {
            createAlert("Error al abrir" + title,
                    "Volverá a la ventana anterior");
            logger.error("Error al abrir {}", title, e);
            return null;
        }
    }

    public void closeWindow(Rectangle blueFringe) {
        Stage currentStage = (Stage) blueFringe.getScene().getWindow();
        currentStage.close();
    }

    public void closeSession(Rectangle blueFringe) {
        try {
            new LoginPage().start(new Stage());
        } catch (Exception e) {
            createAlert("Error al abrir inicio de sesión",
                    "Se cerrará la aplicación, una disculpa");
            logger.error("Error al abrir login", e);
        }
        closeWindow(blueFringe);
    }
}
