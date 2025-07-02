package gui;

import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.login.LoginDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import dataaccess.system.UserDataConfig;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import model.UniversityAffiliate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Properties;

public class GUIEditUserController {
    private static final Logger logger = LogManager.getLogger(GUIEditUserController.class);
    private final GUIUtils utils = new GUIUtils();
    private UniversityAffiliate affiliate;

    @FXML private Rectangle blueFringe;
    @FXML private Label userLabel;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private TextField userNameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField emailTextField;

    public void init() {
        affiliate = new UniversityAffiliate();
        Properties userProperties = UserDataConfig.loadProperties();
        affiliate.setId(Integer.valueOf(userProperties.getProperty("user.id")));
        affiliate.setName(userProperties.getProperty("user.name"));
        affiliate.setEmail(userProperties.getProperty("user.email"));
        affiliate.setUserName(userProperties.getProperty("user.username"));
        affiliate.setPassword(userProperties.getProperty("user.password"));

        setLabel();
        setTextFields();
    }

    public void setLabel() {
        userLabel.setText(affiliate.getName());
    }

    public void setTextFields() {
        userNameTextField.setText(affiliate.getUserName());
        emailTextField.setText(affiliate.getEmail());
    }

    public void getTextFields() {
        affiliate.setUserName(userNameTextField.getText());
        affiliate.setEmail(emailTextField.getText());
        String password = passwordTextField.getText();
        if (!password.isBlank()) {
            String hashedPassword = "";
            try {
                hashedPassword = new LoginDAOImplementation().getHashedPassword(password);
            } catch (SQLException e) {
                utils.createAlert("Error",
                        "Hubo un error al codificar la contraseña, no se realizarán cambios");
                closeWindow();
            }
            affiliate.setPassword(hashedPassword);
        }
    }

    public void showInvalidData(boolean[] flags) {
        int EMAIL_FLAG = 0;
        if (!flags[EMAIL_FLAG]){
            emailLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int USERNAME_FLAG = 1;
        if (!flags[USERNAME_FLAG]) {
            usernameLabel.setTextFill(utils.ERROR_COLOUR);

        }
    }

    public void resetLabels() {
        usernameLabel.setTextFill(utils.DEFAULT_COLOUR);
        emailLabel.setTextFill(utils.DEFAULT_COLOUR);
    }

    public void saveChanges() {
        resetLabels();
        getTextFields();
        boolean[] validationFlags = affiliate.validateData();
        if (validationFlags[4]) {
            Properties userProperties = UserDataConfig.loadProperties();
            if (userProperties.getProperty("user.role").equals("Academic")) {
                AcademicDAOImplementation dao = new AcademicDAOImplementation();
                try {
                    dao.updateAcademicCredentialsById(affiliate);
                    utils.createAlert("Éxito",
                            "¡Se hicieron los cambios con éxito!");
                } catch (SQLException e) {
                    utils.createAlert("Error",
                            "Hubo un error al actualizar al usuario, no se hicieron cambios");
                    logger.error("Error al actualizar usuario", e);
                }
            } else {
                StudentDAOImplementation dao = new StudentDAOImplementation();
                try {
                    dao.updateStudentCredentialsById(affiliate);
                    utils.createAlert("Éxito",
                            "¡Se hicieron los cambios con éxito!");
                } catch (SQLException e) {
                    utils.createAlert("Error",
                            "Hubo un error al actualizar al usuario, no se hicieron cambios");
                    logger.error("Error al actualizar usuario", e);
                }
            }
        } else {
            showInvalidData(validationFlags);
            utils.createAlert("Datos inválidos",
                    "Hay datos inválidos, por favor corrígalos");
        }

    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }
}
