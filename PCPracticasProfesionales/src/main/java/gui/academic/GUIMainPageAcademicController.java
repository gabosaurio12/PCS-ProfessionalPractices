package gui.academic;

import gui.academic.student.GUIStudentsController;
import gui.GUIEditUserController;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

public class GUIMainPageAcademicController {
    private final GUIUtils utils = new GUIUtils();

    @FXML private Rectangle blueFringe;

    public void openGUIStudents() {
        GUIStudentsController controller = utils.openWindow(
                "/fxml/Academic/Student/GUIStudents.fxml",
                "Estudiantes",
                GUIStudentsController.class,
                blueFringe);
        controller.initComponents();
    }

    public void openGUIProjects() {
    }

    public void openGUICronogram() {
    }

    public void openGUIEditUser() {
        GUIEditUserController controller = utils.openWindow(
                "/fxml/GUIEditUser.fxml",
                "Editar usuario",
                GUIEditUserController.class
        );
        controller.init();
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }

}
