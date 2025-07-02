package guitest.coordinator.linkedorganization;

import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import gui.coordinator.linkedorganization.GUILinkedOrganizationsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.LinkedOrganization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;
import java.util.List;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIRegisterOrganizationTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIRegisterOrganizationTest.class);

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/LinkedOrganization/GUILinkedOrganizations.fxml"));
        Parent root = loader.load();
        GUILinkedOrganizationsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUIRegisterOrganizationTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void registrationTest() {
        clickOn("#newButton");
        sleep(500);
        clickOn("#nameTextField").write("Alta república");
        clickOn("#descriptionTextField").write("Dirección de la galaxia hace mucho tiempo " +
                "en una galaxia muy, muy lejana");
        clickOn("#addressTextField").write("Coruscant");
        clickOn("#emailTextField").write("highrep@gmail.com");
        clickOn("#alterContactTextField").write("5546789876");

        verifyThat("#nameTextField", hasText("Alta república"));
        verifyThat("#descriptionTextField", hasText("Dirección de la galaxia hace mucho tiempo " +
                "en una galaxia muy, muy lejana"));
        verifyThat("#addressTextField", hasText("Coruscant"));
        verifyThat("#emailTextField", hasText("highrep@gmail.com"));
        verifyThat("#alterContactTextField", hasText("5546789876"));

        clickOn("#saveButton");
        sleep(250);
        push(KeyCode.ENTER);
        sleep(250);
        clickOn("Alta república");
        sleep(1000);
    }

    @AfterAll
    public static void deleteStudent() {
        LinkedOrganizationDAOImplementation dao = new LinkedOrganizationDAOImplementation();
        try {
            List<LinkedOrganization> organizations = dao.getLinkedOrganizations();
            LinkedOrganization organization = organizations.getLast();
            if (organization != null && organization.getName().equals("Alta república")) {
                dao.dropLinkedOrganization(organization.getLinkedOrganizationID());
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar organización", e);
        }
    }
}
