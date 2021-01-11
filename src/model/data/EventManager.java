package model.data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class EventManager {

    Map<String, Event> map;

    public void addEvent(String name) {
        Event event = new Event(name);
        map.put(event.getID(), event);
    }

    public void removeEvent(String eventID) {
        map.remove(eventID);
    }

    public void setName(String eventID, String name) {
        map.get(eventID).setName(name);
    }

    public void setDueDate(String eventID, LocalDateTime due) {
        map.get(eventID).setDueDate(due);
    }

    public void setDuration(String eventID, Duration duration) {
        map.get(eventID).setDuration(duration);
    }

    public void setRecurring(String eventID, boolean recurring) {
        map.get(eventID).setRecurring(recurring);
    }

    public String getName(String eventID) {
        return map.get(eventID).getName();
    }

    public LocalDateTime getDueDate(String eventID) {
        return map.get(eventID).getDueDate();
    }

    public Duration getDuration(String eventID) {
        return map.get(eventID).getDuration();
    }

    public boolean getRecurring(String eventID) {
        return map.get(eventID).getRecurring();
    }

    public void importData(Map<String, Event> events) {
        map = events;
    }

    public Map<String, Event> exportData() {
        return map;
    }
}
