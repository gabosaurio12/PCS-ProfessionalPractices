package gui.coordinator.linkedorganization;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import gui.coordinator.projectresponsible.GUIProjectResponsiblesController;
import gui.util.GUIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import model.LinkedOrganization;
import model.Section;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUILinkedOrganizationsController {
    private static final Logger logger = LogManager.getLogger(GUILinkedOrganizationsController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML
    private Rectangle blueFringe;
    @FXML
    private TableView<LinkedOrganization> linkedOrganizationsTable;
    @FXML
    private TableColumn<LinkedOrganization, Integer> linkedOrganizationIDColumn;
    @FXML
    private TableColumn<LinkedOrganization, String> organizationNameColumn;
    @FXML
    private TableColumn<LinkedOrganization, String> addressColumn;
    @FXML
    private TableColumn<LinkedOrganization, String> emailColumn;
    @FXML
    private TableColumn<LinkedOrganization, String> alterContactColumn;
    @FXML
    private ComboBox<Section> periodComboBox;

    public void initComponents() {
        setLinkedOrganizationsTable();
        setPeriodComboBox();
    }

    public void setLinkedOrganizationsTable() {
        linkedOrganizationIDColumn.setCellValueFactory(new PropertyValueFactory<>("linkedOrganizationID"));
        organizationNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        alterContactColumn.setCellValueFactory(new PropertyValueFactory<>("alterContact"));

        refreshTable();
    }

    public void refreshTable() {
        List<LinkedOrganization> linkedOrganizations = new ArrayList<>();
        try {
            LinkedOrganizationDAOImplementation dao = new LinkedOrganizationDAOImplementation();
            linkedOrganizations = dao.getLinkedOrganizations();
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Error al recopilar organizaciones vinculadas");
            logger.error("Error al recuperar organizaciones vinculadas", e);
            if (e.getMessage().contains("Unknown database")) {
                utils.createAlert("Error",
                        "No se encontró la base de datos");
            }
        }

        ObservableList<LinkedOrganization> organizationsList = FXCollections.observableArrayList(linkedOrganizations);
        linkedOrganizationsTable.setItems(organizationsList);
    }

    public void setPeriodComboBox() {
        List<Section> sections = new ArrayList<>();
        try {
            sections = new SystemDAOImplementation().getSections();
        } catch (SQLException e) {
            utils.createAlert("Error", "Error al recopilar periodos");
            logger.error("Error al recopilar periodos", e);
            if (e.getMessage().contains("Unknown database")) {
                utils.createAlert("Error",
                        "No se encontró la base de datos");
            }
        }
        if (!sections.isEmpty()) {
            for (Section i : sections) {
                periodComboBox.getItems().add(i);
            }
        } else {
            utils.createAlert("Aviso", "No hay periodos disponibles");
        }
        try {
            periodComboBox.setValue(new SystemDAOImplementation().getCurrentSection());
        } catch (SQLException e) {
            utils.createAlert("Error", "Error al obtener periodos");
            logger.error("Error al obtener periodos", e);
            if (e.getMessage().contains("Unknown database")) {
                utils.createAlert("Error",
                        "No se encontró la base de datos");
            }
        }
    }

    public void openAdminMainPage() {
        utils.openWindow("/fxml/Coordinator/GUIMainPageCoordinator.fxml", "Menú Principal", blueFringe);
    }

    public void openGUIRegisterLinkedOrganization() {
        GUIRegisterLinkedOrganizationController controller = utils.openWindow(
                "/fxml/Coordinator/LinkedOrganization/GUIRegisterLinkedOrganization.fxml",
                "Registrar Organización Vinculada",
                GUIRegisterLinkedOrganizationController.class,
                blueFringe);
        controller.initComponents(periodComboBox.getValue().getSectionID());
    }

    public void openGUILinkedOrganizationDetails() {
        LinkedOrganization organization = linkedOrganizationsTable.getSelectionModel().getSelectedItem();
        if (organization != null) {
            GUILinkedOrganizationDetailsController controller = utils.openWindow(
                    "/fxml/Coordinator/LinkedOrganization/GUILinkedOrganizationDetails.fxml",
                    "Detalles de Organización Vinculada",
                    GUILinkedOrganizationDetailsController.class,
                    blueFringe
            );
            controller.init(organization);
        } else {
            utils.createAlert("Inválido",
                    "Por favor seleccione una organización vinculada");
        }
    }

    public void deleteLinkedOrganization() {
        LinkedOrganization selectedOrganization = linkedOrganizationsTable.getSelectionModel().getSelectedItem();
        if (selectedOrganization == null) {
            utils.createAlert("Aviso",
                    "Seleccione una organización por favor");
        } else {
            LinkedOrganizationDAOImplementation dao = new LinkedOrganizationDAOImplementation();
            try {
                dao.dropLinkedOrganization(selectedOrganization.getLinkedOrganizationID());
                refreshTable();
                utils.createAlert("Éxito", "Se eliminó con éxito la Organización vinculada");
            } catch (SQLException e) {
                utils.createAlert("Error", "Error al eliminar Organización vinculada");
                logger.error("Error al eliminar Organización vinculada", e);
            }
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

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }
}
