package ui;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ObservableCourse {

    private final StringProperty name;
    private final StringProperty code;
    private final DoubleProperty avg;
    private final StringProperty nextEvent;
    private final StringProperty nextDue;
    private final StringProperty id;

    public ObservableCourse(String name, String code, String id, double avg, String nextEvent, Optional<LocalDateTime> nextDue) {
        this.name = new SimpleStringProperty(name);
        this.code = new SimpleStringProperty(code);
        this.id = new SimpleStringProperty(id);
        this.avg = new SimpleDoubleProperty(avg);
        this.nextEvent = new SimpleStringProperty(nextEvent);
        this.nextDue = new SimpleStringProperty(nextDue.map(localDateTime -> localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).orElse("N/A"));
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty codeProperty() {
        return code;
    }

    public StringProperty idProperty() {
        return id;
    }

    public DoubleProperty avgProperty() {
        return avg;
    }

    public StringProperty nextEventProperty() {
        return nextEvent;
    }

    public StringProperty nextDueProperty() {
        return nextDue;
    }
}
