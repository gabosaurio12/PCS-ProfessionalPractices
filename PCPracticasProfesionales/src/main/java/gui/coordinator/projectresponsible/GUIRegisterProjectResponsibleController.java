package gui.coordinator.projectresponsible;

import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import businesslogic.projectresponsible.ProjectResponsibleDAOImplementation;
import gui.util.GUIUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.LinkedOrganization;
import model.ProjectResponsible;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class GUIRegisterProjectResponsibleController {
    private static final Logger logger = LogManager.getLogger(GUIRegisterProjectResponsibleController.class);
    private final GUIUtils utils = new GUIUtils();
    private String period;

    @FXML private Rectangle blueFringe;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label organizationLabel;
    @FXML private TextField nameTextField;
    @FXML private TextField alterContactTextField;
    @FXML private TextField emailTextField;
    @FXML private ComboBox<LinkedOrganization> organizationComboBox;

    public void init(String period) {
        this.period = period;
        setOrganizationBox();
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

    public void getTextFields(ProjectResponsible responsible) {
        responsible.setName(nameTextField.getText());
        responsible.setAlterContact(alterContactTextField.getText());
        responsible.setEmail(emailTextField.getText());
        responsible.setLinkedOrganizationId(organizationComboBox.getValue().getLinkedOrganizationID());
    }

    public void showInvalidData(boolean[] dataFlags) {
        if (!dataFlags[0]) {
            nameLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!dataFlags[1]) {
            emailLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!dataFlags[2]) {
            organizationLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
    }

    public void registerProjectResponsible() {
        ProjectResponsible responsible = new ProjectResponsible();
        getTextFields(responsible);
        boolean[] validationFlags = responsible.validateData();
        if (validationFlags[validationFlags.length - 1]) {
            try {
                ProjectResponsibleDAOImplementation dao = new ProjectResponsibleDAOImplementation();
                responsible.setPeriod(period);
                dao.registerProjectResponsible(responsible);
                utils.createAlert("Éxito",
                        "Se registró el responsable de proyecto con éxito");
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
