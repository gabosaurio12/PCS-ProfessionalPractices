package gui.coordinator.presentations;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.presentation.PresentationDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class GUIEditPresentationController {
    private static final Logger logger = LogManager.getLogger(GUIEditPresentationController.class);
    private final GUIUtils utils = new GUIUtils();
    private int presentationId;

    @FXML
    private Rectangle blueFringe;
    @FXML private Label studentLabel;
    @FXML private Label dateLabel;
    @FXML private Label gradeLabel;
    @FXML private DatePicker datePicker;
    @FXML private TextField presentationTextField;
    @FXML private TextField gradeTextField;
    @FXML private ComboBox<Student> studentComboBox;
    @FXML private Button fileButton;

    public void init(Presentation presentation) {
        presentationId = presentation.getPresentationID();
        setStudentComboBox();
        setInfo(presentation);
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

    public void setInfo(Presentation presentation) {
        presentationTextField.setText(presentation.getPresentationPath());
        gradeTextField.setText(String.valueOf(presentation.getPresentationGrade()));
        datePicker.setValue(presentation.getDate().toLocalDate());
        Student student = new Student();
        try {
            student = new StudentDAOImplementation().getStudentByID(
                    presentation.getStudentId());
        } catch (SQLException e) {
            logger.error("Error al recuperar estudiante", e);
            utils.createAlert("Error",
                    "Hubo un error al recuperar al Estudiante");
        }
        studentComboBox.setValue(student);
    }

    public Presentation getInfo() {
        Presentation presentation = new Presentation();
        presentation.setDate(Date.valueOf(datePicker.getValue()));
        presentation.setPresentationPath(presentationTextField.getText());
        presentation.setPresentationGrade(Double.valueOf(gradeTextField.getText()));
        presentation.setStudentId(studentComboBox.getSelectionModel().getSelectedItem().getId());
        presentation.setPresentationID(presentationId);
        return presentation;
    }

    public void showInvalidData(boolean[] flags) {
        int DATE_FLAG = 0;
        if (!flags[DATE_FLAG]) {
            dateLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int STUDENT_FLAG = 1;
        if (!flags[STUDENT_FLAG]) {
            studentLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int GRADE_FLAG = 2;
        if (!flags[GRADE_FLAG]) {
            gradeLabel.setTextFill(utils.ERROR_COLOUR);
        }
    }

    public void resetLabels() {
        dateLabel.setTextFill(utils.DEFAULT_COLOUR);
        studentLabel.setTextFill(utils.DEFAULT_COLOUR);
        gradeLabel.setTextFill(utils.DEFAULT_COLOUR);
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

    public void savePresentation() {
        resetLabels();
        Presentation presentation = getInfo();
        boolean[] validationFlags = presentation.validateData();
        if (validationFlags[validationFlags.length - 1]) {
            try {
                PresentationDAOImplementation dao = new PresentationDAOImplementation();
                dao.updatePresentation(presentation);
                utils.createAlert("Éxito",
                        "La presentación fue actualizada con éxito");
            } catch (SQLException e) {
                logger.error("Error al editar presentación", e);
                utils.createAlert("Error",
                        "Hubo un error al editar la presentación");
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
    }}
