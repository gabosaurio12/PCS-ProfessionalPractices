package gui.coordinator.projectresponsible;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import businesslogic.projectresponsible.ProjectResponsibleDAOImplementation;
import gui.coordinator.linkedorganization.GUILinkedOrganizationsController;
import gui.util.GUIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import model.LinkedOrganization;
import model.ProjectResponsible;
import model.Section;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUIProjectResponsiblesController {
    private static final Logger logger = LogManager.getLogger(GUIProjectResponsiblesController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML private Rectangle blueFringe;
    @FXML private TableView<ProjectResponsible> projectResponsibleTable;
    @FXML private TableColumn<ProjectResponsible, Integer> projectResponsibleIDColumn;
    @FXML private TableColumn<ProjectResponsible, String> nameColumn;
    @FXML private TableColumn<ProjectResponsible, String> emailColumn;
    @FXML private TableColumn<ProjectResponsible, String> alterContactColumn;
    @FXML private TableColumn<ProjectResponsible, String> organizationColumn;
    @FXML private ComboBox<Section> periodComboBox;

    public void init() {
        setPeriodComboBox();
        setTable();
    }

    public void openGUIRegisterProjectResponsible() {
        GUIRegisterProjectResponsibleController controller = utils.openWindow(
                "/fxml/Coordinator/ProjectResponsible/GUIRegisterProjectResponsible.fxml",
                "Registrar Responsable de Proyecto",
                GUIRegisterProjectResponsibleController.class,
                blueFringe
        );
        controller.init(periodComboBox.getValue().getPeriod());
    }

    public void openGUIProjectResponsibleDetails() {
        GUIProjectResponsibleDetailsController controller = utils.openWindow(
                "/fxml/Coordinator/ProjectResponsible/GUIProjectResponsibleDetails.fxml",
                "Detalles de Responsable de Proyecto",
                GUIProjectResponsibleDetailsController.class,
                blueFringe
        );
        ProjectResponsible responsible = projectResponsibleTable.getSelectionModel().getSelectedItem();
        if (responsible == null) {
            utils.createAlert("Elija un responsable de proyecto",
                    "Por favor elija un responsable de proyecto para ver sus detalles");
        } else {
            controller.init(responsible);
        }
    }

    public void deleteProjectResponsible() {
        ProjectResponsible responsible = projectResponsibleTable.getSelectionModel().getSelectedItem();
        ProjectResponsibleDAOImplementation dao = new ProjectResponsibleDAOImplementation();
        try {
            dao.dropProjectResponsibleByID(responsible.getProjectResponsibleID());
            utils.createAlert("Éxito",
                    "Se eliminó el responsable de proyecto con éxito");
            refreshTable();
        } catch (SQLException e) {
            logger.error("Error al eliminar responsable de proyecto", e);
            utils.createAlert("Error",
                    "Hubo un error al eliminar al responsable de proyecto");
        }
    }

    public void setTable() {
        projectResponsibleIDColumn.setCellValueFactory(new PropertyValueFactory<>("projectResponsibleID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        alterContactColumn.setCellValueFactory(new PropertyValueFactory<>("alterContact"));
        organizationColumn.setCellValueFactory(cellData -> {
            int organizationId = cellData.getValue().getLinkedOrganizationId();
            LinkedOrganization organization = new LinkedOrganization();
            try {
                organization = new LinkedOrganizationDAOImplementation().
                        getLinkedOrganizationById(organizationId);
            } catch (SQLException e) {
                logger.error("Error al leer organización vinculdada", e);
                utils.createAlert("Error",
                        "Hubo un error al leer organización vinculada");
            }
            String organizationName = "";
            if (!organization.isNull()) {
                organizationName = organization.getName();
            }
            return new SimpleStringProperty(organizationName);
        });

        refreshTable();
    }

    public void refreshTable() {
        List<ProjectResponsible> responsibles = new ArrayList<>();
        try {
            ProjectResponsibleDAOImplementation responsibleDAO = new ProjectResponsibleDAOImplementation();
            responsibles = responsibleDAO.getProjectResponsibles(periodComboBox.getValue().toString());
        } catch (SQLException e) {
            logger.error("Error al recuperar responsables de proyecto", e);
            utils.createAlert("Error",
                    "Hubo un error al recuperar a los responsables de proyecto");
        }
        if (!responsibles.isEmpty()) {
            ObservableList<ProjectResponsible> responsiblesList = FXCollections.observableArrayList(responsibles);
            projectResponsibleTable.setItems(responsiblesList);
        } else {
            projectResponsibleTable.setItems(null);
            utils.createAlert("Aviso", "Aún no hay responsables de proyecto dados de alta");
        }
    }

    public void setPeriodComboBox() {
        List<Section> sections = new ArrayList<>();
        try {
            sections = new SystemDAOImplementation().getSections();
        } catch (SQLException e) {
            utils.createAlert("Error", "Error al recopilar periodos");
            logger.error("Error al recopilar periodos", e);
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
        }
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }

    public void openGUILinkedOrganizations() {
        GUILinkedOrganizationsController controller = utils.openWindow(
                "/fxml/Coordinator/LinkedOrganization/GUILinkedOrganizations.fxml",
                "Organizaciones Vinculadas",
                GUILinkedOrganizationsController.class, blueFringe
        );
        controller.initComponents();
    }

}
