package gui.coordinator.linkedorganization;

import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
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

    public void updateLinkedOrganization() {
        resetLabels();
        LinkedOrganization organization = new LinkedOrganization();
        getTextFields(organization);
        boolean[] validationFlags = organization.validateData();
        if (validationFlags[5]) {
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
