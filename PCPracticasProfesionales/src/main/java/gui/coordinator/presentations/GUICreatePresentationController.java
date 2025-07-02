package gui.coordinator.presentations;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.presentation.PresentationDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.Presentation;
import model.Section;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GUICreatePresentationController {
    private static final Logger logger = LogManager.getLogger(GUICreatePresentationController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML private Rectangle blueFringe;
    @FXML private Label studentLabel;
    @FXML private Label dateLabel;
    @FXML private DatePicker datePicker;
    @FXML private TextField presentationTextField;
    @FXML private ComboBox<Student> studentComboBox;
    @FXML private Button fileButton;

    public void init() {
        setStudentComboBox();
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
            presentationTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    public void setStudentComboBox() {
        List<Student> students = new ArrayList<>();
        try {
            StudentDAOImplementation studentDAO = new StudentDAOImplementation();
            Section section = new SystemDAOImplementation().getCurrentSection();
            students = studentDAO.getStudents(section.getPeriod());
        } catch (SQLException e) {
            utils.createAlert("Error al recopilar estudiantes",
                    "Vuelva a intentar cargar la página");
            logger.error("Error al recopilar estudiantes", e);
        }

        for (Student i : students) {
            studentComboBox.getItems().add(i);
        }
    }

    public Presentation getInfo() {
        Presentation presentation = new Presentation();
        presentation.setDate(Date.valueOf(datePicker.getValue()));
        presentation.setPresentationPath(presentationTextField.getText());
        presentation.setStudentId(studentComboBox.getSelectionModel().getSelectedItem().getId());
        return presentation;
    }

    public void showInvalidData(boolean[] flags) {
        String colour = "#dd0000";
        if (!flags[0]) {
            dateLabel.setTextFill(Paint.valueOf(colour));
        }
        if (!flags[1]) {
            studentLabel.setTextFill(Paint.valueOf(colour));
        }
    }

    public void resetLabels() {
        String colour = "#000000";
        dateLabel.setTextFill(Paint.valueOf(colour));
        studentLabel.setTextFill(Paint.valueOf(colour));
    }

    public void createPresentation() {
        resetLabels();
        Presentation presentation = getInfo();
        boolean[] validationFlags = presentation.validateData();
        if (validationFlags[validationFlags.length - 1]) {
            try {
                PresentationDAOImplementation dao = new PresentationDAOImplementation();
                dao.createPresentation(presentation);
                utils.createAlert("Éxito",
                        "La presentación fue creada con éxito");
            } catch (SQLException e) {
                logger.error("Error al crear presentación", e);
                utils.createAlert("Error",
                        "Hubo un error al crear la presentación");
            } finally {
                closeWindow();
            }
        } else {
            utils.createAlert("Campos sin llenar",
                    "Hay campos sin llenar, favor de llenarlos");
            showInvalidData(validationFlags);
        }
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }
}
