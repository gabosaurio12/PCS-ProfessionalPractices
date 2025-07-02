package gui.coordinator.student;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.login.LoginDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import gui.coordinator.project.GUIReassignProjectController;
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
    private String originalUsername;

    @FXML private Rectangle blueFringe;
    @FXML private TextField tuitionTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField firstSurnameTextField;
    @FXML private TextField secondSurnameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField creditAdvanceTextField;
    @FXML private TextField usernameTextField;
    @FXML private ChoiceBox<Academic> academicBox;
    @FXML TextField gradeTextField;
    @FXML private Label tuitionLabel;
    @FXML private Label nameLabel;
    @FXML private Label firstSurnameLabel;
    @FXML private Label emailLabel;
    @FXML private Label creditAdvanceLabel;
    @FXML private Label academicLabel;
    @FXML private Label usernameLabel;

    public void initComponents(Student student, int sectionID) {
        this.sectionID = sectionID;
        setAcademicsBox();
        setTextField(student);
    }

    public void closeWindow() {
       utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeWindow(blueFringe);
    }

    public void openStudentsPage() {
        GUIStudentsController controller = utils.openWindow(
                "/fxml/Coordinator/Student/GUIStudents.fxml", "Estudiantes",
                GUIStudentsController.class, blueFringe);
        if (controller != null) {
            controller.initComponents();
        }
    }

    @FXML
    public void setTextField(Student student) {
        oldTuition = student.getTuition();
        tuitionTextField.setText(student.getTuition());
        gradeTextField.setText(student.getTuition());
        nameTextField.setText(student.getName());
        firstSurnameTextField.setText(student.getFirstSurname());
        secondSurnameTextField.setText(student.getSecondSurname());
        emailTextField.setText(student.getEmail());
        creditAdvanceTextField.setText(String.valueOf(student.getCreditAdvance()));
        usernameTextField.setText(student.getUserName());
        originalUsername = student.getUserName();
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
        student.setTuition(tuitionTextField.getText());
        student.setName(nameTextField.getText());
        student.setFirstSurname(firstSurnameTextField.getText());
        student.setSecondSurname(secondSurnameTextField.getText());
        student.setCreditAdvance(Integer.parseInt(creditAdvanceTextField.getText()));
        student.setEmail(emailTextField.getText());
        student.setUserName(usernameTextField.getText());
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

    public boolean validateUsername(Student student) throws SQLException {
        LoginDAOImplementation loginDAO = new LoginDAOImplementation();
        boolean validUsername = true;
        if (!student.getUserName().equals(originalUsername)) {
            if (loginDAO.getUser(student.getUserName())) {
                utils.createAlert("Nombre de usuario inválido",
                        "El nombre de usuario ya está registrado, " +
                                "elija otro por favor");
                usernameLabel.setTextFill(utils.ERROR_COLOUR);
                validUsername = false;
            }
        }

        return validUsername;
    }

    public void saveChanges() {
        StudentDAOImplementation studentDAO = new StudentDAOImplementation();
        Student student = null;
        try {
            student = studentDAO.getStudentByTuition(oldTuition);
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "No se encontró al estudiante");
            logger.error("Error al buscar estudiante", e);
        }
        if (student != null) {
            resetLabels();
            getTextFieldsData(student);
            boolean[] validationFlags = student.validateData();
            if (validationFlags[validationFlags.length - 1] && !usernameTextField.getText().isBlank()) {
                try {
                    if (validateUsername(student)) {
                        studentDAO.updateStudentByTuition(student, oldTuition);
                        utils.createAlert("Éxito", "La actualización se realizó de forma exitosa");
                        openStudentsPage();
                    }
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

    public void openProjectsAssignation() {
        Student student = new Student();
        try {
            student = new StudentDAOImplementation().
                    getStudentByTuition(oldTuition);
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "No se encontró al estudiante");
            logger.error("Error al buscar estudiante openProjectsAsignation", e);
        }

        GUIReassignProjectController controller = utils.openWindow(
                "/fxml/Coordinator/Project/GUIAvailableReassignProjects.fxml", "Asignar proyecto",
                GUIReassignProjectController.class, blueFringe);
        controller.createTable();
        controller.setStudent(student);
    }
}
