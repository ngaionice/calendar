package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private Map<String, Event> map;

    // setters

    void addEvent(String name) {
        Event event = new Event(name);
        map.put(event.getID(), event);
    }

    void removeEvent(String eventID) {
        map.remove(eventID);
    }

    void setName(String eventID, String name) {
        map.get(eventID).setName(name);
    }

    void setDueDate(String eventID, LocalDateTime due) {
        map.get(eventID).setDueDate(due);
    }

    void setDuration(String eventID, Duration duration) {
        map.get(eventID).setDuration(duration);
    }

    void setRecurring(String eventID, boolean recurring) {
        map.get(eventID).setRecurring(recurring);
    }

    boolean setGrade(String eventID, double grade) {
        if (grade >= 0) {
            map.get(eventID).setGrade(grade);
            return true;
        }
        return false;
    }

    void addNotes(String eventID, String notes) {
        map.get(eventID).addNotes(notes);
    }

    // getters

    String getName(String eventID) {
        return map.get(eventID).getName();
    }

    LocalDateTime getDueDate(String eventID) {
        return map.get(eventID).getDueDate();
    }

    Duration getDuration(String eventID) {
        return map.get(eventID).getDuration();
    }

    boolean getRecurrence(String eventID) {
        return map.get(eventID).getRecurring();
    }

    double getGrade(String eventID) {
        return map.get(eventID).getGrade();
    }

    Map<String, String[]> getAllEventInfo() {
        Map<String, String[]> info = new HashMap<>(100);
        for (String eventID: map.keySet()) {
            info.put(eventID, new String[] {map.get(eventID).getName(), map.get(eventID).getDueDate().toString()});
        }
        return info;
    }

    List<String> getNotes(String eventID) {
        return map.get(eventID).getNotes();
    }

    // data import/export

    void importData(Map<String, Event> events) {
        map = events;
    }

    Map<String, Event> exportData() {
        return map;
    }
}
