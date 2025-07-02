package gui.coordinator.academics;

import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.login.LoginDAOImplementation;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import model.Academic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUIRegisterAcademicController {
    private static final Logger logger = LogManager.getLogger(GUIRegisterAcademicController.class);
    private static final GUIUtils utils = new GUIUtils();
    private int sectionID;

    @FXML private Rectangle blueFringe;
    @FXML private TextField personalNumberTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField firstSurnameTextField;
    @FXML private TextField secondSurnameTextField;
    @FXML private TextField emailTextField;
    @FXML private ComboBox<String> academicPositionBox;
    @FXML private Label personalNumberLabel;
    @FXML private Label nameLabel;
    @FXML private Label firstSurnameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;

    public void initComponents(int sectionID) {
        this.sectionID = sectionID;
        setAcademicPositionBox();
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }

    public void setAcademicPositionBox() {
        List<String> roles = new ArrayList<>();
        try {
            roles = new AcademicDAOImplementation().getRoles();
        } catch (SQLException e) {
            logger.error("Error al obtener roles ", e);
            utils.createAlert("Error",
                    "Error al obtener roles");
            openAcademicsPage();
        }
        if (roles.isEmpty()) {
            utils.createAlert("Error",
                    "No hay roles registrados");
            openAcademicsPage();
        } else {
            academicPositionBox.getItems().addAll(roles);
        }
    }

    public void openAcademicsPage() {
        GUIAcademicsController controller = utils.openWindow(
                "/fxml/Coordinator/Academic/GUIAcademics.fxml", "Academicos",
                GUIAcademicsController.class, blueFringe);
        controller.initComponents();
    }

    @FXML
    public void getTextFieldsData(Academic academic) {
        academic.setPersonalNumber(personalNumberTextField.getText());
        academic.setName(nameTextField.getText());
        academic.setFirstSurname(firstSurnameTextField.getText());
        academic.setSecondSurname(secondSurnameTextField.getText());
        academic.setEmail(emailTextField.getText());
        String role = academicPositionBox.getValue() != null ? academicPositionBox.getValue() : "";
        academic.setRole(role);
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
    }

    public void showInvalidData(boolean[] flags) {
        int PERSONAL_NUMBER_FLAG = 0;
        if (!flags[PERSONAL_NUMBER_FLAG]) {
            utils.createAlert("Número personal inválido",
                    "El número personal debe ser de 5 digitos o menos");
            personalNumberLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int NAME_FLAG = 1;
        if (!flags[NAME_FLAG]) {
            nameLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int FIRST_SURNAME_FLAG = 2;
        if (!flags[FIRST_SURNAME_FLAG]) {
            firstSurnameLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int EMAIL_FLAG = 3;
        if (!flags[EMAIL_FLAG]) {
            emailLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int ROLE_FLAG = 4;
        if (!flags[ROLE_FLAG]) {
            roleLabel.setTextFill(utils.ERROR_COLOUR);
        }
    }

    public void resetLabels() {
        personalNumberLabel.setTextFill(utils.DEFAULT_COLOUR);
        nameLabel.setTextFill(utils.DEFAULT_COLOUR);
        firstSurnameLabel.setTextFill(utils.DEFAULT_COLOUR);
        emailLabel.setTextFill(utils.DEFAULT_COLOUR);
        roleLabel.setTextFill(utils.DEFAULT_COLOUR);
    }

    public void validateUsername(Academic academic) throws SQLException {
        LoginDAOImplementation loginDAO = new LoginDAOImplementation();
        while (loginDAO.getUser(academic.getUserName())) {
            char[] usernameLetters = academic.getUserName().toCharArray();
            Random random = new Random();
            String username = "";
            for (int i = 0; i < usernameLetters.length; i++) {
                int randomLetter = random.nextInt(usernameLetters.length);
                username = username.concat(String.valueOf(usernameLetters[randomLetter]));
            }
            academic.setUserName(username);
        }
    }

    public void registerAcademic() {
        resetLabels();
        Academic academic = new Academic();
        getTextFieldsData(academic);
        boolean[] validationFlags = academic.validateData();
        if (validationFlags[validationFlags.length - 1]) {
            try {
                validateUsername(academic);
                AcademicDAOImplementation academicDAO = new AcademicDAOImplementation();
                int academicID = academicDAO.registerAcademic(academic);
                academicDAO.assignSection(academicID, sectionID);
                utils.createAlert("Registro exitoso",
                        "¡Se registró el evaluador con éxito! \n" +
                                "Con el Username: ".concat(academic.getUserName()));
                openAcademicsPage();
            } catch (SQLIntegrityConstraintViolationException e) {
                if (e.getMessage().contains("Duplicate entry")) {
                    utils.createAlert("Número de personal inválido",
                            "Ya hay un Académico registrado con esta matrícula," +
                                    "no se registró el Académico");
                } else {
                    utils.createAlert("Error",
                            "Violación de integridad, no se registró al Académico");
                    logger.error("Error, violación de integridad", e);
                }
            } catch (SQLException e) {
                utils.createAlert("Error",
                        "El registro del Académico falló");
                logger.error("Error al registrar Académico", e);
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
