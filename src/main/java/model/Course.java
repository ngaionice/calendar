package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Course implements Serializable {

    private String name;
    private Map<String, Double> breakdown;     // note that the integer is in %, and strings are the assignments and tests' UUID
    private final List<String> recurring = new ArrayList<>();   // list of recurringIDs
    private final List<String> oneTime = new ArrayList<>();     // list of eventIDs
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

    void setBreakdown(Map<String, Double> breakdown) {
        this.breakdown = breakdown;
    }

    void addRecurring(String recurringID) {
        recurring.add(recurringID);
    }

    void removeRecurring(String recurringID) {
        this.recurring.remove(recurringID);
    }

    void addOneTime(String eventID) {
        oneTime.add(eventID);
    }

    void removeOneTime(String eventID) {
        oneTime.remove(eventID);
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

    Map<String, Double> getBreakdown() {
        return breakdown;
    }

    List<String> getRecurring() {
        return recurring;
    }

    List<String> getOneTime() {
        return oneTime;
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
