package gui.coordinator.linkedorganization;

import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
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
        if (!flags[0]) {
            nameLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!flags[1]) {
            descriptionLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!flags[2]) {
            addressLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!flags[3]) {
            emailLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!flags[4]) {
            alterContactLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
    }

    public void resetLabels() {
        nameLabel.setTextFill(Paint.valueOf("#000000"));
        descriptionLabel.setTextFill(Paint.valueOf("#000000"));
        addressLabel.setTextFill(Paint.valueOf("#000000"));
        emailLabel.setTextFill(Paint.valueOf("#000000"));
        alterContactLabel.setTextFill(Paint.valueOf("#000000"));
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