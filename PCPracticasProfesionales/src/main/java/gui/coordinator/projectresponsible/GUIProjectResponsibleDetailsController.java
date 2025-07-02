package gui.coordinator.projectresponsible;

import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import businesslogic.projectresponsible.ProjectResponsibleDAOImplementation;
import gui.util.GUIUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import model.LinkedOrganization;
import model.ProjectResponsible;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class GUIProjectResponsibleDetailsController {
    private static final Logger logger = LogManager.getLogger(GUIProjectResponsibleDetailsController.class);
    private final GUIUtils utils = new GUIUtils();
    private int projectResponsibleID;

    @FXML
    private Rectangle blueFringe;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label organizationLabel;
    @FXML private TextField nameTextField;
    @FXML private TextField alterContactTextField;
    @FXML private TextField emailTextField;
    @FXML private ComboBox<LinkedOrganization> organizationComboBox;

    public void init(ProjectResponsible responsible) {
        projectResponsibleID = responsible.getProjectResponsibleID();
        setOrganizationBox();
        setTextFields(responsible);
    }

    public void setOrganizationBox() {
        try {
            List<LinkedOrganization> organizations = new LinkedOrganizationDAOImplementation()
                    .getLinkedOrganizations();
            organizationComboBox.setItems(FXCollections.observableArrayList(organizations));
        } catch (Exception e) {
            logger.error("Error al cargar organizaciones", e);
            utils.createAlert("Error", "No se pudieron cargar las organizaciones.");
        }
    }

    public void setTextFields(ProjectResponsible responsible) {
        nameTextField.setText(responsible.getName());
        alterContactTextField.setText(responsible.getAlterContact());
        emailTextField.setText(responsible.getEmail());
        try {
            organizationComboBox.setValue(
                    new LinkedOrganizationDAOImplementation().getLinkedOrganizationById(
                            responsible.getLinkedOrganizationId()));
        } catch (SQLException e) {
            logger.error("Error al recuperar organización vinculada", e);
            utils.createAlert("Error",
                    "Error al recuperar organización vinculada");
            openGUIProjectResponsibles();
        }
    }

    public void getTextFields(ProjectResponsible responsible) {
        responsible.setName(nameTextField.getText());
        responsible.setAlterContact(alterContactTextField.getText());
        responsible.setEmail(emailTextField.getText());
        responsible.setLinkedOrganizationId(organizationComboBox.getValue().getLinkedOrganizationID());
    }

    public void showInvalidData(boolean[] dataFlags) {
        int NAME_FLAG = 0;
        if (!dataFlags[NAME_FLAG]) {
            nameLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int EMAIL_FLAG = 1;
        if (!dataFlags[EMAIL_FLAG]) {
            emailLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int ORGANIZATION_FLAG = 2;
        if (!dataFlags[ORGANIZATION_FLAG]) {
            organizationLabel.setTextFill(utils.ERROR_COLOUR);
        }
    }

    public void resetLabels() {
        nameLabel.setTextFill(utils.DEFAULT_COLOUR);
        emailLabel.setTextFill(utils.DEFAULT_COLOUR);
        organizationLabel.setTextFill(utils.DEFAULT_COLOUR);
    }

    public void updateProjectResponsible() {
        resetLabels();
        ProjectResponsible responsible = new ProjectResponsible();
        getTextFields(responsible);
        boolean[] validationFlags = responsible.validateData();
        if (validationFlags[validationFlags.length - 1]) {
            try {
                ProjectResponsibleDAOImplementation dao = new ProjectResponsibleDAOImplementation();
                responsible.setProjectResponsibleID(projectResponsibleID);
                dao.updateProjectResponsible(responsible);
                utils.createAlert("Éxito",
                        "El responsable de proyecto fue actualizado correctamente");
                openGUIProjectResponsibles();
            } catch (SQLException e) {
                logger.error("Error al registrar responsable de proyecto", e);
                utils.createAlert("Error",
                        "Hubo un error al registrar al responsable de proyecto");
            }
        } else {
            showInvalidData(validationFlags);
            utils.createAlert("Campos inválidos",
                    "Los campos ingresados son inválidos, corrígalos por favor");
        }

    }

    public void openGUIProjectResponsibles() {
        GUIProjectResponsiblesController controller = utils.openWindow(
                "/fxml/Coordinator/ProjectResponsible/GUIProjectResponsibles.fxml",
                "Responsables de proyecto",
                GUIProjectResponsiblesController.class,
                blueFringe
        );
        controller.init();
    }
}
