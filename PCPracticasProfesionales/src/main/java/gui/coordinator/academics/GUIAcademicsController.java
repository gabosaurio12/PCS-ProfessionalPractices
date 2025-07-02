package gui.coordinator.academics;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.academic.AcademicDAOImplementation;
import gui.util.GUIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import model.Academic;
import model.Section;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUIAcademicsController {
    private static final Logger logger = LogManager.getLogger(GUIAcademicsController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML private Rectangle blueFringe;
    @FXML private TableView<Academic> academicsTable;
    @FXML private TableColumn<Academic, String> tuitionColumn;
    @FXML private TableColumn<Academic, String> nameColumn;
    @FXML private TableColumn<Academic, String> lastNamesColumn;
    @FXML private TableColumn<Academic, String> roleColumn;
    @FXML private TableColumn<Academic, String> userNameColumn;
    @FXML private ComboBox<Section> periodComboBox;

    public void initComponents() {
        setPeriodComboBox();
        setAcademicsTable();
    }

    @FXML
    public void setAcademicsTable() {
        tuitionColumn.setCellValueFactory(new PropertyValueFactory<>("personalNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNamesColumn.setCellValueFactory(new PropertyValueFactory<>("firstSurname"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        refreshTable();
    }

    public void refreshTable() {
        List<Academic> academics = new ArrayList<>();
        try {
            academics = new AcademicDAOImplementation().getAcademics(
                    periodComboBox.getValue().getPeriod());
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Error al recopilar académicos");
            logger.error("Error al recopilar academicos", e);
            if (e.getMessage().contains("Unknown database")) {
                utils.createAlert("Error",
                        "No se encontró la base de datos");
            }
        }
        ObservableList<Academic> academicsList = FXCollections.observableArrayList(academics);
        academicsTable.setItems(academicsList);
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
            utils.createAlert("Error", "Error al recopilar sección");
            logger.error("Error al recopilar sección", e);
            if (e.getMessage().contains("Unknown database")) {
                utils.createAlert("Error",
                        "No se encontró la base de datos");
            }
        }
    }

    public void openAdminMainPage() {
        utils.openWindow("/fxml/Coordinator/GUIMainPageCoordinator.fxml", "Menú Principal",
                blueFringe);
    }

    public void openRegisterAcademicPage() {
        GUIRegisterAcademicController controller = utils.openWindow(
                "/fxml/Coordinator/Academic/GUIRegisterAcademic.fxml", "Registrar Académico",
                GUIRegisterAcademicController.class, blueFringe);
        if (controller != null) {
            controller.initComponents(periodComboBox.getValue().getSectionID());
        }
    }

    public void openGUIAcademicDetailsFromButton() {
        Academic selectedAcademic = academicsTable.getSelectionModel().getSelectedItem();
        if (selectedAcademic != null) {
            GUIAcademicDetailsController controller = utils.openWindow(
                    "/fxml/Coordinator/Academic/GUIAcademicDetails.fxml", "Detalles de Académico",
                    GUIAcademicDetailsController.class, blueFringe);
            if (controller != null) {
                Academic fullAcademic = new Academic();
                try {
                    fullAcademic = new AcademicDAOImplementation().getAcademicByID(selectedAcademic.getId());
                } catch (SQLException e) {
                    utils.createAlert("Error",
                            "Error al leer académicos");
                    logger.error("Error al leer académico: ", e);
                }
                controller.initComponents(fullAcademic);
            }
        } else {
            utils.createAlert("Elija un académico",
                    "Elija un académico para ver sus detalles");
        }
    }

    public void openGUIAcademicDetailsFromClicks() {
        academicsTable.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Academic selectedAcademic = academicsTable.getSelectionModel().getSelectedItem();
                if (selectedAcademic != null) {
                    GUIAcademicDetailsController controller = utils.openWindow(
                            "/fxml/Coordinator/Academic/GUIAcademicDetails.fxml", "Detalles de Académico",
                            GUIAcademicDetailsController.class, blueFringe);
                    if (controller != null) {
                        Academic fullAcademic = new Academic();
                        try {
                            fullAcademic = new AcademicDAOImplementation().getAcademicByID(selectedAcademic.getId());
                        } catch (SQLException e) {
                            utils.createAlert("Error",
                                    "Error al leer académicos");
                            logger.error("Error al leer académico: ", e);
                        }
                        if (!fullAcademic.isNull()) {
                            controller.initComponents(fullAcademic);
                        } else {
                            controller.initComponents(selectedAcademic);
                        }
                    }
                }
            }
        });
    }

    public void deleteAcademic() {
        Academic selectedAcademic =  academicsTable.getSelectionModel().getSelectedItem();
        if (selectedAcademic == null) {
            utils.createAlert("Aviso",
                    "Seleccione un académico por favor");
        } else {
            AcademicDAOImplementation dao = new AcademicDAOImplementation();
            try {
                dao.dropAcademic(dao.getAcademicByPersonalNumber(selectedAcademic.getPersonalNumber()).getId());
                utils.createAlert("Éxito",
                        "Se eliminó el Académico con éxito!");
                refreshTable();
            } catch (SQLException e) {
                utils.createAlert("Error",
                        "Error al eliminar Académico");
                logger.error("Error al eliminar Académico", e);
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
