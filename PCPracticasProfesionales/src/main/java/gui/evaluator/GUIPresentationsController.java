package gui.evaluator;

import businesslogic.presentation.PresentationDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import gui.coordinator.GUICoordinatorMainPageController;
import gui.util.GUIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import model.Presentation;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUIPresentationsController {

    private static final Logger logger = LogManager.getLogger(GUIPresentationsController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML private TableView<Presentation> presentationsTable;
    @FXML private TableColumn<Presentation, String> idColumn;
    @FXML private TableColumn<Presentation, String> gradeColumn;
    @FXML private TableColumn<Presentation, String> dateColumn;
    @FXML private TableColumn<Presentation, String> studentColumn;
    @FXML private Rectangle blueFringe;

    public void init() {
        setPresentationsTable();
    }

    public void setPresentationsTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("presentationID"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("presentationGrade"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        studentColumn.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getStudentId();
            Student student = new Student();
            try {
                student = new StudentDAOImplementation().getStudentByID(id);
            } catch (SQLException e) {
                logger.error("Error al recuperar estudiante", e);
                utils.createAlert("Error",
                        "Hubo un error al recuperar estudiante");
            }
            String name = "";
            if (!student.isNull()) {
                name = student.getName().concat(" ").concat(student.getFirstSurname());
            }
            return new SimpleStringProperty(name);
        });

        refreshTable();
    }

    public void refreshTable() {
        List<Presentation> presentations = new ArrayList<>();
        try {
            PresentationDAOImplementation dao = new PresentationDAOImplementation();
            presentations = dao.getPresentations();
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Error al recopilar presentaciones");
            logger.error("Error al recopilar presentaciones", e);
            if (e.getMessage().contains("Unknown database")) {
                utils.createAlert("Error",
                        "No se encontró la base de datos");
            }
        }
        if (!presentations.isEmpty()) {
            ObservableList<Presentation> presentationsList = FXCollections.observableArrayList(presentations);
            presentationsTable.setItems(presentationsList);
        } else {
            presentationsTable.setItems(null);
            utils.createAlert("Aviso", "Aún no hay estudiantes registrados");
        }
    }

    public void evaluatePresentation() {
        Presentation presentation = presentationsTable.getSelectionModel().getSelectedItem();
        GUIUploadEvaluationController controller = utils.openWindow(
                "/fxml/Evaluator/GUIUploadEvaluation.fxml",
                "Subir Evaluación",
                GUIUploadEvaluationController.class);
        controller.init(presentation.getPresentationID());
    }

    public void openMainPage() {
        utils.openWindow(
                "/fxml/Evaluator/GUIMainPageEvaluator.fxml",
                "Menú Principal",
                GUICoordinatorMainPageController.class,
                blueFringe);
    }
}
