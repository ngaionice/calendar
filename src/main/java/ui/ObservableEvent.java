package ui;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ObservableEvent {

    private StringProperty name;
    private final StringProperty courseCode;
    private final StringProperty courseID;
    private StringProperty dueDate;
    private final StringProperty id;
    private DoubleProperty grade;
    private DoubleProperty weight;
    private IntegerProperty remainingDays;

    public ObservableEvent(String name, String id, String courseCode, String courseID, LocalDateTime dueDate, double grade, double weight) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.courseCode = new SimpleStringProperty(courseCode);
        this.courseID = new SimpleStringProperty(courseID);
        this.dueDate = new SimpleStringProperty(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        this.grade = new SimpleDoubleProperty(grade);
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

    public StringProperty courseIDProperty() {
        return courseID;
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }

    public DoubleProperty gradeProperty() {
        return grade;
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public IntegerProperty remainingDaysProperty() {
        return remainingDays;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate.set(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        this.remainingDays.set((int) ChronoUnit.DAYS.between(LocalDate.now(), dueDate.toLocalDate()));
    }

    public void setGrade(double grade) {
        this.grade.setValue(grade);
    }

    public void setWeight(double weight) {
        this.weight.setValue(weight);
    }
}
