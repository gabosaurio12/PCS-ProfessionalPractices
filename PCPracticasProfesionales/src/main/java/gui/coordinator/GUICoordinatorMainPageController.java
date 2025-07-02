package gui.coordinator;

import gui.coordinator.academics.GUIAcademicsController;
import gui.coordinator.linkedorganization.GUILinkedOrganizationsController;
import gui.coordinator.presentations.GUIPresentationsController;
import gui.coordinator.project.GUIProjectsController;
import gui.coordinator.student.GUIStudentsController;
import gui.GUIEditUserController;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

public class GUICoordinatorMainPageController {
    private final GUIUtils utils = new GUIUtils();

    @FXML
    private Rectangle blueFringe;

    public void openGUIStudents() {
        GUIStudentsController controller = utils.openWindow(
                "/fxml/Coordinator/Student/GUIStudents.fxml", "Estudiantes",
                GUIStudentsController.class, blueFringe);
        if (controller != null) {
            controller.initComponents();
        }
    }

    public void openGUIAcademics() {
        GUIAcademicsController controller = utils.openWindow(
                "/fxml/Coordinator/Academic/GUIAcademics.fxml", "Academicos",
                GUIAcademicsController.class, blueFringe);
        if (controller != null) {
            controller.initComponents();
        }
    }

    public void openGUIProjects() {
        GUIProjectsController controller = utils.openWindow(
                "/fxml/Coordinator/Project/GUIProjects.fxml", "Proyectos",
                GUIProjectsController.class, blueFringe);
        controller.init();
    }

    public void openGUILinkedOrganizations() {
        GUILinkedOrganizationsController controller = utils.openWindow(
                "/fxml/Coordinator/LinkedOrganization/GUILinkedOrganizations.fxml",
                "Organizaciones Vinculadas",
                GUILinkedOrganizationsController.class, blueFringe
        );
        controller.initComponents();
    }

    public void openGUICronogram() {
        GUICronogramController contoller = utils.openWindow(
                "/fxml/Coordinator/GUICronogram.fxml",
                "Cronograma",
                GUICronogramController.class,
                blueFringe);
        contoller.init();
    }

    public void openGUIEditUser() {
        GUIEditUserController controller = utils.openWindow(
                "/fxml/GUIEditUser.fxml",
                "Editar usuario",
                GUIEditUserController.class
        );
        controller.init();
    }

    public void openGUIPresentations() {
        GUIPresentationsController controller = utils.openWindow(
                "/fxml/Coordinator/Presentation/GUIPresentations.fxml",
                "Presentaciones",
                GUIPresentationsController.class,
                blueFringe);
        controller.init();
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }
}
