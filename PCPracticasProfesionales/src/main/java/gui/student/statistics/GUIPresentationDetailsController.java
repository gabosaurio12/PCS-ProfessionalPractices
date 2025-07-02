package gui.student.statistics;

import businesslogic.academic.AcademicDAOImplementation;
import gui.util.GUIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import model.Evaluation;
import model.Presentation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUIPresentationDetailsController {
    private static final Logger logger = LogManager.getLogger(GUIPresentationDetailsController.class);
    private final GUIUtils utils = new GUIUtils();

    @FXML private Rectangle blueFringe;
    @FXML private Label dateLabel;
    @FXML private TableView<Evaluation> evaluationsTable;
    @FXML private TableColumn<Evaluation, String> evaluatorColumn;
    @FXML private TableColumn<Evaluation, String> gradeColumn;

    public void init(Presentation presentation) {
        dateLabel.setText(String.valueOf(presentation.getDate()));
        setEvaluationsTable(presentation.getPresentationID());
    }

    public void closeWindow() {
        utils.closeWindow(blueFringe);
    }

    public void setEvaluationsTable(int presentationId) {
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("averageGrade"));
        evaluatorColumn.setCellValueFactory(cellData -> {
            int evaluatorId = cellData.getValue().getAcademicId();
            String name = "";
            try {
                AcademicDAOImplementation dao = new AcademicDAOImplementation();
                name = dao.getAcademicNameByID(evaluatorId);
            } catch (SQLException e) {
                utils.createAlert("Error",
                        "Hubo un error al recuperar a los evaluadores");
                logger.error("Error al recuperar evaluador", e);
            }
            return new SimpleStringProperty(name);
        });

        List<Evaluation> evaluations = new ArrayList<>();
        try {
            AcademicDAOImplementation dao = new AcademicDAOImplementation();
            evaluations = dao.getEvaluationsFromPresentation(presentationId);
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Hubo un error al recuperar las evaluaciones");
            logger.error("Error al recuperar las evaluaciones", e);
        }

        if (!evaluations.isEmpty()) {
            ObservableList<Evaluation> evaluationsList = FXCollections.observableArrayList(evaluations);
            evaluationsTable.setItems(evaluationsList);
        } else {
            evaluationsTable.setItems(null);
            utils.createAlert("Aviso",
                    "Aún no se llevan a cabo evaluaciones");
        }
    }

}
