package guitest.coordinator.project;

import businesslogic.project.ProjectDAOImplementation;
import gui.coordinator.project.GUIProjectsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIProjectDetailsTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIProjectDetailsTest.class);
    private static int projectID = 0;

    @Override
    public void start(Stage stage) throws Exception {
        registerProject();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Project/GUIProjects.fxml"));
        Parent root = loader.load();
        GUIProjectsController controller = loader.getController();
        controller.init();
        stage.setTitle("GUIProjectDetailsTest");
        stage.setScene(new Scene(root));
        stage.show();
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

        projectID = 0;
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
    void updateTest() {
        clickOn("Juego de rol");
        clickOn("#editButton");
        sleep(500);
        clickOn("#titleTextField");
        doubleClickOn("#titleTextField").write("Gestor de expedientes Sith");
        clickOn("#openSpotsSpinner");
        push(KeyCode.UP);
        push(KeyCode.UP);

        verifyThat("#titleTextField", hasText("Gestor de expedientes Sith"));
        Assertions.assertEquals(5, lookup("#openSpotsSpinner").queryAs(Spinner.class).getValue());

        clickOn("#saveButton");
        push(KeyCode.ENTER);
        sleep(250);
        clickOn("Gestor de expedientes Sith");
        sleep(2000);
    }

    @AfterAll
    public static void after() {
        ProjectDAOImplementation dao = new ProjectDAOImplementation();
        try {
            dao.dropProject(projectID);
        } catch (SQLException e) {
            logger.error("Error al eliminar organización", e);
        }
    }
}
