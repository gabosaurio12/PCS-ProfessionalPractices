package gui.evaluator;

import gui.GUIEditUserController;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

public class GUIMainPageEvaluatorController {
    private final GUIUtils utils = new GUIUtils();

    @FXML
    private Rectangle blueFringe;


    public void openGUICronogram() {
        GUICronogramController controller = utils.openWindow(
                "/fxml/Evaluator/GUICronogram.fxml",
                "Cronograma",
                GUICronogramController.class,
                blueFringe);
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

    public void openGUIPresentations() {
        GUIPresentationsController controller = utils.openWindow(
                "/fxml/Evaluator/GUIPresentations.fxml",
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
