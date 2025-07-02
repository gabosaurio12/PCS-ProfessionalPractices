package gui.coordinator.linkedorganization;

import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import model.LinkedOrganization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class GUILinkedOrganizationDetailsController {

    private static final Logger logger = LogManager.getLogger(GUILinkedOrganizationDetailsController.class);
    private final GUIUtils utils = new GUIUtils();
    private int organizationID;

    @FXML
    private Rectangle blueFringe;
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

    public void init(LinkedOrganization organization) {
        setTextFields(organization);
        organizationID = organization.getLinkedOrganizationID();
    }

    public void setTextFields(LinkedOrganization organization) {
        nameTextField.setText(organization.getName());
        descriptionTextField.setText(organization.getDescription());
        addressTextField.setText(organization.getAddress());
        emailTextField.setText(organization.getEmail());
        alterContactTextField.setText(organization.getAlterContact());
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

    public void updateLinkedOrganization() {
        resetLabels();
        LinkedOrganization organization = new LinkedOrganization();
        getTextFields(organization);
        boolean[] validationFlags = organization.validateData();
        if (validationFlags[validationFlags.length - 1]) {
            organization.setLinkedOrganizationID(organizationID);
            LinkedOrganizationDAOImplementation dao = new LinkedOrganizationDAOImplementation();
            try {
                dao.updateLinkedOrganizationByID(organization);
                utils.createAlert("Éxito",
                        "Se actualizó la organización vinculada con éxito");
            } catch (SQLException e) {
                logger.error("Error al actualizar organización vinculada", e);
                utils.createAlert("Error",
                        "Hubo un error al actualizar la organización vinculada");
            } finally {
                openLinkedOrganizationsPage();
            }
        } else {
            utils.createAlert("Error",
                    "Hay valores ingresados inválidos");
            showInvalidData(validationFlags);
        }
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }
}
