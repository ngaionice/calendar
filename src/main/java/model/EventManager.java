package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class EventManager {

    private Map<String, Event> map;

    // note that there are 2 types of ID here: recurringID and eventID;
    // eventID is the unique identifier for an Event

    // setters

    String addEvent(String name) {
        Event event = new Event(name);
        map.put(event.getID(), event);
        return event.getID();
    }

    /**
     * Adds a recurring event (weekly), with 1 optional week that gets skipped.
     *
     * @param name        name of the event
     * @param suffixStart the starting number appended after the suffix, e.g. 2 in 'Module 2'
     * @param occurrences number of occurrences of the event
     * @param startDate   the first LocalDateTime that this event happens
     * @param skipDate    the LocalDate that this event gets skipped (does not occur) on
     * @return the recurring ID
     */
    List<String> addRecurringEvent(String name, int suffixStart, int occurrences, LocalDateTime startDate, LocalDate skipDate) {
        List<String> eventIDs = new ArrayList<>();
        LocalDate currDate = startDate.toLocalDate();
        LocalTime dueTime = startDate.toLocalTime();
        for (int i = suffixStart; i < occurrences + suffixStart; i++) {
            if (!currDate.equals(skipDate)) {
                String eventID = addEvent(name + " " + i);
                setDueDate(eventID, LocalDateTime.of(currDate, dueTime));
                eventIDs.add(eventID);
            } else {
                i--;
            }
            currDate = currDate.plusDays(7);
        }
        return eventIDs;
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

    boolean setGrade(String eventID, double grade) {
        if (grade >= 0) {
            map.get(eventID).setGrade(grade);
            return true;
        }
        return false;
    }

    boolean setWeight(String eventID, double weight) {
        if (weight >= 0) {
            map.get(eventID).setWeight(weight);
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

    double getGrade(String eventID) {
        return map.get(eventID).getGrade();
    }

    double getWeight(String eventID) {
        return map.get(eventID).getWeight();
    }

    Map<String, String[]> getAllEventInfo() {
        Map<String, String[]> info = new HashMap<>(100);
        for (String eventID : map.keySet()) {
            info.put(eventID, new String[]{map.get(eventID).getName(), map.get(eventID).getDueDate().toString(),
                    String.valueOf(map.get(eventID).getGrade()), String.valueOf(map.get(eventID).getWeight())});
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
