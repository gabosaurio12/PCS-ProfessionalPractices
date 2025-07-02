package gui.coordinator.academics;

import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.login.LoginDAOImplementation;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.Academic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUIAcademicDetailsController {
    private static final Logger logger = LogManager.getLogger(GUIAcademicDetailsController.class);
    private final GUIUtils utils = new GUIUtils();
    private String oldPersonalNumber;
    private int academicID;
    private String originalUsername;

    @FXML private Rectangle blueFringe;
    @FXML private TextField personalNumberTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField firstSurnameTextField;
    @FXML private TextField secondSurnameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField usernameTextField;
    @FXML private ComboBox<String> academicPositionBox;
    @FXML private Label personalNumberLabel;
    @FXML private Label nameLabel;
    @FXML private Label firstSurnameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private Label usernameLabel;

    public void initComponents(Academic academic) {
        setAcademicPositionBox();
        setTextField(academic);
        academicID = academic.getId();
        oldPersonalNumber = academic.getPersonalNumber();
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }

    public void openAcademicsPage() {
        GUIAcademicsController controller = utils.openWindow(
                "/fxml/Coordinator/Academic/GUIAcademics.fxml", "Academicos",
                GUIAcademicsController.class, blueFringe);
        if (controller != null) {
            controller.initComponents();
        }
    }

    @FXML
    public void setTextField(Academic academic) {
        personalNumberTextField.setText(academic.getPersonalNumber());
        nameTextField.setText(academic.getName());
        firstSurnameTextField.setText(academic.getFirstSurname());
        secondSurnameTextField.setText(academic.getSecondSurname());
        emailTextField.setText(academic.getEmail());
        usernameTextField.setText(academic.getUserName());
        originalUsername = academic.getUserName();
        academicPositionBox.setValue(academic.getRole());
    }

    @FXML
    public void getTextFieldsData(Academic academic) {
        academic.setPersonalNumber(personalNumberTextField.getText());
        academic.setName(nameTextField.getText());
        academic.setFirstSurname(firstSurnameTextField.getText());
        academic.setSecondSurname(secondSurnameTextField.getText());
        academic.setEmail(emailTextField.getText());
        academic.setUserName(usernameTextField.getText());
        academic.setRole(academicPositionBox.getValue());
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
            academicPositionBox.setValue(roles.getFirst());
        }
    }

    public void showInvalidData(boolean[] flags) {
        if (!flags[0]) {
            utils.createAlert("Número personal inválido",
                    "El número personal debe ser de 5 digitos o menos");
            personalNumberLabel.setTextFill(Paint.valueOf("#dd0000"));
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
            roleLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (usernameTextField.getText().isBlank()) {
            usernameLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
    }

    public void resetLabels() {
        personalNumberLabel.setTextFill(Paint.valueOf("#000000"));
        nameLabel.setTextFill(Paint.valueOf("#000000"));
        firstSurnameLabel.setTextFill(Paint.valueOf("#000000"));
        emailLabel.setTextFill(Paint.valueOf("#000000"));
        roleLabel.setTextFill(Paint.valueOf("#000000"));
        usernameLabel.setTextFill(Paint.valueOf("#000000"));
    }

    public boolean validateUsername(Academic academic) throws SQLException {
        LoginDAOImplementation loginDAO = new LoginDAOImplementation();
        boolean validUsername = true;
        if (!academic.getUserName().equals(originalUsername)) {
            if (loginDAO.getUser(academic.getUserName())) {
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
        AcademicDAOImplementation academicDAO = new AcademicDAOImplementation();
        Academic academic = new Academic();
        try {
            academic = academicDAO.getAcademicByPersonalNumber(oldPersonalNumber);
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "No se encontró el académico");
            logger.error("Error al buscar académico", e);
            openAcademicsPage();
        }
        if (!academic.isNull()) {
            resetLabels();
            getTextFieldsData(academic);
            boolean[] validationFlags = academic.validateData();
            if (validationFlags[validationFlags.length - 1] && !usernameTextField.getText().isBlank()) {
                try {
                    if (validateUsername(academic)) {
                        academicDAO.updateAcademicByID(academic);
                        utils.createAlert("Actualización exitosa",
                                "¡El académico fue actualizado con éxito!"
                        );
                        openAcademicsPage();
                    }
                } catch (SQLException e) {
                    utils.createAlert("Actualización fallida",
                            "El académico no pudo ser actualizado");
                    logger.error("Error al actualizar académico", e);
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
