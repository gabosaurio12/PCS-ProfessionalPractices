package gui.coordinator.project;

import businesslogic.system.SystemDAOImplementation;
import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import businesslogic.project.ProjectDAOImplementation;
import businesslogic.projectresponsible.ProjectResponsibleDAOImplementation;
import gui.util.GUIUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.List;
import java.util.Objects;

public class GUIRegisterProjectController {
    private final GUIUtils utils = new GUIUtils();
    private static final Logger logger = LogManager.getLogger(GUIRegisterProjectController.class);
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

    public void initComponents(int sectionID) {
        this.sectionID = sectionID;
        setOrganizationComboBox();
        setProjectResponsibleComboBox();
        setOpenSpotsSpinner();
        setFileButton();
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

    public void setOrganizationComboBox() {
        try {
            List<LinkedOrganization> organizations = new
                    LinkedOrganizationDAOImplementation().getLinkedOrganizations();
            organizationComboBox.setItems(FXCollections.observableArrayList(organizations));
            organizationComboBox.setValue(organizations.getFirst());
        } catch (SQLException e) {
            logger.error("Error al cargar organizaciones", e);
            utils.createAlert("Error", "No se pudieron cargar las organizaciones.");
            openGUIProjects();
        }
    }

    public void setProjectResponsibleComboBox() {
        try {
            ProjectResponsibleDAOImplementation dao = new ProjectResponsibleDAOImplementation();
            List<ProjectResponsible> responsibles = dao.getProjectResponsibles(
                    new SystemDAOImplementation().getSectionByID(sectionID).getPeriod());
            projectResponsibleComboBox.setItems(FXCollections.observableArrayList(responsibles));
            projectResponsibleComboBox.setValue(responsibles.getFirst());
        } catch (SQLException e) {
            logger.error("Error al recuperar responsables de proyectos");
            utils.createAlert("Error",
                    "Error al recuperar responsables de proyectos");
            openGUIProjects();
        }
    }

    public void setOpenSpotsSpinner() {
        openSpotsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));
    }

    public void getTextFields(Project project) {
        project.setTitle(titleTextField.getText());
        project.setCategory(categoryTextField.getText());
        if (beginningDatePicker.getValue() == null) {
            project.setBeginningDate(new Date(0));
        } else {
            project.setBeginningDate(Date.valueOf(beginningDatePicker.getValue()));
        }
        if (endingDatePicker.getValue() == null) {
            project.setEndingDate(new Date(0));
        } else {
            project.setEndingDate(Date.valueOf(endingDatePicker.getValue()));
        }
        project.setStatus(statusTextField.getText());
        project.setOpenSpots(openSpotsSpinner.getValue());
        project.setLinkedOrganizationId(organizationComboBox.getValue().getLinkedOrganizationID());
        project.setProjectResponsibleId(projectResponsibleComboBox.getValue().getProjectResponsibleID());
        project.setDocumentInfoPath(filePathTextField.getText());
    }

    public void showInvalidData(boolean[] dataFlags) {
        int TITLE_FLAG = 0;
        if (!dataFlags[TITLE_FLAG]) {
            titleLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int OPEN_SPOTS_FLAG = 1;
        if (!dataFlags[OPEN_SPOTS_FLAG]) {
            openSpotsLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int STATUS_FLAG = 2;
        if (!dataFlags[STATUS_FLAG]) {
            statusLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int PROJECT_RESPONSIBLE_FLAG = 3;
        if (!dataFlags[PROJECT_RESPONSIBLE_FLAG]) {
            projectResponsibleLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int CATEGORY_FLAG = 4;
        if (!dataFlags[CATEGORY_FLAG]) {
            categoryLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int BEGINNING_DATE_FLAG = 5;
        if (!dataFlags[BEGINNING_DATE_FLAG]) {
            beginningDateLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int ENDING_DATE_FLAG = 6;
        if (!dataFlags[ENDING_DATE_FLAG]) {
            endingDateLabel.setTextFill(utils.ERROR_COLOUR);
        }
        int ORGANIZATION_FLAG = 7;
        if (!dataFlags[ORGANIZATION_FLAG]) {
            organizationLabel.setTextFill(utils.ERROR_COLOUR);
        }
    }

    public void restartLabels() {
        titleLabel.setTextFill(utils.DEFAULT_COLOUR);
        openSpotsLabel.setTextFill(utils.DEFAULT_COLOUR);
        statusLabel.setTextFill(utils.DEFAULT_COLOUR);
        projectResponsibleLabel.setTextFill(utils.DEFAULT_COLOUR);
        categoryLabel.setTextFill(utils.DEFAULT_COLOUR);
        beginningDateLabel.setTextFill(utils.DEFAULT_COLOUR);
        endingDateLabel.setTextFill(utils.DEFAULT_COLOUR);
        organizationLabel.setTextFill(utils.DEFAULT_COLOUR);
    }

    @FXML
    public void registerProject() {
        restartLabels();
        Project project = new Project();
        getTextFields(project);
        boolean[] validationFlags = project.validateData();
        if (validationFlags[validationFlags.length - 1]) {
            try {
                ProjectDAOImplementation dao = new ProjectDAOImplementation();
                int projectID = dao.registerProject(project);
                dao.assignProjectSection(projectID, sectionID);
                utils.createAlert("Éxito", "Proyecto se registró correctamente.");
                closeWindow();
                openGUIProjects();
            } catch (Exception e) {
                logger.error("Error al registrar proyecto", e);
                utils.createAlert("Error", "No se pudo registrar el proyecto.");
            }
        } else {
            utils.createAlert("Campos inválidos",
                    "Hay campos ingresados inválidos");
            showInvalidData(validationFlags);
        }
    }

    public void openGUIProjects() {
        GUIProjectsController controller = utils.openWindow(
                "/fxml/Coordinator/Project/GUIProjects.fxml", "Proyectos",
                GUIProjectsController.class, blueFringe);
        controller.init();
    }

    private void closeWindow() {
        utils.closeWindow(blueFringe);
    }

}


