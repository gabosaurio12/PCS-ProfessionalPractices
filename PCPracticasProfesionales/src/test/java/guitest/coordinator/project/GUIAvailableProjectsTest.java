package guitest.coordinator.project;

import businesslogic.project.ProjectDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Project;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;


public class GUIAvailableProjectsTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIAvailableProjectsTest.class);
    private static int studentID = 0;
    private static int projectID = 0;

    @Override
    public void start(Stage stage) throws Exception {
        registerStudent();
        registerProject();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/GUILogin.fxml"));
        Parent root = loader.load();
        stage.setTitle("GUIAvailableProjectsTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void registerStudent() {
        Student student = new Student();
        student.setTuition("S23014038");
        student.setName("Gabriel Antonio");
        student.setFirstSurname("González");
        student.setSecondSurname("López");
        student.setEmail(student.generateEmail());
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());
        student.setCreditAdvance(300);
        student.setGrade(10F);
        student.setAcademicId(13);

        try {
            StudentDAOImplementation dao = new StudentDAOImplementation();
            studentID = dao.registerStudent(student);
            int sectionID = 1;
            dao.assignSection(studentID, sectionID);
        } catch (SQLException e) {
            logger.error("Error al registrar Estudiante", e);
        }
        student.setId(studentID);
    }

    public void registerProject() {
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

        try {
            ProjectDAOImplementation dao = new ProjectDAOImplementation();
            projectID = dao.registerProject(project);
            int sectionID = 1;
            dao.assignProjectSection(projectID, sectionID);
        } catch (SQLException e) {
            logger.error("Error al registrar proyecto", e);
        }
    }

    @Test
    void assignProjectTest() {
        clickOn("#userNameTextField").write("angel");
        interact(() ->
                ((PasswordField) lookup("#userPasswordTextField").query()
                ).setText("angel54321"));
        verifyThat("#userNameTextField", hasText("angel"));
        verifyThat("#userPasswordTextField", hasText("angel54321"));
        clickOn("#loginButton");
        sleep(1500);

        clickOn("#studentsButton");
        sleep(250);

        clickOn("S23014038");
        sleep(250);
        clickOn("#assignProjectButton");
        sleep(500);
        clickOn("Juego de rol");
        clickOn("#assignButton");

        push(KeyCode.ENTER);
        sleep(250);
        clickOn("Juego de rol");
        sleep(2000);
    }

    @AfterAll
    public static void after() {
        ProjectDAOImplementation dao = new ProjectDAOImplementation();
        StudentDAOImplementation studentDAO = new StudentDAOImplementation();
        try {
            studentDAO.dropStudent(studentID);
            dao.dropProject(projectID);
        } catch (SQLException e) {
            logger.error("Error al eliminar organización", e);
        }
    }
}
