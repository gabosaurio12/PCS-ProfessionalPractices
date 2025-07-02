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

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

public class GUILinkedOrganizationDetailsTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUILinkedOrganizationDetailsTest.class);

    private static int organizationID;

    @Override
    public void start(Stage stage) throws Exception {
        registerOrganization();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/LinkedOrganization/GUILinkedOrganizations.fxml"));
        Parent root = loader.load();
        GUILinkedOrganizationsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUILinkedOrganizationDetailsTest");
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
    public void updateOrganizationTest() {
        clickOn(String.valueOf(organizationID));
        clickOn("#editButton");
        sleep(250);
        doubleClickOn("#descriptionTextField");
        doubleClickOn("#descriptionTextField").write("Empresa colombiana de ropa premium");

        verifyThat("#descriptionTextField", hasText("Empresa colombiana de ropa premium"));
        clickOn("#saveButton");
        sleep(250);
        push(KeyCode.ENTER);
        sleep(250);
        clickOn(String.valueOf(organizationID));
        sleep(2000);
    }

    @AfterAll
    public static void after() {
        LinkedOrganizationDAOImplementation dao = new LinkedOrganizationDAOImplementation();
        try {
            dao.dropLinkedOrganization(organizationID);
        } catch (SQLException e) {
            logger.error("Error al eliminar organización", e);
        }
    }
}
