package model.data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public class Event implements Serializable {

    private String name;
    private LocalDateTime dueDate;
    private Duration duration;
    private boolean recurring;
    private final String UUID;

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

    void setRecurring(boolean recurring) {
        this.recurring = recurring;
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

    boolean getRecurring() {
        return recurring;
    }

    String getID() {
        return UUID;
    }


}
