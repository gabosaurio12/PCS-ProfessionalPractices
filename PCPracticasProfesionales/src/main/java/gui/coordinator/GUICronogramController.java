package gui.coordinator;

import businesslogic.system.SystemDAOImplementation;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.google.gson.Gson;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import model.Event;
import model.Section;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class GUICronogramController {
    private static final Logger logger = LogManager.getLogger(GUICronogramController.class);
    private final GUIUtils utils = new GUIUtils();
    private Calendar<String> calendar;

    @FXML private Rectangle blueFringe;
    @FXML private VBox calendarContainer;
    @FXML private ComboBox<Section> periodComboBox;

    public void init() {
        calendar = new Calendar<>("Cronograma");
        setPeriodComboBox();
        setCalendar();
        loadEventsFromJSON();
    }

    public void openGUIMainPage() {
        utils.openWindow("/fxml/Coordinator/GUIMainPageCoordinator.fxml",
                "Menú Principal", blueFringe);
    }

    public void setPeriodComboBox() {
        List<Section> sections = new ArrayList<>();
        try {
            sections = new SystemDAOImplementation().getSections();
        } catch (SQLException e) {
            utils.createAlert("Error", "Error al recopilar periodos");
            logger.error("Error al recopilar periodos", e);
        }
        if (!sections.isEmpty()) {
            for (Section i : sections) {
                periodComboBox.getItems().add(i);
            }
        } else {
            utils.createAlert("Aviso", "No hay periodos disponibles");
        }
        try {
            periodComboBox.setValue(new SystemDAOImplementation().getCurrentSection());
        } catch (SQLException e) {
            utils.createAlert("Error", "Error al obtener periodos");
            logger.error("Error al obtener periodo actual", e);
        }
    }

    public void setCalendar() {
        calendar.setReadOnly(false);

        CalendarSource source = new CalendarSource("Cronogramas");
        source.getCalendars().add(calendar);

        CalendarView calendarView = new CalendarView();
        calendarView.getCalendarSources().add(source);
        calendarView.setPrefSize(800, 450);
        calendarView.showMonthPage();
        calendarView.setShowToday(true);
        calendarView.setShowToolBar(true);
        calendarContainer.getChildren().add(calendarView);

        calendarView.setEntryFactory(param -> {
            Entry<String> entry = new Entry<>("Nuevo evento");
            entry.changeStartDate(param.getZonedDateTime().toLocalDate());
            entry.changeStartTime(param.getZonedDateTime().toLocalTime());
            entry.setCalendar(calendar);
            calendar.addEntry(entry);
            return entry;
        });
    }

    public void saveEventsInJSON() {
        List<Event> events = new ArrayList<>();

        for (Entry<?> entry : calendar.findEntries("")) {
            Event event = new Event();
            event.setTitle(entry.getTitle());
            event.setDescription(
                    entry.getUserObject() != null ? entry.getUserObject().toString() : "");
            event.setStartDateTime(entry.getStartAsLocalDateTime().toString());
            event.setEndDateTime(entry.getEndAsLocalDateTime().toString());
            events.add(event);
        }

        String jsonPath = "cronogram/calendar".concat(periodComboBox.getValue().getPeriod().concat(".json"));
        File jsonFile = new File(jsonPath);
        jsonFile.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(jsonFile)) {
            Gson gson = new Gson();
            gson.toJson(events, writer);
            openGUIMainPage();
        } catch (IOException e) {
            logger.error("Error al guardar calendario en JSON", e);
            utils.createAlert("Error",
                    "Error al guardar calendario en JSON");
        }
    }

    public void loadEventsFromJSON() {
        String jsonPath = "cronogram/calendar".concat(periodComboBox.getValue().getPeriod().concat(".json"));
        File jsonFile = new File(jsonPath);

        if (!jsonFile.exists()) {
            logger.warn("No se encontró el archivo de eventos: {}", jsonPath);
        } else {
            try (FileReader reader = new FileReader(jsonFile)) {
                Gson gson = new Gson();
                Event[] events = gson.fromJson(reader, Event[].class);

                for (Event event : events) {
                    Entry<String> entry = new Entry<>(event.getTitle());
                    entry.setUserObject(event.getDescription());
                    LocalDateTime start = LocalDateTime.parse(event.getStartDateTime());
                    LocalDateTime end = LocalDateTime.parse(event.getEndDateTime());
                    entry.setInterval(start, end);

                    entry.setCalendar(calendar);
                    calendar.addEntry(entry);
                }
            } catch (IOException | DateTimeParseException e) {
                logger.error("Error al cargar eventos desde JSON", e);
                utils.createAlert("Error",
                "Error al cargar eventos desde JSON");
            }
        }
    }
}
