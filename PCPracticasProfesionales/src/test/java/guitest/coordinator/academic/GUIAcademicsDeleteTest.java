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
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;


public class GUIAcademicsDeleteTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIAcademicsDeleteTest.class);
    @Override
    public void start(Stage stage) throws Exception {
        registerAcademic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Coordinator/Academic/GUIAcademics.fxml"));
        Parent root = loader.load();
        GUIAcademicsController controller = loader.getController();
        controller.initComponents();
        stage.setTitle("GUIUpdateAcademicTest");
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
            int academicID = dao.registerAcademic(academic);
            int sectionID = 1;
            dao.assignSection(academicID, sectionID);
        } catch (SQLException e) {
            logger.error("Error al registrar Académico", e);
        }
    }

    @Test
    void deleteTest() {
        clickOn("01");
        sleep(500);
        clickOn("#deleteButton");
        sleep(250);
        push(KeyCode.ENTER);
        sleep(2000);
    }
}
