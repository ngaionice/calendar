package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class EventManager {

    private Map<String, Event> map = new HashMap<>(10);
    private Map<String, String> recurringMap = new HashMap<>(10);   // key is recurringID, value is name

    // note that there are 2 types of ID here: recurringID and eventID;
    // a recurringID is a variable in an Event, and is used for grouping purposes;
    // eventID is the unique identifier for an Event

    // recurringID can be used to calculate 'best 3 of 5 assignments' etc purposes in the future

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
    String addRecurringEvent(String name, int suffixStart, int occurrences, LocalDateTime startDate, LocalDate skipDate) {
        String recurringID = UUID.randomUUID().toString();
        recurringMap.put(recurringID, name);
        LocalDate currDate = startDate.toLocalDate();
        LocalTime dueTime = startDate.toLocalTime();
        for (int i = suffixStart; i <= occurrences + suffixStart; i++) {
            if (currDate != skipDate) {
                String eventID = addEvent(name + " " + i);
                setDueDate(eventID, LocalDateTime.of(currDate, dueTime));
                setRecurringID(eventID, recurringID);
            } else {
                i--;
            }
            currDate = currDate.plusDays(7);
        }
        return recurringID;
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

    private void setRecurringID(String eventID, String recurringID) {
        map.get(eventID).setRecurringID(recurringID);
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

    String getRecurrence(String eventID) {
        return map.get(eventID).getRecurringID();
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

    String getRecurringEventName(String recurringID) {
        return recurringMap.get(recurringID);
    }

    /**
     * Returns the event IDs associated with the input recurring event ID.
     *
     * @param recurringID ID of the recurring event
     * @return list of event IDs
     */
    List<String> getRecurringEventInstances(String recurringID) {
        List<String> eventIDs = new ArrayList<>();
        for (Event event : map.values()) {
            if (event.getRecurringID() != null && event.getRecurringID().equals(recurringID)) {
                eventIDs.add(event.getID());
            }
        }
        return eventIDs;
    }

    // data import/export

    void importData(Map<String, Event> events) {
        map = events;
    }

    Map<String, Event> exportData() {
        return map;
    }
}
