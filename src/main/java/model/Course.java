package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

    private String name;
    private String code;
    private final List<String> events = new ArrayList<>();     // list of eventIDs
    private final String UUID;
    private double targetGrade = -1;           // default is -1, if not -1 then it has been set
    private final List<String> notes = new ArrayList<>();

    public Course(String name) {
        this.name = name;
        this.UUID = java.util.UUID.randomUUID().toString();
    }

    // setters
    void setName(String name) {
        this.name = name;
    }

    void setCode(String code) {
        this.code = code;
    }

    void addEvent(String eventID) {
        events.add(eventID);
    }

    void removeEvent(String eventID) {
        events.remove(eventID);
    }

    void setTargetGrade(double targetGrade) {
        this.targetGrade = targetGrade;
    }

    void addNotes(String note) {
        notes.add(note);
    }

    // getters
    String getName() {
        return name;
    }

    String getCode() {
        return code;
    }

    List<String> getEvents() {
        return events;
    }

    double getTargetGrade() {
        return targetGrade;
    }

    String getID() {
        return UUID;
    }

    List<String> getNotes() {
        return notes;
    }
}
