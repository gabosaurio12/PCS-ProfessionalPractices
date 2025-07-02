package gui.student;

import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import businesslogic.project.ProjectDAOImplementation;
import businesslogic.projectresponsible.ProjectResponsibleDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import dataaccess.system.UserDataConfig;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import model.LinkedOrganization;
import model.Project;
import model.ProjectResponsible;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Properties;

public class GUIProjectController {
    private static final Logger logger = LogManager.getLogger(GUIProjectController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML private Rectangle blueFringe;
    @FXML private Label titleLabel;
    @FXML private Label statusLabel;
    @FXML private Label projectResponsibleLabel;
    @FXML private Label categoryLabel;
    @FXML private Label beginningDateLabel;
    @FXML private Label endingDateLabel;
    @FXML private Label organizationLabel;
    @FXML private TextField filePathTextField;

    public void openGUIMainPage() {
        utils.openWindow(
                "/fxml/Student/GUIMainPageStudent.fxml",
                "Menú Principal",
                GUIMainPageStudentController.class,
                blueFringe
        );
    }

    public void init() {
        Properties userProperties = UserDataConfig.loadProperties();
        int studentId = Integer.parseInt(userProperties.getProperty("user.id"));
        int projectId = 0;
        try {
            Student student = new StudentDAOImplementation().getStudentByID(studentId);
            projectId = new ProjectDAOImplementation().getProjectById(student.getProjectId()).getProjectID();
        } catch (SQLException e) {
            logger.error("Error al recuperar id del proyecto", e);
            utils.createAlert("Error",
                    "Hubo un error al recuperar el id del proyecto");
        }
        Project project = new Project();
        try {
            project = new ProjectDAOImplementation().getProjectById(projectId);
        } catch (SQLException e) {
            logger.error("Error al recuperar proyecto");
            utils.createAlert("Error",
                    "Hubo un error al recuperar el proyecto");
        }
        if (!project.isNull()) {
            setProjectInfo(project);
        } else {
            utils.createAlert("Error",
                    "No se encontró el proyecto");
            openGUIMainPage();
        }
    }

    public void setProjectInfo(Project project) {
        titleLabel.setText(project.getTitle());
        statusLabel.setText(project.getStatus());
        ProjectResponsible responsible = new ProjectResponsible();
        try {
            responsible = new ProjectResponsibleDAOImplementation().getProjectResponsibleByID(
                    project.getProjectResponsibleId());
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Hubo un error al recuperar responsable de proyecto");
            logger.error("Error al recuperar responsable de proyecto");
        }
        String responsibleName = responsible.isNull() ? "" : responsible.getName();
        projectResponsibleLabel.setText(responsibleName);
        categoryLabel.setText(project.getCategory());
        beginningDateLabel.setText(project.getBeginningDate().toString());
        endingDateLabel.setText(project.getEndingDate().toString());
        LinkedOrganization organization = new LinkedOrganization();
        try {
            organization = new LinkedOrganizationDAOImplementation()
                    .getLinkedOrganizationById(project.getLinkedOrganizationId());
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Hubo un error al recuperar la organización vinculada del proyecto");
            logger.error("Error al recuperar la organización vinculada del proyecto");
        }
        String organizationName = organization.isNull() ? "" : organization.getName();
        organizationLabel.setText(organizationName);
        filePathTextField.setText(project.getDocumentInfoPath());
    }
}
