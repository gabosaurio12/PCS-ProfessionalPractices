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

public class GUIAcademicsDeleteFailTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIAcademicsDeleteFailTest.class);
    private static int academicID;

    @Override
    public void start(Stage stage) throws Exception {
        registerAcademic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Academic/GUIAcademics.fxml"));
        Parent root = loader.load();
        GUIAcademicsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUIAcademicsDeleteTestFail");
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
    void deleteTestFail() {
        clickOn("#deleteButton");
        sleep(1000);
        push(KeyCode.ENTER);
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
