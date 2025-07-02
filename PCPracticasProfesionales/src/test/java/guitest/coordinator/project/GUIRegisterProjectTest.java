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

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIRegisterProjectTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIRegisterProjectTest.class);

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Project/GUIProjects.fxml"));
        Parent root = loader.load();
        GUIProjectsController controller = loader.getController();
        controller.init();
        stage.setTitle("GUIRegisterProjectTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void registrationTest() {
        clickOn("#newProjectButton");
        sleep(500);
        clickOn("#titleTextField").write("Gestor de expedientes Sith");
        clickOn("#openSpotsSpinner");
        push(KeyCode.UP);
        push(KeyCode.UP);
        clickOn("#statusTextField").write("En planeación");
        clickOn("#categoryTextField").write("Desarrollo de app de escritorio");
        DatePicker beginningPicker = lookup("#beginningDatePicker").queryAs(DatePicker.class);
        interact(() -> beginningPicker.setValue(LocalDate.of(2025, 6, 20)));

        DatePicker endingPicker = lookup("#endingDatePicker").queryAs(DatePicker.class);
        interact(() -> endingPicker.setValue(LocalDate.of(2028, 6, 20)));
        clickOn("#projectResponsibleComboBox");
        sleep(500);
        clickOn("Slobotsky");
        clickOn("#organizationComboBox");
        sleep(500);
        clickOn("La Cotorrisa");

        verifyThat("#titleTextField", hasText("Gestor de expedientes Sith"));
        Assertions.assertEquals(3, lookup("#openSpotsSpinner").queryAs(Spinner.class).getValue());
        verifyThat("#statusTextField", hasText("En planeación"));
        verifyThat("#categoryTextField", hasText("Desarrollo de app de escritorio"));
        Assertions.assertEquals(LocalDate.of(2025, 6, 20), beginningPicker.getValue());
        Assertions.assertEquals(LocalDate.of(2028, 6, 20), endingPicker.getValue());

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
            List<Project> projects = dao.getProjectsByPeriod("202551");
            Project project = projects.getLast();
            if (project != null && project.getTitle().equals("Gestor de expedientes Sith")) {
                dao.dropProject(project.getProjectID());
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar organización", e);
        }
    }
}
