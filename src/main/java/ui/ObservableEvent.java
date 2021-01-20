package ui;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ObservableEvent {

    private final StringProperty name;
    private final StringProperty courseCode;
    private final StringProperty dueDate;
    private final StringProperty id;
    private final DoubleProperty mark;
    private final DoubleProperty weight;
    private final IntegerProperty remainingDays;

    public ObservableEvent(String name, String id, String courseCode, LocalDateTime dueDate, double mark, double weight) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.courseCode = new SimpleStringProperty(courseCode);
        this.dueDate = new SimpleStringProperty(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        this.mark = new SimpleDoubleProperty(mark);
        this.weight = new SimpleDoubleProperty(weight);
        this.remainingDays = new SimpleIntegerProperty((int) ChronoUnit.DAYS.between(LocalDate.now(), dueDate.toLocalDate()));
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

    public IntegerProperty remainingDaysProperty() {
        return remainingDays;
    }
}
