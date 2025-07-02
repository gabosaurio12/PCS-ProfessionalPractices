package gui.coordinator.linkedorganization;

import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import model.LinkedOrganization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GUIRegisterLinkedOrganizationController {

    private static final Logger logger = LogManager.getLogger(GUIRegisterLinkedOrganizationController.class);
    private final GUIUtils utils = new GUIUtils();
    private int sectionID;

    @FXML private Rectangle blueFringe;
    @FXML private TextField nameTextField;
    @FXML private TextArea descriptionTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField alterContactTextField;
    @FXML private Label nameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label addressLabel;
    @FXML private Label emailLabel;
    @FXML private Label alterContactLabel;

    public void initComponents(int sectionID) {
        this.sectionID = sectionID;
    }

    public void getTextFields(LinkedOrganization organization) {
        organization.setName(nameTextField.getText());
        organization.setDescription(descriptionTextField.getText());
        organization.setAddress(addressTextField.getText());
        organization.setEmail(emailTextField.getText());
        organization.setAlterContact(alterContactTextField.getText());
    }

    public void showInvalidData(boolean[] flags) {
        int NAME_FLAG = 0;
        if (!flags[NAME_FLAG]) {
            nameLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int DESCRIPTION_FLAG = 1;
        if (!flags[DESCRIPTION_FLAG]) {
            descriptionLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int ADDRESS_FLAG = 2;
        if (!flags[ADDRESS_FLAG]) {
            addressLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int EMAIL_FLAG = 3;
        if (!flags[EMAIL_FLAG]) {
            emailLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int ALTER_CONTACT_FLAG = 4;
        if (!flags[ALTER_CONTACT_FLAG]) {
            alterContactLabel.setTextFill(utils.ERROR_COLOUR);
        }
    }

    public void resetLabels() {
        nameLabel.setTextFill(utils.DEFAULT_COLOUR);
        descriptionLabel.setTextFill(utils.DEFAULT_COLOUR);
        addressLabel.setTextFill(utils.DEFAULT_COLOUR);
        emailLabel.setTextFill(utils.DEFAULT_COLOUR);
        alterContactLabel.setTextFill(utils.DEFAULT_COLOUR);
    }

    @FXML
    public void registerLinkedOrganization() {
        resetLabels();
        LinkedOrganization organization = new LinkedOrganization();
        getTextFields(organization);
        boolean[] validationFlags = organization.validateData();
        if (validationFlags[validationFlags.length - 1]) {
            try {
                LinkedOrganizationDAOImplementation dao = new LinkedOrganizationDAOImplementation();
                int organizationID = dao.registerLinkedOrganization(organization);
                dao.assignLinkedOrganizationSection(organizationID, sectionID);
                utils.createAlert("Éxito", "Organización vinculada registrada correctamente.");
                closeWindow();
                openLinkedOrganizationsPage();
            } catch (Exception e) {
                logger.error("Error al registrar proyecto", e);
                utils.createAlert("Error", "No se pudo registrar la organización vinculada.");
            }
        } else {
            utils.createAlert("Datos inválidos",
                    "Campos inválidos");
            showInvalidData(validationFlags);

        }
    }

    public void openLinkedOrganizationsPage() {
        GUILinkedOrganizationsController controller = utils.openWindow(
                "/fxml/Coordinator/LinkedOrganization/GUILinkedOrganizations.fxml",
                "Organizaciones Vinculadas",
                GUILinkedOrganizationsController.class,
                blueFringe
        );
        controller.initComponents();
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }
}