package model.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Course implements Serializable {

    private String name;
    private Map<String, Integer> breakdown;     // note that the integer is in %, and strings are the assignments and tests' UUID
    private List<String> recurring;
    private List<String> oneTime;
    private final String UUID;

    public Course(String name) {
        this.name = name;
        this.UUID = java.util.UUID.randomUUID().toString();
    }

    // setters
    void setName(String name) {
        this.name = name;
    }

    void setBreakdown(Map<String, Integer> breakdown) {
        this.breakdown = breakdown;
    }

    void addRecurring(String eventID) {
        recurring.add(eventID);
    }

    void removeRecurring(String eventID) {
        recurring.remove(eventID);
    }

    void addOneTime(String eventID) {
        oneTime.add(eventID);
    }

    void removeOneTime(String eventID) {
        oneTime.remove(eventID);
    }

    // getters
    String getName() {
        return name;
    }

    Map<String, Integer> getBreakdown() {
        return breakdown;
    }

    List<String> getRecurring() {
        return recurring;
    }

    List<String> getOneTime() {
        return oneTime;
    }

    String getID() {
        return UUID;
    }
}
