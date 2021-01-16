package model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {

    private String name;
    private LocalDateTime dueDate;
    private Duration duration;
    private String recurringID;
    private final String UUID;
    private double grade = -1;       // defaults to -1 if unset
    private final List<String> notes = new ArrayList<>();

    public Event(String name) {
        this.name= name;
        this.UUID = java.util.UUID.randomUUID().toString();
    }

    // setters
    void setName(String name) {
        this.name = name;
    }

    void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    void setDuration(Duration duration) {
        this.duration = duration;
    }

    void setRecurringID(String recurringID) {
        this.recurringID = recurringID;
    }

    void setGrade(double grade) {
        this.grade = grade;
    }

    void addNotes(String note) {
        notes.add(note);
    }

    // getters
    String getName() {
        return name;
    }

    LocalDateTime getDueDate() {
        return dueDate;
    }

    Duration getDuration() {
        return duration;
    }

    String getRecurringID() {
        return recurringID;
    }

    /**
     * Returns the actual mark (not percentage) obtained in this assignment.
     *
     * @return the numeric grade obtained in this assignment
     */
    double getGrade() {
        return grade;
    }

    String getID() {
        return UUID;
    }

    List<String> getNotes() {
        return notes;
    }


}
