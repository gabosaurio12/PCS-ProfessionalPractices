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
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;

public class GUILinkedOrganizationsDeleteTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUILinkedOrganizationsDeleteTest.class);

    private static int organizationID;

    @Override
    public void start(Stage stage) throws Exception {
        registerOrganization();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/LinkedOrganization/GUILinkedOrganizations.fxml"));
        Parent root = loader.load();
        GUILinkedOrganizationsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUILinkedOrganizationsDeleteTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void registerOrganization() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setName("EcoModa");
        organization.setDescription("Empresa de Ropa");
        organization.setAddress("Colombia DP: 15, CP: 88888");
        organization.setEmail("ecomoda@gmail.com");
        organization.setAlterContact("2257748621");

        organizationID = 0;
        if (organization.validateData()[5]) {
            try {
                LinkedOrganizationDAOImplementation dao = new LinkedOrganizationDAOImplementation();
                organizationID = dao.registerLinkedOrganization(organization);
            } catch (SQLException e) {
                logger.error("Error al registrar organización vinculada", e);
            }
        }
    }

    @Test
    public void deleteOrganizationTest() {
        clickOn(String.valueOf(organizationID));
        clickOn("#deleteButton");
        sleep(500);
        push(KeyCode.ENTER);
        sleep(1000);
    }
}
