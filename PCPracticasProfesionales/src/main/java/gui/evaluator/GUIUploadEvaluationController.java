package gui.evaluator;

import businesslogic.academic.AcademicDAOImplementation;
import dataaccess.system.UserDataConfig;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.Evaluation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class GUIUploadEvaluationController {

    private static final Logger logger = LogManager.getLogger(GUIUploadEvaluationController.class);
    private final GUIUtils utils = new GUIUtils();
    private int presentationId;

    @FXML private Rectangle blueFringe;
    @FXML private Spinner<Double> gradeSpinner;
    @FXML private Button fileButton;
    @FXML private TextField filePathTextField;
    @FXML private Label filePathLabel;
    @FXML private Label gradeLabel;

    public void init(int presentationId) {
        this.presentationId = presentationId;
        setFileButton();
        setGradeSpinner();
    }

    public void setFileButton() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/fileicon.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        fileButton.setGraphic(imageView);
    }

    public void setGradeSpinner() {
        gradeSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100));
    }

    public void searchProjectFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(fileButton.getScene().getWindow());
        if (selectedFile != null) {
            filePathTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    public Evaluation getTextFields() {
        Evaluation evaluation = new Evaluation();
        evaluation.setAverageGrade(gradeSpinner.getValue());
        evaluation.setEvaluationPath(filePathTextField.getText());
        evaluation.setPresentationId(presentationId);
        Properties userProperties = UserDataConfig.loadProperties();
        evaluation.setAcademicId(Integer.valueOf(userProperties.getProperty("user.id")));

        return evaluation;
    }

    public boolean validateData(Evaluation evaluation) {
        boolean validationFlag = true;
        if (evaluation.getEvaluationPath().isBlank()) {
            filePathLabel.setTextFill(Paint.valueOf("#dd0000"));
            utils.createAlert("Campos inválidos",
                    "Debe subir el archivo de evaluación");
            validationFlag = false;
        }
        if (evaluation.getAverageGrade() < 1 || evaluation.getAverageGrade() > 100) {
            gradeLabel.setTextFill(Paint.valueOf("#dd0000"));
            utils.createAlert("Campos inválidos",
                    "La calificación debe estar en el rango [1-100]");
            validationFlag = false;
        }
        return validationFlag;
    }

    public void uploadEvaluation() {
        Evaluation evaluation = getTextFields();
        if (validateData(evaluation)) {
            try {
                AcademicDAOImplementation dao = new AcademicDAOImplementation();
                dao.addEvaluation(evaluation);
                utils.createAlert("Éxito",
                        "Se subió la evaluación con éxito");
                closeWindow();
            } catch (SQLException e) {
                utils.createAlert("Error",
                        "Hubo un error al subir la evaluación");
                logger.error("Error al subir la evaluación", e);
                closeWindow();
            }
        }
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }
}
