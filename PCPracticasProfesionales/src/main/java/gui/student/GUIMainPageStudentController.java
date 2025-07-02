package gui.student;

import businesslogic.project.ProjectDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import dataaccess.system.UserDataConfig;
import gui.GUIEditUserController;
import gui.util.GUIUtils;
import gui.student.statistics.GUIStatisticsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Properties;

public class GUIMainPageStudentController {
    private static final Logger logger = LogManager.getLogger(GUIMainPageStudentController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML private Rectangle blueFringe;
    @FXML private Button requestProjectButton;

    public void init() {
        int buttonStatus = 0;
        try {
            buttonStatus = new ProjectDAOImplementation().getProjectsRegistrationStatus();
        } catch (SQLException e) {
            logger.error("Error al leer el estado del registro de proyectos", e);
        }
        initializeProjectsRegistration(buttonStatus);
    }

    public void initializeProjectsRegistration(int buttonStatus) {
        requestProjectButton.setDisable(buttonStatus != 1);
    }

    public void openGUIProject() {
        GUIProjectController controller = utils.openWindow(
                "/fxml/Student/GUIProject.fxml", "Proyecto",
                GUIProjectController.class, blueFringe);
        controller.init();
    }

    public void openGUICronogram() {
        GUICronogramController controller = utils.openWindow(
                "/fxml/Student/GUICronogram.fxml",
                "Cronograma",
                GUICronogramController.class,
                blueFringe);
        controller.init();
    }

    public void openGUIProjectRequest() {
        GUIProjectRequestController controller = utils.openWindow(
                "/fxml/Student/GUIProjectRequest.fxml",
                "Solicitud de proyecto",
                GUIProjectRequestController.class
        );
        controller.init();
    }

    public void openGUIEditUser() {
        GUIEditUserController controller = utils.openWindow(
                "/fxml/GUIEditUser.fxml",
                "Editar usuario",
                GUIEditUserController.class
        );
        controller.init();
    }

    public void openGUIRegisterAutoevaluation () {
        GUIRegisterAutoevaluationController controller = utils.openWindow(
                "/fxml/Student/GUIRegisterAutoevaluation.fxml",
                "Registrar autoevaluación",
                GUIRegisterAutoevaluationController.class);
        controller.init();
    }

    public void openGUIUploadReport() {
        GUIUploadReportController controller = utils.openWindow(
                "/fxml/Student/GUIUploadReport.fxml",
                "Subir Reporte",
                GUIUploadReportController.class);
        controller.init();
    }

    public void openGUIStatistics() {
        Student student = null;
        Properties userProperties = UserDataConfig.loadProperties();
        try {
            student = new StudentDAOImplementation().getStudentByID(
                    Integer.parseInt(userProperties.getProperty("user.id")));
        } catch (SQLException e) {
            logger.error("Error al recuperar datos", e);
        }
        GUIStatisticsController controller = utils.openWindow(
                "/fxml/Student/Statistics/GUIStatistics.fxml",
                "Avance",
                GUIStatisticsController.class,
                blueFringe);
        controller.init(student);
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }
}
