package gui.login;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.login.LoginDAOImplementation;
import gui.util.GUIUtils;
import gui.academic.GUIMainPageAcademicController;
import gui.coordinator.GUICoordinatorMainPageController;
import gui.evaluator.GUIMainPageEvaluatorController;
import gui.student.GUIMainPageStudentController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import model.UniversityAffiliate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class GUILoginController {
    private final LoginDAOImplementation loginDAO = new LoginDAOImplementation();
    private static final Logger logger = LogManager.getLogger(GUILoginController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField userPasswordTextField;
    @FXML
    private Rectangle blueFringe;

    public void loginVerification() {
        String userName = userNameTextField.getText();
        String userPassword = userPasswordTextField.getText();
        if (userName.isEmpty() || userPassword.isEmpty()) {
           utils.createAlert("Campos vacíos",
                   "Ingrese los datos solicitados por favor");
        } else {
            try {
                if (loginDAO.getUser(userNameTextField.getText())) {
                    String hashedPassword = new LoginDAOImplementation().getHashedPassword(userPassword);
                    String userRole = loginDAO.getUserRole(userName, hashedPassword);
                    openMainPage(userRole, userName, hashedPassword);
                } else {
                    utils.createAlert("Usuario no encontrado",
                            "El usuario ingresado no está registrado");
                }
            } catch (SQLException e) {
                utils.createAlert("Error",
                        "Error al conectarse a la base de datos");
                logger.error("Error al conectarse al LoginDB ", e);
            } catch (Exception e) {
                utils.createAlert("Error",
                        "Hubo un error inesperado, se cerrará la aplicación");
                logger.error("Error al abrir MainPage", e);
                utils.closeWindow(blueFringe);
            }
        }

    }

    public void openMainPage(String userRole, String userName, String hashedPassword) {
        try {
            if (userRole != null) {
                SystemDAOImplementation dao = new SystemDAOImplementation();
                switch (userRole) {
                    case "COORDINADOR":
                        utils.openWindow("/fxml/Coordinator/GUIMainPageCoordinator.fxml",
                                "Menú Principal",
                                GUICoordinatorMainPageController.class, blueFringe);
                        UniversityAffiliate coordinator = dao.getAffiliateByCredentials(
                                userName, hashedPassword, "Academic");
                        saveUser(coordinator, "Academic");
                        break;
                    case "ACADEMICO":
                        utils.openWindow(
                                "/fxml/Academic/GUIMainPageAcademic.fxml",
                                "Menú Principal",
                                GUIMainPageAcademicController.class, blueFringe);
                        UniversityAffiliate academic = dao.getAffiliateByCredentials(
                                userName, hashedPassword, "Academic");
                        saveUser(academic, "Academic");
                        break;

                    case "EVALUADOR":
                        utils.openWindow(
                                "/fxml/Evaluator/GUIMainPageEvaluator.fxml",
                                "Menú Principal",
                                GUIMainPageEvaluatorController.class, blueFringe);
                        UniversityAffiliate evaluator = dao.getAffiliateByCredentials(
                                userName, hashedPassword, "Academic");
                        saveUser(evaluator, "Academic");
                        break;

                    case "ESTUDIANTE":
                        GUIMainPageStudentController controller = utils.openWindow(
                                "/fxml/Student/GUIMainPageStudent.fxml",
                                "Menú Principal",
                                GUIMainPageStudentController.class,
                                blueFringe);
                        controller.init();
                        UniversityAffiliate student = dao.getAffiliateByCredentials(
                                userName, hashedPassword, "Student");
                        saveUser(student, "Student");
                        break;
                }
            } else {
                utils.createAlert("Datos incorrectos",
                        "La contraseña es incorrecta");
            }
        } catch (SQLException e) {
            logger.error("Error al leer universitario", e);
        }
    }

    public void saveUser(UniversityAffiliate user, String role) {
        File userFile = new File("/Users/gabosaurio/Documents/UV/4° Semestre/PCS/" +
                "ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/properties/user.properties");
        Properties properties = new Properties();
        properties.setProperty("user.id", String.valueOf(user.getId()));
        properties.setProperty("user.name", user.getName());
        properties.setProperty("user.username", user.getUserName());
        properties.setProperty("user.password", user.getPassword());
        properties.setProperty("user.email", user.getEmail());
        properties.setProperty("user.role", role);
        try (FileOutputStream fileOutputStream = new FileOutputStream(userFile)) {
            properties.store(fileOutputStream, "User Information");
        } catch (IOException e) {
            utils.createAlert("Error al iniciar sesión",
                    "Hubo un error al iniciar sesión, intente de nuevo");
            logger.error("Error al crear archivo user.properties", e);
        }
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }
}
