package gui.coordinator.student;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.login.LoginDAOImplementation;
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
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUIRegisterStudentController {
    private static final Logger logger = LogManager.getLogger(GUIRegisterStudentController.class);
    private final GUIUtils utils = new GUIUtils();
    private int sectionID;

    @FXML private Rectangle blueFringe;
    @FXML private TextField tuitionTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField firstSurnameTextField;
    @FXML private TextField secondSurnameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField creditAdvanceTextField;
    @FXML private ChoiceBox<Academic> academicBox;
    @FXML private Label tuitionLabel;
    @FXML private Label nameLabel;
    @FXML private Label firstSurnameLabel;
    @FXML private Label emailLabel;
    @FXML private Label creditAdvanceLabel;
    @FXML private Label academicLabel;

    public void initComponents(int sectionID) {
        this.sectionID = sectionID;
        setAcademicsBox();
    }

    public void openStudentsPage() {
        GUIStudentsController controller = utils.openWindow(
                "/fxml/Coordinator/Student/GUIStudents.fxml", "Estudiantes",
                GUIStudentsController.class, blueFringe);
        if (controller != null) {
            controller.initComponents();
        }
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void setAcademicsBox() {
        List<Academic> academics = new ArrayList<>();
        try {
            AcademicDAOImplementation academicDAO = new AcademicDAOImplementation();
            Section section = new SystemDAOImplementation().getSectionByID(sectionID);
            String role = "ACADÉMICO";
            academics = academicDAO.getAcademicsByRole(role, section.getPeriod());
        } catch (SQLException e) {
            utils.createAlert("Error al recopilar Académicos",
                    "Vuelva a intentar cargar la página");
            logger.error("Error al recopilar academicos", e);
        }

        for (Academic i : academics) {
            academicBox.getItems().add(i);
        }
    }

    public void generateEmail() {
        Student emailStudent = new Student();
        emailStudent.setTuition(tuitionTextField.getText());
        emailTextField.setText(emailStudent.generateEmail());
    }

    @FXML
    public void getTextFieldsData(Student student) {
        student.setTuition(tuitionTextField.getText());
        student.setName(nameTextField.getText());
        student.setFirstSurname(firstSurnameTextField.getText());
        student.setSecondSurname(secondSurnameTextField.getText());
        student.setCreditAdvance(Integer.parseInt(creditAdvanceTextField.getText()));
        student.setEmail(emailTextField.getText());
        Integer academicId = academicBox.getValue() == null ? null : academicBox.getValue().getId();
        student.setAcademicId(academicId);
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());
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

    public void validateUsername(Student student) throws SQLException {
        LoginDAOImplementation loginDAO = new LoginDAOImplementation();
        while (loginDAO.getUser(student.getUserName())) {
            char[] usernameLetters = student.getUserName().toCharArray();
            Random random = new Random();
            String username = "";
            for (int i = 0; i < usernameLetters.length; i++) {
                int randomLetter = random.nextInt(usernameLetters.length);
                username = username.concat(String.valueOf(usernameLetters[randomLetter]));
            }
            student.setUserName(username);
        }
    }

    public void registerStudent() {
        resetLabels();
        Student student = new Student();
        getTextFieldsData(student);
        boolean[] validationFlags = student.validateData();
        if (validationFlags[8]) {
            try {
                validateUsername(student);
                StudentDAOImplementation studentDAO = new StudentDAOImplementation();
                int studentID = studentDAO.registerStudent(student);
                studentDAO.assignSection(studentID, sectionID);
                utils.createAlert("Registro exitoso",
                        "¡El Estudiante se registró exitosamente! \n" +
                                "El nombre de usuario es: ".concat(student.getUserName()));
                openStudentsPage();
            } catch (SQLIntegrityConstraintViolationException e) {
                if (e.getMessage().contains("Duplicate entry")) {
                    utils.createAlert("Matrícula inválida",
                            "Ya hay un Estudiante registrado con esta matrícula," +
                                    "no se registró el Estudiante");
                } else {
                    utils.createAlert("Registro fallido",
                            "Violación de integridad, no se registró el Estudiante");
                    logger.error("Error, violación de integridad", e);
                }
            } catch (SQLException e) {
                utils.createAlert("Registro fallido",
                        "El registro del estudiante falló");
                logger.error("Error al registrar estudiante", e);
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
