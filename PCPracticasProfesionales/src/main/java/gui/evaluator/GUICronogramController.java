package gui.evaluator;

import businesslogic.system.SystemDAOImplementation;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.google.gson.Gson;
import gui.util.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import model.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class GUICronogramController {
    private static final Logger logger = LogManager.getLogger(GUICronogramController.class);
    private final GUIUtils utils = new GUIUtils();
    private Calendar<String> calendar;

    @FXML
    private Rectangle blueFringe;
    @FXML private VBox calendarContainer;

    public void init() {
        calendar = new Calendar<>("Cronograma");
        setCalendar();
        loadEventsFromJSON();
    }

    public void openGUIMainPage() {
        utils.openWindow("/fxml/Evaluator/GUIMainPageEvaluator.fxml",
                "Menú Principal", blueFringe);
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

    public void loadEventsFromJSON() {
        String period = "";
        try {
            period = new SystemDAOImplementation().getCurrentSection().getPeriod();
        } catch (SQLException e) {
            utils.createAlert("Error",
                    "Hubo un error al recuperar el periodo actual");
            logger.error("Error al recuperar periodo actual");
            openGUIMainPage();
        }
        String jsonPath = "cronogram/calendar".concat(period).concat(".json");
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
