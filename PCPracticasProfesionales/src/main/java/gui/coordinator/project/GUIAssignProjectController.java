package gui.coordinator.project;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import businesslogic.project.ProjectDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import dataaccess.system.UserDataConfig;
import gui.coordinator.student.GUIStudentsController;
import util.DocsUtil;
import gui.util.GUIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import model.LinkedOrganization;
import model.Project;
import model.ProjectAssignation;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class GUIAssignProjectController {
    private static final Logger logger = LogManager.getLogger(GUIAssignProjectController.class);
    private final GUIUtils utils = new GUIUtils();
    private Student student;

    @FXML private Rectangle blueFringe;
    @FXML private TableView<Project> projectsTable;
    @FXML private TableColumn<Project, String> titleColumn;
    @FXML private TableColumn<Project, String> linkedOrganizationColumn;
    @FXML private TableColumn<Project, String> categoryColumn;
    @FXML private TableColumn<Project, String> beginningDateColumn;
    @FXML private TableColumn<Project, String> endingDateColumn;
    @FXML private TableColumn<Project, String> statusColumn;
    @FXML private TableColumn<Project, String> openSpotsColumn;

    public void setStudent(Student student) {
        this.student = student;
    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }

    public void openStudentsPage() {
        GUIStudentsController controller = utils.openWindow("/fxml/Coordinator/Student/GUIStudents.fxml",
                "Estudiantes",
                GUIStudentsController.class, blueFringe);
        if (controller != null) {
            controller.initComponents();
        }
    }

    public void createTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        linkedOrganizationColumn.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getLinkedOrganizationId();
            String organizationName = "";
            try {
                organizationName = new LinkedOrganizationDAOImplementation().getLinkedOrganizationNameByID(id);
            } catch (SQLException e) {
                logger.error("Error al recuperar el nombre de la organización vinculada", e);
            }

            return new SimpleStringProperty(organizationName);
        });
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        beginningDateColumn.setCellValueFactory(new PropertyValueFactory<>("beginningDate"));
        endingDateColumn.setCellValueFactory(new PropertyValueFactory<>("endingDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        openSpotsColumn.setCellValueFactory(new PropertyValueFactory<>("openSpots"));

        ObservableList<Project> projectsList = FXCollections.observableArrayList(getProjectsList());
        projectsTable.setItems(projectsList);
    }

    public List<Project> getProjectsList() {
        List<Project> projects = new ArrayList<>();
        try {
            ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
            SystemDAOImplementation dao = new SystemDAOImplementation();
            projects = projectDAO.getProjectsAvailable(dao.getCurrentSection().getPeriod());
        } catch (SQLException e) {
            utils.createAlert("¡Error al recopilar proyectos!",
                    "Hubo un error inesperado al recopilar los proyectos \n" +
                            "Inténtelo más tarde por favor");
            logger.error("Error al recopilar proyectos", e);
        }
        return projects;
    }

    public String getMonth(int month) {
        return switch (month) {
            case 1 -> "Enero";
            case 2 -> "Febrero";
            case 3 -> "Marzo";
            case 4 -> "Abril";
            case 5 -> "Mayo";
            case 6 -> "Junio";
            case 7 -> "Julio";
            case 8 -> "Agosto";
            case 9 -> "Septiembre";
            case 10 -> "Octubre";
            case 11 -> "Noviembre";
            case 12 -> "Diciembre";
            default -> "";
        };
    }

    public Map<String, String> getFillers(ProjectAssignation assignation) throws SQLException {
        LocalDate currentDate = LocalDate.now();
        Student student = new StudentDAOImplementation().getStudentByID(assignation.getStudentId());
        String studentName = student.getName().concat(" ").concat(student.getFirstSurname()).concat(
                "").concat(student.getSecondSurname());
        Project project = new ProjectDAOImplementation().getProjectById(
                assignation.getProjectId());
        String projectName = project.getTitle();
        LinkedOrganization organization = new LinkedOrganizationDAOImplementation().getLinkedOrganizationById(
                project.getLinkedOrganizationId());
        String organizationName = organization.getName();
        String organizationAddress = organization.getAddress();
        String shortDate = String.valueOf(currentDate.getDayOfMonth()).concat(" de ").concat(
                getMonth(currentDate.getMonthValue()));
        Map<String, String> filler = new HashMap<>();
        filler.put("dia", String.valueOf(currentDate.getDayOfMonth()));
        filler.put("mes", getMonth(currentDate.getMonthValue()));
        filler.put("ano", String.valueOf(currentDate.getYear()));
        filler.put("nombre", studentName);
        filler.put("nombreProyecto", projectName);
        filler.put("nombreOrganizacion", organizationName);
        filler.put("direccionOrganizacion", organizationAddress);
        filler.put("matricula", student.getTuition());
        filler.put("fecha", shortDate);
        return filler;
    }

    public void generateAssignationDocument(Map<String, String> filler, String documentPath) throws SQLException {
        String templatePath = "/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/" +
                "PCS-ProfessionalPractices/PCPracticasProfesionales/src/main/resources/" +
                "AssignationDocumentsTemplates/Formato_Oficio_Asignación.docx";
        DocsUtil docsUtil = new DocsUtil();
        try {
            docsUtil.copyDocx(templatePath, documentPath);
            docsUtil.fillDocx(templatePath, documentPath, filler);
            utils.createAlert("¡Asignación exitosa!",
                    "Se realizó la asignación correctamente \n" +
                            "Se creo el archivo en la ruta: \n" +
                            documentPath);
        } catch (IOException e) {
            logger.error("Error al copiar formato del documento de asignación", e);
            utils.createAlert("Error",
                    "Hubo un error al copiar el formato del documento de asignación");
        } catch (Exception e) {
            logger.error("Error al generar docx", e);
            utils.createAlert("Error",
                    "Hubo un error al generar el documento de asignación");
        }
    }

    public void assignProject() {
        Project project = projectsTable.getSelectionModel().getSelectedItem();
        if (project == null) {
            utils.createAlert("Aviso",
                    "Por favor elija un proyecto");
        } else {
            ProjectDAOImplementation dao = new ProjectDAOImplementation();
            ProjectAssignation assignation = new ProjectAssignation();
            assignation.setProjectId(project.getProjectID());
            assignation.setStudentId(student.getId());
            String documentPath = ("/Users/gabosaurio/Documents/UV/4° Semestre/PCS/" +
                    "ProyectoFinal/PCS-ProfessionalPractices/PCPracticasProfesionales/" +
                    "OficiosAsignacion/OficioAsignacion").concat(
                            student.getNoSpacedName()).concat(".docx");
            assignation.setDocumentPath(documentPath);
            Properties coordinatorProperties = UserDataConfig.loadProperties();
            int coordinatorID = Integer.parseInt(coordinatorProperties.getProperty("user.id"));
            assignation.setCoordinatorId(coordinatorID);
            try {
                dao.assignProjectById(assignation);

                Map<String, String> filler = getFillers(assignation);
                generateAssignationDocument(filler, documentPath);

                openStudentsPage();
            } catch (SQLException e) {
                utils.createAlert("¡Error al asignar proyecto!",
                        "Hubo un error al asignar el proyecto, " +
                                "Vuelva a intentarlo por favor");
                logger.error("Error al asignar proyecto: ", e);
            }
        }
    }
}