package gui.student;

import businesslogic.project.ProjectDAOImplementation;
import dataaccess.system.UserDataConfig;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.ProjectRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class GUIProjectRequestController {
    private static final Logger logger = LogManager.getLogger(GUIProjectRequestController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML private Rectangle blueFringe;
    @FXML private Button fileButton;
    @FXML private TextField titleTextField;
    @FXML private TextField filePathTextField;
    @FXML private Label titleLabel;
    @FXML private Label filePathLabel;

    public void init() {
        setFileButton();
    }

    public void setFileButton() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/fileicon.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        fileButton.setGraphic(imageView);
    }

    public void searchProjectFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(fileButton.getScene().getWindow());
        if (selectedFile != null) {
            filePathTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    public ProjectRequest getTextFields() {
        ProjectRequest request = new ProjectRequest();
        request.setName(titleTextField.getText());
        request.setDocumentPath(filePathTextField.getText());
        Properties userProperties = UserDataConfig.loadProperties();
        request.setStudentId(Integer.valueOf(userProperties.getProperty("user.id")));
        return request;
    }

    public void showInvalidData(boolean[] flags) {
        int TITLE_FLAG = 0;
        if (!flags[TITLE_FLAG]) {
            titleLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int FILE_PATH_FLAG = 1;
        if (!flags[FILE_PATH_FLAG]) {
            filePathLabel.setTextFill(utils.ERROR_COLOUR);
        }
    }

    public void resetLabels() {
        titleLabel.setTextFill(utils.DEFAULT_COLOUR);
        filePathLabel.setTextFill(utils.DEFAULT_COLOUR);
    }

    public void registerProjectRequest() {
        resetLabels();
        ProjectRequest request = getTextFields();
        boolean[] validationFlags = request.validateData();
        if (validationFlags[validationFlags.length - 1]) {
            try {
                ProjectDAOImplementation dao = new ProjectDAOImplementation();
                dao.registerProjectRequest(request);
                utils.createAlert("Éxito",
                        "¡Se registró la solicitud de proyecto con éxito!");
            } catch (SQLException e) {
                utils.createAlert("Error",
                        "Hubo un error al registrar solicitud de proyecto");
                logger.error("Error al registrar solicitud de proyecto");
            }
        } else {
            utils.createAlert("Campos inválidos",
                    "Hay campos ingresados vacíos, por favor llénelos");
            showInvalidData(validationFlags);
        }
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }
}
