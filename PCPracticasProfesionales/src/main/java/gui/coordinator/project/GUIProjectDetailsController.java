package gui.coordinator.project;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import businesslogic.project.ProjectDAOImplementation;
import businesslogic.projectresponsible.ProjectResponsibleDAOImplementation;
import gui.util.GUIUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.LinkedOrganization;
import model.Project;
import model.ProjectResponsible;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GUIProjectDetailsController {
    private static final Logger logger = LogManager.getLogger(GUIProjectDetailsController.class);
    private final GUIUtils utils = new GUIUtils();
    private int projectID;
    private int sectionID;

    @FXML private Rectangle blueFringe;
    @FXML private TextField titleTextField;
    @FXML private TextField categoryTextField;
    @FXML private DatePicker beginningDatePicker;
    @FXML private DatePicker endingDatePicker;
    @FXML private TextField statusTextField;
    @FXML private Spinner<Integer> openSpotsSpinner;
    @FXML private ComboBox<LinkedOrganization> organizationComboBox;
    @FXML private ComboBox<ProjectResponsible> projectResponsibleComboBox;
    @FXML private Button fileButton;
    @FXML private TextField filePathTextField;

    @FXML private Label titleLabel;
    @FXML private Label openSpotsLabel;
    @FXML private Label statusLabel;
    @FXML private Label projectResponsibleLabel;
    @FXML private Label categoryLabel;
    @FXML private Label beginningDateLabel;
    @FXML private Label endingDateLabel;
    @FXML private Label organizationLabel;

    public void init(Project project, int sectionID) {
        projectID = project.getProjectID();
        this.sectionID = sectionID;
        setProjectResponsibleComboBox();
        setLinkedOrganizationsBox();
        setFileButton();
        setOpenSpotsSpinner(project.getOpenSpots());
        fillTextFields(project);
    }

    public void setFileButton() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/fileicon.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        fileButton.setGraphic(imageView);
    }

    public void searchProjectFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(fileButton.getScene().getWindow());
        if (selectedFile != null) {
            filePathTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    public void setOpenSpotsSpinner(int spots) {
        openSpotsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));
        openSpotsSpinner.getValueFactory().setValue(spots);
    }

    public void fillTextFields(Project project) {
        titleTextField.setText(project.getTitle());
        openSpotsSpinner.getValueFactory().setValue(project.getOpenSpots());
        statusTextField.setText(project.getStatus());
        categoryTextField.setText(project.getCategory());
        beginningDatePicker.setValue(project.getBeginningDate().toLocalDate());
        endingDatePicker.setValue(project.getEndingDate().toLocalDate());
        filePathTextField.setText(project.getDocumentInfoPath());
        try {
            projectResponsibleComboBox.setValue(
                    new ProjectResponsibleDAOImplementation().getProjectResponsibleByID(
                            project.getProjectResponsibleId()
                    )
            );
            organizationComboBox.setValue(
                    new LinkedOrganizationDAOImplementation().getLinkedOrganizationById(
                            project.getLinkedOrganizationId()));
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Error al recopilar datos");
            logger.error("Error al buscar Organización vinculada");
            openGUIProjects();
        }
    }

    public void setLinkedOrganizationsBox() {
        List<LinkedOrganization> organizations = new ArrayList<>();
        try {
            organizations = new LinkedOrganizationDAOImplementation().getLinkedOrganizations();
        } catch (SQLException e) {
            logger.error("Error al recopilar academicos", e);
        }

        for (LinkedOrganization i : organizations) {
            organizationComboBox.getItems().add(i);
        }
    }

    public void setProjectResponsibleComboBox() {
        try {
            ProjectResponsibleDAOImplementation dao = new ProjectResponsibleDAOImplementation();
            List<ProjectResponsible> responsibles = dao.getProjectResponsibles(
                    new SystemDAOImplementation().getSectionByID(sectionID).getPeriod());
            projectResponsibleComboBox.setItems(FXCollections.observableArrayList(responsibles));
        } catch (SQLException e) {
            logger.error("Error al recuperar responsables de proyectos");
            utils.createAlert("Error",
                    "Error al recuperar responsables de proyectos");
        }
    }

    public void getTextFields(Project project) {
        project.setTitle(titleTextField.getText());
        project.setCategory(categoryTextField.getText());
        project.setBeginningDate(Date.valueOf(beginningDatePicker.getValue()));
        project.setEndingDate(Date.valueOf(endingDatePicker.getValue()));
        project.setStatus(statusTextField.getText());
        project.setOpenSpots(openSpotsSpinner.getValue());
        project.setLinkedOrganizationId(organizationComboBox.getValue().getLinkedOrganizationID());
        project.setProjectResponsibleId(projectResponsibleComboBox.getValue().getProjectResponsibleID());
        project.setDocumentInfoPath(filePathTextField.getText());
    }

    public void openGUIProjects() {
        GUIProjectsController controller = utils.openWindow(
                "/fxml/Coordinator/Project/GUIProjects.fxml", "Proyectos",
                GUIProjectsController.class, blueFringe);
        controller.init();
    }

    public void showInvalidData(boolean[] dataFlags) {
        if (!dataFlags[0]) {
            titleLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!dataFlags[1]) {
            openSpotsLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!dataFlags[2]) {
            statusLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!dataFlags[3]) {
            projectResponsibleLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!dataFlags[4]) {
            categoryLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!dataFlags[5]) {
            beginningDateLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!dataFlags[6]) {
            endingDateLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
        if (!dataFlags[7]) {
            organizationLabel.setTextFill(Paint.valueOf("#dd0000"));
        }
    }

    public void restartLabels() {
        titleLabel.setTextFill(Paint.valueOf("#000000"));
        openSpotsLabel.setTextFill(Paint.valueOf("#000000"));
        statusLabel.setTextFill(Paint.valueOf("#000000"));
        projectResponsibleLabel.setTextFill(Paint.valueOf("#000000"));
        categoryLabel.setTextFill(Paint.valueOf("#000000"));
        beginningDateLabel.setTextFill(Paint.valueOf("#000000"));
        endingDateLabel.setTextFill(Paint.valueOf("#000000"));
        organizationLabel.setTextFill(Paint.valueOf("#000000"));
    }

    public void saveChanges() {
        restartLabels();
        Project project = new Project();
        getTextFields(project);
        boolean[] validationFlags = project.validateData();
        if (validationFlags[8]) {
            ProjectDAOImplementation dao = new ProjectDAOImplementation();
            try {
                project.setProjectID(projectID);
                dao.updateProjectById(project);
                utils.createAlert("¡Actualización exitosa!",
                        "El Proyecto fue actualizado correctamente");
                openGUIProjects();
            } catch (SQLException e) {
                utils.createAlert("¡ERROR al actualizar Proyecto",
                        "El Proyecto no pudo actualizarse correctamente");
                logger.error("Error al actualizar proyecto", e);
            }
        } else {
            utils.createAlert("Campos inválidos",
                    "Hay campos ingresados inválidos");
            showInvalidData(validationFlags);
        }

    }

    public void closeSession() {
        utils.closeSession(blueFringe);
    }
}
