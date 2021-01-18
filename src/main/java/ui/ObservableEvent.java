package ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ObservableEvent {

    private final StringProperty name;
    private final StringProperty courseCode;
    private final StringProperty dueDate;
    private final StringProperty id;
    private final DoubleProperty mark;
    private final DoubleProperty weight;

    public ObservableEvent(String name, String id, String courseCode, LocalDateTime dueDate, double mark, double weight) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.courseCode = new SimpleStringProperty(courseCode);
        this.dueDate = new SimpleStringProperty(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        this.mark = new SimpleDoubleProperty(mark);
        this.weight = new SimpleDoubleProperty(weight);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }

    public DoubleProperty markProperty() {
        return mark;
    }

    public DoubleProperty weightProperty() {
        return weight;
    }
}
