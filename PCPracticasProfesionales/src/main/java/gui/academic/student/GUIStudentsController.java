package gui.academic.student;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.project.ProjectDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import gui.academic.statistics.GUIStatisticsController;
import gui.util.GUIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import model.Project;
import model.Section;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUIStudentsController {
    private static final Logger logger = LogManager.getLogger(GUIStudentsController.class);
    private final GUIUtils utils = new GUIUtils();
    private Section section;

    @FXML
    private Rectangle blueFringe;
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> tuitionColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> lastNamesColumn;
    @FXML private TableColumn<Student, String> academicColumn;
    @FXML private TableColumn<Student, String> userNameColumn;
    @FXML private TableColumn<Student, String> projectColumn;

    public void initComponents() {
        setStudentsTable();
    }

    @FXML
    public void setStudentsTable() {
        tuitionColumn.setCellValueFactory(new PropertyValueFactory<>("tuition"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNamesColumn.setCellValueFactory(cellData -> {
            String paternalSurname = cellData.getValue().getFirstSurname();
            String maternalSurname = cellData.getValue().getSecondSurname();
            String surnames = paternalSurname.concat(" ").concat(maternalSurname);
            return new SimpleStringProperty(surnames);
        });
        academicColumn.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getAcademicId();
            String academicName = "";
            try {
                AcademicDAOImplementation academicDAO = new AcademicDAOImplementation();
                academicName = academicDAO.getAcademicNameByID(id);
            } catch (SQLException e) {
                logger.error("Error al recuperar nombre del académico", e);
            }
            return new SimpleStringProperty(academicName);
        });
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        projectColumn.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getProjectId();
            String projectName = "";
            try {
                ProjectDAOImplementation dao = new ProjectDAOImplementation();
                Project project = dao.getProjectById(id);
                if (project == null) {
                    projectName = "Sin asignar";
                } else {
                    projectName = project.getTitle();
                }
            } catch (SQLException e) {
                logger.error("Error al recuperar nombre del proyecto", e);
            }
            return new SimpleStringProperty(projectName);
        });

        refreshTable();
    }

    public void refreshTable() {
        List<Student> students = new ArrayList<>();
        try {
            StudentDAOImplementation studentDAO = new StudentDAOImplementation();
            section = new SystemDAOImplementation().getCurrentSection();
            students = studentDAO.getStudents(String.valueOf(section.getPeriod()));
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Error al recopilar estudiantes");
            logger.error("Error al recopilar estudiantes", e);
            if (e.getMessage().contains("Unknown database")) {
                utils.createAlert("Error",
                        "No se encontró la base de datos");
            }
        }
        if (!students.isEmpty()) {
            ObservableList<Student> studentsList = FXCollections.observableArrayList(students);
            studentsTable.setItems(studentsList);
        } else {
            studentsTable.setItems(null);
            utils.createAlert("Aviso", "Aún no hay estudiantes dados de alta");
        }
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void openRegisterStudentPage() {
        GUIRegisterStudentController controller = utils.openWindow(
                "/fxml/Academic/Student/GUIRegisterStudent.fxml",
                "Registrar Estudiante",
                GUIRegisterStudentController.class, blueFringe);
        if (controller != null) {
            controller.initComponents(section.getSectionID());
        }
    }

    public void openMainPage() {
        utils.openWindow("/fxml/Academic/GUIMainPageAcademic.fxml",
                "Menú Principal", blueFringe);
    }

    public void openGUIStudentDetailsFromButton() {
        Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            GUIStudentDetailsController controller = utils.openWindow(
                    "/fxml/Academic/Student/GUIStudentDetails.fxml",
                    "Detalles de Estudiante",
                    GUIStudentDetailsController.class, blueFringe);
            if (controller != null) {
                try {
                    StudentDAOImplementation studentDAO = new StudentDAOImplementation();
                    Student fullStudentData = studentDAO.getStudentByTuition(
                            selectedStudent.getTuition());
                    controller.initComponents(
                            fullStudentData,
                            new SystemDAOImplementation().getCurrentSection().getSectionID());
                } catch (SQLException e) {
                    utils.createAlert("Error",
                            "Error al leer estudiantes");
                    logger.error("Error al leer estudiante: ", e);
                }

            }
        } else {
            utils.createAlert("Elija un estudiante",
                    "Por favor elija un estudiante para ver sus detalles");
        }
    }

    public void openGUIStudentDetailsFromClicks() {
        studentsTable.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
                if (selectedStudent != null) {
                    GUIStudentDetailsController controller = utils.openWindow(
                            "/fxml/Academic/Student/GUIStudentDetails.fxml",
                            "Detalles de Estudiante",
                            GUIStudentDetailsController.class, blueFringe);
                    if (controller != null) {
                        try {
                            StudentDAOImplementation studentDAO = new StudentDAOImplementation();
                            Student fullStudentData = studentDAO.getStudentByTuition(
                                    selectedStudent.getTuition());
                            controller.initComponents(
                                    fullStudentData,
                                    new SystemDAOImplementation().getCurrentSection().getSectionID());
                        } catch (SQLException e) {
                            utils.createAlert("Error",
                                    "Error al leer estudiantes");
                            logger.error("Error al leer estudiante: ", e);
                        }
                    }
                }
            }
        });
    }

    public void openGUIStatistics() {
        Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            utils.createAlert("Error",
                    "Elige a un estudiante por favor");
        } else {
            Student fullStudentData = null;
            try {
                fullStudentData = new StudentDAOImplementation().getStudentByTuition(
                        selectedStudent.getTuition());
            } catch (SQLException e) {
                logger.error("Error al buscar estudiante", e);
            }
            GUIStatisticsController controller = utils.openWindow(
                    "/fxml/Academic/GUIStatistics.fxml",
                    "Estadísticas",
                    GUIStatisticsController.class,
                    blueFringe);
            controller.init(fullStudentData);
        }
    }
}
