package gui.academic.student;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.Academic;
import model.Section;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUIStudentDetailsController {
    private static final Logger logger = LogManager.getLogger(GUIStudentDetailsController.class);
    private final GUIUtils utils = new GUIUtils();
    private int sectionID;
    private String oldTuition;

    @FXML
    private Rectangle blueFringe;
    @FXML private TextField nameTextField;
    @FXML private TextField firstSurnameTextField;
    @FXML private TextField secondSurnameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField userNameTextField;
    @FXML private ChoiceBox<Academic> academicBox;
    @FXML TextField gradeTextField;
    @FXML private Label tuitionLabel;
    @FXML private Label nameLabel;
    @FXML private Label firstSurnameLabel;
    @FXML private Label emailLabel;
    @FXML private Label creditAdvanceLabel;
    @FXML private Label academicLabel;

    public void initComponents(Student student, int sectionID) {
        this.sectionID = sectionID;
        setAcademicsBox();
        fillTextField(student);
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeWindow(blueFringe);
    }

    public void openStudentsPage() {
        GUIStudentsController controller = utils.openWindow(
                "/fxml/Academic/Student/GUIStudents.fxml", "Estudiantes",
                GUIStudentsController.class, blueFringe);
        if (controller != null) {
            controller.initComponents();
        }
    }

    @FXML
    public void fillTextField(Student student) {
        oldTuition = student.getTuition();
        tuitionLabel.setText(student.getTuition());
        gradeTextField.setText(student.getTuition());
        nameTextField.setText(student.getName());
        firstSurnameTextField.setText(student.getFirstSurname());
        secondSurnameTextField.setText(student.getSecondSurname());
        emailTextField.setText(student.getEmail());
        creditAdvanceLabel.setText(String.valueOf(student.getCreditAdvance()));
        userNameTextField.setText(student.getUserName());
        gradeTextField.setText(String.valueOf(student.getGrade()));
        try {
            academicBox.setValue(new AcademicDAOImplementation().getAcademicByID(student.getAcademicId()));
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Error al buscar académico");
            logger.error("Error al buscar academico por ID: ", e);
        }
    }

    @FXML
    public void getTextFieldsData(Student student) {
        student.setTuition(tuitionLabel.getText());
        student.setName(nameTextField.getText());
        student.setFirstSurname(firstSurnameTextField.getText());
        student.setSecondSurname(secondSurnameTextField.getText());
        student.setCreditAdvance(Integer.parseInt(creditAdvanceLabel.getText()));
        student.setEmail(emailTextField.getText());
        student.setUserName(userNameTextField.getText());
        student.setAcademicId(academicBox.getValue().getId());
        student.setGrade(Float.parseFloat(gradeTextField.getText()));
    }

    public void setAcademicsBox() {
        List<Academic> academics = new ArrayList<>();
        try {
            AcademicDAOImplementation academicDAO = new AcademicDAOImplementation();
            Section section = new SystemDAOImplementation().getSectionByID(sectionID);
            String role = "ACADÉMICO";
            academics = academicDAO.getAcademicsByRole(role, section.getPeriod());
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Error al recopilar académicos");
            logger.error("Error al recopilar academicos", e);
        }

        for (Academic i : academics) {
            academicBox.getItems().add(i);
        }
    }

    public void showInvalidData(boolean[] flags) {
        if (!flags[0]) {
            tuitionLabel.setTextFill(Paint.valueOf("#dd0000"));
            utils.createAlert("La matrícula es inválida",
                    "El tamaño de la matrícula debe ser de 9 caracteres");
        }
        if (!flags[1]) {
            nameLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!flags[2]) {
            firstSurnameLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!flags[3]) {
            emailLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!flags[4]) {
            creditAdvanceLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!flags[5]) {
            academicLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
    }

    public void resetLabels() {
        tuitionLabel.setTextFill(Paint.valueOf("#000000"));
        nameLabel.setTextFill(Paint.valueOf("#000000"));
        firstSurnameLabel.setTextFill(Paint.valueOf("#000000"));
        emailLabel.setTextFill(Paint.valueOf("#000000"));
        creditAdvanceLabel.setTextFill(Paint.valueOf("#000000"));
        academicLabel.setTextFill(Paint.valueOf("#000000"));
    }

    public void saveChanges() {
        StudentDAOImplementation studentDAO = new StudentDAOImplementation();
        Student student = new Student();
        try {
            student = studentDAO.getStudentByTuition(oldTuition);
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "No se encontró al estudiante");
            logger.error("Error al buscar estudiante", e);
        }
        if (!student.isNull()) {
            resetLabels();
            getTextFieldsData(student);
            boolean[] validationFlags = student.validateData();
            if (validationFlags[8] && !userNameTextField.getText().isBlank()) {
                try {
                    studentDAO.updateStudentByTuition(student, oldTuition);
                    utils.createAlert("Éxito", "La actualización se realizó de forma exitosa");
                    openStudentsPage();
                } catch (SQLException e) {
                    utils.createAlert("Error",
                            "No se pudo actualizar el estudiante");
                    logger.error("Error al actualizar estudiante", e);
                    if (e.getMessage().contains("Unknown database")) {
                        utils.createAlert("Error",
                                "No se encontró la base de datos");
                    }
                }
            } else {
                utils.createAlert("Datos inválidos",
                        "Los datos ingresados son inválidos");
                showInvalidData(validationFlags);
            }
        }
    }
}
