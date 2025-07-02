package dao;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.project.ProjectDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import model.Project;
import model.ProjectAssignation;
import model.ProjectRequest;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectDAOTest {

    private static final ProjectDAOImplementation dao = new ProjectDAOImplementation();
    private static final Logger logger = LogManager.getLogger(ProjectDAOTest.class);
    private static final int[] projectsID = new int[2];
    private static int projectRegistrationStatus;
    private static int studentID;
    private static int requestID;

    @BeforeAll
    public static void setUp() {
        Student student = new Student();
        student.setTuition("S23014038");
        student.setName("Gabriel Antonio");
        student.setFirstSurname("González");
        student.setSecondSurname("López");
        student.setCreditAdvance(300);
        student.setEmail(student.generateEmail());
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());
        student.setGrade(10F);
        student.setAcademicId(13);


        Project project = new Project();
        project.setTitle("Wikipedia");
        project.setProjectResponsibleId(1);
        project.setOpenSpots(3);
        project.setStatus("En desarrollo");
        project.setDocumentInfoPath("/ruta/al/documento");
        project.setLinkedOrganizationId(5);
        project.setCategory("Desarrollo web");
        project.setBeginningDate(Date.valueOf(LocalDate.of(2025, 5, 10)));
        project.setEndingDate(Date.valueOf(LocalDate.of(2026, 6, 15)));

        projectsID[0] = 0;
        try {
            studentID = new StudentDAOImplementation().registerStudent(student);
            projectRegistrationStatus = dao.getProjectsRegistrationStatus();
            projectsID[0] = dao.registerProject(project);
        } catch (SQLException e) {
            logger.error("Error al registrar proyecto", e);
        }
    }

    @Test
    public void isNullTest() {
        Project project = new Project();
        Assertions.assertTrue(project.isNull());
    }

    @Test
    public void isNullTestFail() {
        Project project = new Project();
        try {
            project = dao.getProjectById(projectsID[0]);
        } catch (SQLException e) {
            logger.error("Error al leer proyecto: ", e);
        }
        Assertions.assertFalse(project.isNull());
    }

    @Order(1)
    @Test
    public void registerProjectTest() {
        Project project = new Project();
        project.setTitle("Juego de rol");
        project.setProjectResponsibleId(1);
        project.setOpenSpots(3);
        project.setStatus("En desarrollo");
        project.setDocumentInfoPath("/ruta/al/documento");
        project.setLinkedOrganizationId(5);
        project.setCategory("Desarrollo de videojuagos");
        project.setBeginningDate(Date.valueOf(LocalDate.of(2025, 5, 10)));
        project.setEndingDate(Date.valueOf(LocalDate.of(2028, 6, 15)));

        projectsID[1] = 0;
        try {
            projectsID[1] = dao.registerProject(project);
        } catch (SQLException e) {
            logger.error("Error al registrar proyecto", e);
        }
        Assertions.assertNotEquals(0, projectsID[1]);
    }

    @Test
    public void registerProjectTestFailException() {
        Project project = new Project();
        project.setTitle("Wikipedia");
        project.setProjectResponsibleId(1);
        project.setOpenSpots(3);
        project.setDocumentInfoPath("/ruta/al/documento");
        project.setLinkedOrganizationId(5);
        project.setCategory("Desarrollo web");
        project.setBeginningDate(Date.valueOf(LocalDate.of(2025, 5, 10)));

        Assertions.assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> dao.registerProject(project));
    }

    @Order(5)
    @Test
    public void registerProjectRequestTest() {
        ProjectRequest request = new ProjectRequest();
        request.setStudentId(studentID);
        request.setName("Jean");
        request.setDocumentPath("/ruta/de/prueba");
        requestID = 0;
        try {
            requestID = dao.registerProjectRequest(request);
        } catch (SQLException e) {
            logger.error("Error al registrar solicitud de proyecto", e);
        }
        Assertions.assertNotEquals(0, requestID);
    }

    @Order(6)
    @Test
    public void getRequestsTest() {
        List<ProjectRequest> requests = new ArrayList<>();
        try {
            requests = dao.getRequests();
        } catch (SQLException e) {
            logger.error("Error al leer solicitudes de proyecto", e);
        }
        Assertions.assertFalse(requests.isEmpty());
    }

    @Order(7)
    @Test
    public void dropProjectRequestByIDTest() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropProjectRequestByID(requestID);
        } catch (SQLException e) {
            logger.error("Error al eliminar solicitud de proyecto", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void updateProjectByIdTest() {
        Project project = new Project();
        project.setProjectID(projectsID[0]);
        project.setTitle("Wikipedia");
        project.setOpenSpots(3);
        project.setStatus("Concluido");
        project.setDocumentInfoPath("/ruta/al/documento");
        project.setLinkedOrganizationId(5);
        project.setCategory("Desarrollo web");
        project.setProjectResponsibleId(1);
        project.setBeginningDate(Date.valueOf(LocalDate.of(2025, 5, 10)));
        project.setEndingDate(Date.valueOf(LocalDate.of(2026, 6, 15)));

        int affectedRows = 0;
        try {
            affectedRows = dao.updateProjectById(project);
        } catch (SQLException e) {
            logger.error("Error al actualizar solicitud de proyecto", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void updateProjectByIdTestFailException() {
        Project project = new Project();
        project.setProjectID(projectsID[0]);
        project.setTitle("Wikipedia");
        project.setOpenSpots(5);
        project.setProjectResponsibleId(1);
        project.setLinkedOrganizationId(5);
        project.setCategory("Desarrollo web");
        project.setBeginningDate(Date.valueOf(LocalDate.of(2025, 5, 10)));
        project.setEndingDate(Date.valueOf(LocalDate.of(2026, 6, 15)));

        Assertions.assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> dao.updateProjectById(project));
    }

    @Test
    public void assignProjectSectionTest() {
        int affectedRows = 0;
        int sectionID = 1;
        try {
            affectedRows = dao.assignProjectSection(projectsID[0], sectionID);
        } catch (SQLException e) {
            logger.error("Error al recuperar proyectos por periodo", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void assignProjectSectionTestFailException() {
        int sectionId = 50000;
        Assertions.assertThrows(SQLException.class,
                () -> dao.assignProjectSection(projectsID[0], sectionId));
    }

    @Test
    public void getProjectsAvailableTest() {
        List<Project> projects = new ArrayList<>();
        try {
            String currentPeriod = new SystemDAOImplementation().getCurrentSection().getPeriod();
            projects = dao.getProjectsAvailable(currentPeriod);
        } catch (SQLException e) {
            logger.error("Error al leer proyectos disponibles", e);
        }
        boolean availabilityFlag = false;
        if (!projects.isEmpty()) {
            for (Project project : projects) {
                availabilityFlag = project.getOpenSpots() > 0;
            }
        }
        Assertions.assertTrue(availabilityFlag);
    }

    @Test
    public void getProjectsAvailableTestFail() {
        List<Project> projects = new ArrayList<>();
        try {
            String currentPeriod = "200051";
            projects = dao.getProjectsAvailable(currentPeriod);
        } catch (SQLException e) {
            logger.error("Error al leer proyectos disponibles", e);
        }
        boolean availabilityFlag = false;
        if (!projects.isEmpty()) {
            for (Project project : projects) {
                availabilityFlag = project.getOpenSpots() > 0;
            }
        }
        Assertions.assertFalse(availabilityFlag);
    }

    @Order(2)
    @Test
    public void assignProjectByIdTest() {
        ProjectAssignation assignation = new ProjectAssignation();
        int coordinatorId = 13;
        assignation.setCoordinatorId(coordinatorId);
        assignation.setDocumentPath("/ruta/al/archivo");
        assignation.setProjectId(projectsID[0]);
        assignation.setStudentId(studentID);
        int affectedRows = 0;
        try {
            affectedRows = dao.assignProjectById(assignation);
        } catch (SQLException e) {
            logger.error("Error al asignar proyecto", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void assignProjectByIdTestFail() {
        ProjectAssignation assignation = new ProjectAssignation();
        int coordinatorId = 13;
        assignation.setCoordinatorId(coordinatorId);
        assignation.setDocumentPath("");
        assignation.setStudentId(studentID);
        assignation.setProjectId(projectsID[0]);
        int affectedRows = 0;
        if (!assignation.getDocumentPath().isBlank()) {
            try {
                affectedRows = dao.assignProjectById(assignation);
            } catch (SQLException e) {
                logger.error("Error al asignar proyecto", e);
            }
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @Test
    public void setProjectsRegistrationStatusTest() {
        int newProjectRegistrationStatus = projectRegistrationStatus == 1 ? 0 : 1;
        int affectedRows = 0;
        try {
            affectedRows = dao.setProjectsRegistrationStatus(newProjectRegistrationStatus);
        } catch (SQLException e) {
            logger.error("Error al actulizar estado de registro de proyectos", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Order(3)
    @Test
    public void dropAssignationByIDTest() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropAssignationByID(studentID);
        } catch (SQLException e) {
            logger.error("Error al eliminar asignación", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void dropAssignationByIDTestFail() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropAssignationByID(0);
        } catch (SQLException e) {
            logger.error("Error al eliminar asignación", e);
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @Order(4)
    @Test
    public void dropProjectTest() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropProject(projectsID[1]);
        } catch (SQLException e) {
            logger.error("Error al eliminar proyecto: ", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void dropProjectTestFail() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropProject(0);
        } catch (SQLException e) {
            logger.error("Error al eliminar proyecto: ", e);
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @AfterAll
    public static void after() {
        try {
            new StudentDAOImplementation().dropStudent(studentID);
            dao.dropProject(projectsID[0]);

            dao.setProjectsRegistrationStatus(projectRegistrationStatus);
        } catch (SQLException e) {
            logger.error("Error al eliminar proyecto, estudiante o solicitud", e);
        }
    }
}
