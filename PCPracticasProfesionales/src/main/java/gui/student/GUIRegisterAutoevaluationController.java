package gui.student;

import businesslogic.student.StudentDAOImplementation;
import dataaccess.system.UserDataConfig;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Properties;

public class GUIRegisterAutoevaluationController {
    private static final Logger logger = LogManager.getLogger(GUIRegisterAutoevaluationController.class);
    private final GUIUtils utils = new GUIUtils();
    private int studentID;

    @FXML private Rectangle blueFringe;
    @FXML private Label gradeLabel;
    @FXML private Spinner<Double> autoEvaluationGradeSpinner;

    public void init() {
        Properties properties = UserDataConfig.loadProperties();
        studentID = Integer.parseInt(properties.getProperty("user.id"));
        Double grade = 1.0;
        try {
            grade = new StudentDAOImplementation().getAutoevaluationGradeByID(studentID);
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Hubo un error al recuperar la calificación");
            logger.error("Error al recuperar calificacion", e);
            closeWindow();
        }
        autoEvaluationGradeSpinner.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 50, 50));
        autoEvaluationGradeSpinner.getValueFactory().setValue(grade);
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void saveGrade() {
        gradeLabel.setTextFill(Paint.valueOf("#000000"));
        Double grade = autoEvaluationGradeSpinner.getValue();
        if (grade > 0 && grade < 51) {
            try {
                new StudentDAOImplementation().updateAutoevaluationGradeById(grade, studentID);
                utils.createAlert("Éxito",
                        "¡Se registró la actualización con éxito!");
            } catch (SQLException e) {
                utils.createAlert("Error",
                        "Hubo un error al registrar la calificación");
                logger.error("Error al registrar calificacion", e);
            } finally {
                closeWindow();
            }
        } else {
            utils.createAlert("Campos inválidos",
                    "La calificación debe ser mayor a 0 y menor o igual a 10");
            gradeLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
    }

}
