package guitest.student;

import businesslogic.academic.AcademicDAOImplementation;
import businesslogic.project.ProjectDAOImplementation;
import businesslogic.student.StudentDAOImplementation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Academic;
import model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIProjectRequestFailTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIProjectRequestFailTest.class);
    private static int studentID;
    private static int academicID;
    private static int projectStatus;
    private String username;
    private String password;

    @BeforeAll
    public static void setUp() {
        try {
            ProjectDAOImplementation dao = new ProjectDAOImplementation();
            projectStatus = dao.getProjectsRegistrationStatus();
            dao.setProjectsRegistrationStatus(1);
        } catch (SQLException e) {
            logger.error("Error al iniciar estatus de proyectos");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        registerAcademic();
        registerStudent();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/GUILogin.fxml"));
        Parent root = loader.load();
        stage.setTitle("GUIProjectRequestTestFail");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void registerAcademic() {
        Academic academic = new Academic();
        academic.setName("Michael");
        academic.setFirstSurname("Jackson");
        academic.setSecondSurname("");
        academic.setEmail("mj@uv.mx");
        academic.setPersonalNumber("01");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        academicID = 0;
        try {
            AcademicDAOImplementation dao = new AcademicDAOImplementation();
            academicID = dao.registerAcademic(academic);
            int sectionID = 1;
            dao.assignSection(academicID, sectionID);
        } catch (SQLException e) {
            logger.error("Error al registrar Académico", e);
        }
    }

    public Student registerStudent() {
        Student student = new Student();
        student.setTuition("S23014038");
        student.setName("Gabriel Antonio");
        student.setFirstSurname("González");
        student.setSecondSurname("López");
        student.setEmail(student.generateEmail());
        username = student.generateUserName();
        student.setUserName(username);
        password = student.generatePassword();
        student.setPassword(password);
        student.setCreditAdvance(300);
        student.setGrade(10F);
        student.setAcademicId(academicID);

        studentID = 0;
        try {
            StudentDAOImplementation dao = new StudentDAOImplementation();
            studentID = dao.registerStudent(student);
            int sectionID = 1;
            dao.assignSection(studentID, sectionID);
        } catch (SQLException e) {
            logger.error("Error al registrar Estudiante", e);
        }
        student.setId(studentID);
        return student;
    }

    @Test
    void assignProjectTestFail() {
        clickOn("#userNameTextField").write(username);
        interact(() ->
                ((PasswordField) lookup("#userPasswordTextField").query()
                ).setText(password));
        verifyThat("#userNameTextField", hasText(username));
        verifyThat("#userPasswordTextField", hasText(password));
        clickOn("#loginButton");
        sleep(1500);

        clickOn("#requestProjectButton");
        sleep(250);

        clickOn("#saveButton");
        sleep(500);
        push(KeyCode.ENTER);
        sleep(1000);
    }

    @AfterAll
    public static void after() {
        AcademicDAOImplementation academicDAO = new AcademicDAOImplementation();
        StudentDAOImplementation studentDAO = new StudentDAOImplementation();
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        try {
            studentDAO.dropStudent(studentID);
            academicDAO.dropAcademic(academicID);

            projectDAO.setProjectsRegistrationStatus(projectStatus);
        } catch (SQLException e) {
            logger.error("Error al eliminar organización", e);
        }
    }
}
