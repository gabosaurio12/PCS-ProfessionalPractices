package gui.coordinator.project;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import businesslogic.project.ProjectDAOImplementation;
import businesslogic.projectresponsible.ProjectResponsibleDAOImplementation;
import gui.util.GUIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import model.Project;
import model.ProjectResponsible;
import model.Section;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

public class GUIProjectsController {
    private static final Logger logger = LogManager.getLogger(GUIProjectsController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML private Rectangle blueFringe;
    @FXML private TableView<Project> projectsTable;
    @FXML private TableColumn<Project, String> projectNameColumn;
    @FXML private TableColumn<Project, String> organizationColumn;
    @FXML private TableColumn<Project, String> startDateColumn;
    @FXML private TableColumn<Project, String> endDateColumn;
    @FXML private TableColumn<Project, String> statusColumn;
    @FXML private TableColumn<Project, String> responsibleColumn;
    @FXML private ComboBox<Section> periodComboBox;
    @FXML private Slider projectsManagerSlider;

    public void init() {
        setPeriodComboBox();
        setProjectsTable();
        int buttonStatus = 0;
        try {
            buttonStatus = new ProjectDAOImplementation().getProjectsRegistrationStatus();
        } catch (SQLException e) {
            logger.error("Error al leer el estado del registro de proyectos", e);
        }
        setProjectsRegistrationSlider(buttonStatus);
        projectsManagerSlider.valueProperty().addListener(e -> setProjectsRegistration());
    }

    public void openAdminMainPage() {
        utils.openWindow("/fxml/Coordinator/GUIMainPageCoordinator.fxml",
                "Menú Principal", blueFringe);
    }

    public void openRegisterProjectPage() {
        GUIRegisterProjectController controller = utils.openWindow(
                "/fxml/Coordinator/Project/GUIRegisterProject.fxml",
                "Registrar Proyecto",
                GUIRegisterProjectController.class,
                blueFringe);
        try {
            controller.initComponents(new SystemDAOImplementation().getCurrentSection().getSectionID());
        } catch (SQLException e) {
            logger.error("Error al recuperar la sección actual", e);
        }
    }

    @FXML
    public void setProjectsTable() {
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        organizationColumn.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getLinkedOrganizationId();
            String organizationName = "";
            try {
                organizationName = new LinkedOrganizationDAOImplementation()
                        .getLinkedOrganizationNameByID(id);
            } catch (SQLException e) {
                logger.error("Error al recuperar el nombre de la organización vinculada", e);
            }
            return new SimpleStringProperty(organizationName);
        });
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("beginningDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endingDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        responsibleColumn.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getProjectResponsibleId();
            ProjectResponsible responsible = new ProjectResponsible();
            try {
                responsible = new ProjectResponsibleDAOImplementation().getProjectResponsibleByID(id);
            } catch (SQLException e) {
                logger.error("Error al recuperar responsables de proyecto");
            }
            String name = "";
            if (!responsible.isNull()) {
                name = responsible.getName();
            }
            return new SimpleStringProperty(name);
        });

        refreshTable();
    }

    public void refreshTable() {
        List<Project> projects = new ArrayList<>();
        try {
            ProjectDAOImplementation dao = new ProjectDAOImplementation();
            projects = dao.getProjectsByPeriod(periodComboBox.getValue().getPeriod());
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Error al recopilar proyectos");
            logger.error("Error al recopilar proyectos", e);
            if (e.getMessage().contains("Unknown database")) {
                utils.createAlert("Error",
                        "No se encontró la base de datos");
            }
        }

        ObservableList<Project> projectsList = FXCollections.observableArrayList(projects);
        projectsTable.setItems(projectsList);
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

    public void setProjectsRegistrationSlider(int buttonStatus) {
        projectsManagerSlider.adjustValue(buttonStatus);
    }

    public void setProjectsRegistration() {
        double registrationStatus = projectsManagerSlider.getValue();
        if (registrationStatus > 0.5) {
            saveRegistrationConfig(1);
            utils.createAlert("Se habilitó el registro de proyectos",
                    "Ahora se pueden registrar proyectos");
        } else {
            saveRegistrationConfig(0);
            utils.createAlert("Se deshabilitó el registro de proyectos",
                    "Ya no se pueden registrar proyectos");
        }
    }

    public void saveRegistrationConfig(int registrationConfig) {
        try {
            ProjectDAOImplementation dao = new ProjectDAOImplementation();
            dao.setProjectsRegistrationStatus(registrationConfig);
        } catch (SQLException e) {
            logger.error("Error al guardar la configuración del registro de proyectos");
            utils.createAlert("Error",
                    "Hubo un error al guardar la configuración del registro de proyectos");
        }
    }

    public void openGUIProjectDetailsFromButton() {
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            GUIProjectDetailsController controller = new GUIUtils().openWindow(
                    "/fxml/Coordinator/Project/GUIProjectDetails.fxml",
                    "Detalles de Proyecto",
                    GUIProjectDetailsController.class, blueFringe);
            if (controller != null) {
                Project fullProjectData = new Project();
                try {
                    ProjectDAOImplementation dao = new ProjectDAOImplementation();
                    fullProjectData = dao.getProjectById(selectedProject.getProjectID());
                } catch (SQLException e) {
                    logger.error("Error al leer Proyecto: ", e);
                }
                if (!fullProjectData.isNull()) {
                    controller.init(fullProjectData, periodComboBox.getValue().getSectionID());
                } else {
                    controller.init(selectedProject, periodComboBox.getValue().getSectionID());
                }
            }
        } else {
            utils.createAlert("Elija un proyecto",
                    "Por favor elija un proyecto para ver sus detalles");
        }
    }

    public void openGUIProjectDetailsFromClicks() {
        projectsTable.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();
                if (selectedProject != null) {
                    GUIProjectDetailsController controller = new GUIUtils().openWindow(
                            "/fxml/Coordinator/Project/GUIProjectDetails.fxml",
                            "Detalles de Proyecto",
                            GUIProjectDetailsController.class, blueFringe);
                    if (controller != null) {
                        Project fullProjectData = new Project();
                        try {
                            ProjectDAOImplementation dao = new ProjectDAOImplementation();
                            fullProjectData = dao.getProjectById(selectedProject.getProjectID());
                        } catch (SQLException e) {
                            logger.error("Error al leer Proyecto: ", e);
                        }
                        if (!fullProjectData.isNull()) {
                            controller.init(fullProjectData, periodComboBox.getValue().getSectionID());
                        } else {
                            controller.init(selectedProject, periodComboBox.getValue().getSectionID());
                        }
                    }
                } else {
                    utils.createAlert("Elija un proyecto",
                            "Por favor elija un proyecto para ver sus detalles");
                }
            }
        });
    }

    public void deleteProject() {
        Project selectedProject =  projectsTable.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            utils.createAlert("Aviso",
                    "Seleccione un proyecto por favor");
        } else {
            ProjectDAOImplementation dao = new ProjectDAOImplementation();
            try {
                dao.dropProject(selectedProject.getProjectID());
                utils.createAlert("Éxito",
                        "Se eliminó el Proyecto con éxito!");
                refreshTable();
            } catch (SQLException e) {
                utils.createAlert("Error",
                        "Error al eliminar Proyecto");
                logger.error("Error al eliminar Proyecto", e);
            }
        }
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }
}
