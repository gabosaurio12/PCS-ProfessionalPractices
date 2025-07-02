package gui.student;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import dataaccess.system.UserDataConfig;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.Report;
import model.Section;
import model.Student;
import model.StudentProgress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class GUIUploadReportController {
    private static final Logger logger = LogManager.getLogger(GUIUploadReportController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML private Rectangle blueFringe;
    @FXML private Label dateLabel;
    @FXML private Label typeLabel;
    @FXML private Label hoursLabel;
    @FXML private Label filePathLabel;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Spinner<Integer> hoursSpinner;
    @FXML private TextField filePathTextField;
    @FXML private Button fileButton;

    public void init() {
        int remainingHours = validateStatus();
        if (remainingHours > 0) {
            setFileButton();
            reportTypeComboBox();
            setHoursSpinner();
        } else if (remainingHours == 0) {
            utils.createAlert("ÉXITO",
                    "¡FELICIDADES! HAS CONCLUIDO TUS PRÁCTICAS, YA NO DEBES SUBIR REPORTES");
            closeWindow();
        }

    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void setFileButton() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/fileicon.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        fileButton.setGraphic(imageView);
    }

    public void setHoursSpinner() {
        hoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 480, 0));
    }

    public void searchProjectFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(fileButton.getScene().getWindow());
        if (selectedFile != null) {
            filePathTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    public void reportTypeComboBox() {
        List<String> types = new ArrayList<>();
        try {
            types = new StudentDAOImplementation().getReportsType();
        } catch (SQLException e) {
            logger.error("Error al recuperar tipos de reportes");
            utils.createAlert("Error",
                    "Hubo un error al recuperar los tipos de reporte");
            closeWindow();
        }
        if (!types.isEmpty()) {
            for (String type : types) {
                typeComboBox.getItems().add(type);
            }
        } else {
            utils.createAlert("Error",
                    "No se han subido los tipos de reporte, contácte al servicio técnico");
            closeWindow();
        }
    }

    public Report getInfo() {
        Report report = new Report();
        report.setReportPath(filePathTextField.getText());
        report.setHours(hoursSpinner.getValue());
        report.setType(typeComboBox.getValue());
        report.setDate(Date.valueOf(datePicker.getValue()));
        Section section = new Section();
        try {
            section = new SystemDAOImplementation().getCurrentSection();
        } catch (SQLException e) {
            logger.error("Error al recuperar sección actual", e);
            utils.createAlert("Error",
                    "Hubo un error al recuperar la sección actual");
        }
        if (!section.isNull())
            report.setPeriod(section.getPeriod());
        Properties userProperties = UserDataConfig.loadProperties();
        report.setStudentId(Integer.valueOf(userProperties.getProperty("user.id")));
        return report;
    }

    public int validateStatus() {
        StudentDAOImplementation dao = new StudentDAOImplementation();
        Properties userProperties = UserDataConfig.loadProperties();
        int remainingHours = 480;
        try {
            Student student = dao.getStudentByID(Integer.parseInt(userProperties.getProperty("user.id")));
            StudentProgress progress = dao.getStudentProgressByTuition(student.getTuition());
            remainingHours = progress.getRemainingHours();
        } catch (SQLException e) {
            logger.error("Error al recuperar avance de estudiante", e);
        }

        return remainingHours;
    }

    public void showInvalidData(boolean[] flags) {
        String colour = "#dd0000";
        if (!flags[0]) {
            filePathLabel.setTextFill(Paint.valueOf(colour));
        }
        if (!flags[1]) {
            hoursLabel.setTextFill(Paint.valueOf(colour));
        }
        if (!flags[2]) {
            typeLabel.setTextFill(Paint.valueOf(colour));
        }
        if (!flags[3]) {
            dateLabel.setTextFill(Paint.valueOf(colour));
        }
    }

    public void resetLabels() {
        String colour = "#000000";
        filePathLabel.setTextFill(Paint.valueOf(colour));
        hoursLabel.setTextFill(Paint.valueOf(colour));
        typeLabel.setTextFill(Paint.valueOf(colour));
        dateLabel.setTextFill(Paint.valueOf(colour));
    }

    public void uploadReport() {
        resetLabels();
        Report report = getInfo();
        boolean[] validationFlags = report.validateData();
        if (validationFlags[validationFlags.length - 1]) {
            try {
                new StudentDAOImplementation().registerReport(report);
                utils.createAlert("Éxito",
                        "¡Se reigstró el reporte con éxito!");
            } catch (SQLException e) {
                logger.error("Error al subir reporte", e);
                utils.createAlert("Error",
                        "Hubo un error y no se pudo subir el reporte");
            } finally {
                closeWindow();
            }
        } else {
            utils.createAlert("Campos inválidos",
                    "Hay campos que no se han llenado, por favor llénelos");
            showInvalidData(validationFlags);
        }
    }
}
