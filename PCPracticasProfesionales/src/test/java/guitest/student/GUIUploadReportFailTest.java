package guitest.student;

import businesslogic.student.StudentDAOImplementation;
import gui.student.GUIUploadReportController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Report;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class GUIUploadReportFailTest extends ApplicationTest {
    private static final Logger logger = LogManager.getLogger(GUIUploadReportFailTest.class);

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Student/GUIUploadReport.fxml"));
        Parent root = loader.load();
        GUIUploadReportController controller = loader.getController();
        controller.init();
        stage.setTitle("GUIUploadReportTest");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void uploadReportTest() {
        DatePicker datePicker = lookup("#datePicker").queryAs(DatePicker.class);
        interact(() -> datePicker.setValue(LocalDate.of(2025, 6, 23)));
        clickOn("#typeComboBox");
        sleep(500);
        clickOn("Mensual");
        clickOn("#hoursSpinner");
        doubleClickOn("#hoursSpinner");
        write("0");

        Assertions.assertEquals(LocalDate.of(2025, 6, 23), datePicker.getValue());
        verifyThat("#filePathTextField", hasText(""));

        clickOn("#uploadButton");
        sleep(250);
        push(KeyCode.ENTER);
        sleep(2000);
    }

    @AfterAll
    public static void after() {
        StudentDAOImplementation dao = new StudentDAOImplementation();
        List<Report> reports = new ArrayList<>();
        try {
            reports = dao.getReports();
        } catch (SQLException e) {
            logger.error("Error al recuperar reportes", e);
        }
        Report report = reports.getLast();
        try {
            dao.deleteReport(report.getId());
        } catch (SQLException e) {
            logger.error("Error al borrar reporte", e);
        }
    }
}
