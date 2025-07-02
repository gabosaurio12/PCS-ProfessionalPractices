package gui.academic.statistics;

import businesslogic.presentation.PresentationDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import gui.coordinator.student.GUIStudentsController;
import gui.util.GUIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import model.Presentation;
import model.Report;
import model.Student;
import model.StudentProgress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUIStatisticsController {
    private static final Logger logger = LogManager.getLogger(GUIStatisticsController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML Rectangle blueFringe;
    @FXML private Label studentLabel;
    @FXML private Label hoursLabel;
    @FXML private Label gradeLabel;
    @FXML private TableView<Presentation> presentationsTable;
    @FXML private TableColumn<Presentation, String> presentationColumn;
    @FXML private TableColumn<Presentation, Integer> averageColumn;
    @FXML private TableView<Report> documentsTable;
    @FXML private TableColumn<Report, String> documentColumn;

    public void init(Student student) {
        setLabels(student);
        setPresentationTable(student);
        setDocumentsTable(student);
    }

    public void setLabels(Student student) {
        studentLabel.setText(student.getName().concat(" ").concat(
                student.getFirstSurname()).concat(" ").concat(
                        student.getSecondSurname()));
        StudentProgress progress = new StudentProgress();
        try {
            progress = new StudentDAOImplementation().getStudentProgressByTuition(
                    student.getTuition());
        } catch (SQLException e) {
            logger.error("Error al recuperar progreso del estudiante");
            utils.createAlert("Error",
                    "Error al recuperar progreso del estudiante");
        }
        if (!progress.isNull()) {
            hoursLabel.setText(String.valueOf(progress.getHoursValidated()));
            gradeLabel.setText(String.valueOf(student.getGrade()));
        }
    }

    public void setPresentationTable(Student student) {
        presentationColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        averageColumn.setCellValueFactory(new PropertyValueFactory<>("presentationGrade"));

        List<Presentation> presentations = new ArrayList<>();
        try {
            PresentationDAOImplementation dao = new PresentationDAOImplementation();
            presentations = dao.getStudentPresentations(student.getId());
        } catch (SQLException e) {
            logger.error("Error al recuperar presentaciones", e);
            utils.createAlert("Error",
                    "Error al recuperar presentaciones");
        }
        if (!presentations.isEmpty()) {
            ObservableList<Presentation> presentationsList = FXCollections.observableArrayList(presentations);
            presentationsTable.setItems(presentationsList);
        } else {
            presentationsTable.setItems(null);
        }

    }

    public void setDocumentsTable(Student student) {
        documentColumn.setCellValueFactory(new PropertyValueFactory<>("reportPath"));
        List<Report> reports = new ArrayList<>();
        try {
            StudentDAOImplementation dao = new StudentDAOImplementation();
            int id = dao.getStudentByTuition(student.getTuition()).getId();
            reports = new StudentDAOImplementation().getReportsByStudentID(id);
        } catch (SQLException e) {
            logger.error("Error al recuperar reportes", e);
            utils.createAlert("Error",
                    "Error al recuperar reportes");
        }
        if (!reports.isEmpty()) {
            ObservableList<Report> reportsList = FXCollections.observableArrayList(reports);
            documentsTable.setItems(reportsList);
        } else {
            documentsTable.setItems(null);
        }
    }

    public void viewPresentationDetails() {
        Presentation presentation = presentationsTable.getSelectionModel().getSelectedItem();
        if (presentation == null) {
            utils.createAlert("Alerta",
                    "Por favor elija una presentación para ver sus detalles");

        } else {
            GUIPresentationDetailsController controller =
                    utils.openWindow(
                            "/fxml/Academic/GUIPresentationDetails.fxml",
                            "Detalles de Presentación",
                            GUIPresentationDetailsController.class);
            controller.init(presentation);
        }
    }

    public void closeDetails() {
        GUIStudentsController controller = utils.openWindow(
                "/fxml/Academic/Student/GUIStudents.fxml",
                "Estudiantes",
                GUIStudentsController.class, blueFringe);
        if (controller != null) {
            controller.initComponents();
        }
    }
}
