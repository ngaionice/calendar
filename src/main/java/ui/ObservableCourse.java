package ui;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ObservableCourse {

    private final StringProperty name;
    private final DoubleProperty avg;
    private final StringProperty nextEvent;
    private final StringProperty nextDue;
    private final StringProperty id;

    public ObservableCourse(String name, String id, double avg, String nextEvent, LocalDateTime nextDue) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.avg = new SimpleDoubleProperty(avg);
        this.nextEvent = new SimpleStringProperty(nextEvent);
        this.nextDue = new SimpleStringProperty(nextDue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    public StringProperty nameProperty() {
        return name;
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
