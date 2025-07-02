package guitest.coordinator.academic;

import businesslogic.academic.AcademicDAOImplementation;
import gui.coordinator.academics.GUIAcademicsController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Academic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIAcademicDetailsTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIAcademicDetailsTest.class);
    private static int academicID;
    @Override
    public void start(Stage stage) throws Exception {
        registerAcademic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Academic/GUIAcademics.fxml"));
        Parent root = loader.load();
        GUIAcademicsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUIUpdateStudentTest");
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

        academicID= 0;
        try {
            AcademicDAOImplementation dao = new AcademicDAOImplementation();
            academicID = dao.registerAcademic(academic);
            int sectionID = 1;
            dao.assignSection(academicID, sectionID);
        } catch (SQLException e) {
            logger.error("Error al registrar Académico", e);
        }
    }

    @Test
    void updateTest() {
        clickOn("01");
        sleep(500);
        clickOn("#editButton");
        sleep(500);
        doubleClickOn("#personalNumberTextField").write("00001");
        doubleClickOn("#emailTextField");
        doubleClickOn("#emailTextField").write("kingofpop@gmail.com");

        verifyThat("#personalNumberTextField", hasText("00001"));
        verifyThat("#emailTextField", hasText("kingofpop@gmail.com"));

        clickOn("#saveButton");
        sleep(250);
        push(KeyCode.ENTER);
        sleep(250);
        clickOn("00001");
        sleep(2000);
    }

    @AfterAll
    public static void after() {
        AcademicDAOImplementation dao = new AcademicDAOImplementation();
        try {
            dao.dropAcademic(academicID);
        } catch (SQLException e) {
            logger.error("Error al eliminar académico", e);
        }
    }
}
