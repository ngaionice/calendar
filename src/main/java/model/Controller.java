package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {

    CourseManager courses = new CourseManager();
    EventManager events = new EventManager();
    LocalGateway gateway = new LocalGateway();

    public void importData(String coursePath, String eventPath) {
        this.courses.importData(gateway.importCourseData(coursePath));
        this.events.importData(gateway.importEventData(eventPath));
    }

    public void exportData(String coursePath, String eventPath) {
        gateway.exportData(coursePath, courses.exportData());
        gateway.exportData(eventPath, events.exportData());
    }

    // setters - courses

    public String addCourse(String name) {
        return courses.addCourse(name);
    }

    public void removeCourse(String courseID) {
        courses.removeCourse(courseID);
    }

    /**
     * If the course breakdown values sums up to 100 (or within a tolerance of 0.01), sets the grade breakdown of the course, and returns true.
     * Returns false otherwise.
     *
     * @param courseID  course ID of the course
     * @param breakdown a map with event ID as keys, and double as values representing the proportion (multiplied by 100).
     * @return true if the breakdown was set
     */
    public boolean setCourseBreakdown(String courseID, Map<String, Double> breakdown) {
        return courses.setBreakdown(courseID, breakdown);
    }

    public void setCourseName(String courseID, String name) {
        courses.setName(courseID, name);
    }

    public void addEventToCourse(String courseID, String eventID, boolean recurring) {
        if (recurring) {
            courses.addRecurring(courseID, eventID);
        } else {
            courses.addOneTime(courseID, eventID);
        }
    }

    public void removeEventFromCourse(String courseID, String eventID) {
        courses.removeRecurring(courseID, eventID);
        courses.removeOneTime(courseID, eventID);
    }

    public boolean setCourseTarget(String courseID, double target) {
        return courses.setTargetGrade(courseID, target);
    }

    public void addCourseNotes(String courseID, String notes) {
        courses.addNotes(courseID, notes);
    }

    // setters - events

    public String addEvent(String name) {
        return events.addEvent(name);
    }

    public void removeEvent(String eventID) {
        events.removeEvent(eventID);
    }

    public void setEventName(String eventID, String name) {
        events.setName(eventID, name);
    }

    public void setEventDueDate(String eventID, LocalDateTime due) {
        events.setDueDate(eventID, due);
    }

    public void setEventDuration(String eventID, Duration duration) {
        events.setDuration(eventID, duration);
    }

    public void setEventRecurring(String eventID, boolean recurring) {
        events.setRecurring(eventID, recurring);
    }

    public boolean setEventGrade(String eventID, double grade) {
        return events.setGrade(eventID, grade);
    }

    public void addEventNotes(String eventID, String notes) {
        events.addNotes(eventID, notes);
    }
    // getters - courses

    /**
     * Returns the IDs and names of all courses.
     *
     * @return a map containing all stored courses; key is course ID, value is course name
     */
    public Map<String, String> getAllCourseInfo() {
        return courses.getAllCourseInfo();
    }

    public String getCourseName(String courseID) {
        return courses.getName(courseID);
    }

    public Map<String, Double> getCourseBreakdown(String courseID) {
        return courses.getBreakdown(courseID);
    }

    /**
     * Returns a list of recurring events of the specified course.
     *
     * @param courseID ID of the course
     * @return         list of IDs of recurring events that are of the course
     */
    public List<String> getRecurringCourseEvents(String courseID) {
        return courses.getRecurring(courseID);
    }

    /**
     * Returns a list of one time events of the specified course.
     *
     * @param courseID ID of the course
     * @return         list of IDs of one time events that are of the course
     */
    public List<String> getOneTimeCourseEvents(String courseID) {
        return courses.getOneTime(courseID);
    }

    public double getCourseTargetGrade(String courseID) {
        return courses.getTargetGrade(courseID);
    }

    public List<String> getCourseNotes(String courseID) {
        return courses.getNotes(courseID);
    }

    public double getCourseAverage(String courseID) {
        Map<String, Double> breakdown = getCourseBreakdown(courseID);
        double assessed = 0;
        double assessedMax = 0;

        for (String eventID: breakdown.keySet()) {
            double weight = breakdown.get(eventID);
            double obtained = events.getGrade(eventID);
            if (obtained != -1) {
                assessed += obtained;
                assessedMax += weight;
            }
        }

        if (assessedMax != 0) {
            return Math.round(100 * assessed/assessedMax);
        }
        return -1;
    }

    // getters - events

    /**
     * Returns a map containing all events' name, event ID, and due date.
     *
     * @return a map with event ID as key, and a string array of format [event name, event due date]
     */
    public Map<String, String[]> getAllEventInfo() {
        return events.getAllEventInfo();
    }

    public String getEventName(String eventID) {
        return events.getName(eventID);
    }

    public LocalDateTime getEventDueDate(String eventID) {
        return events.getDueDate(eventID);
    }

    public Duration getEventDuration(String eventID) {
        return events.getDuration(eventID);
    }

    public boolean getEventRecurrence(String eventID) {
        return events.getRecurrence(eventID);
    }

    public double getEventGrade(String eventID) {
        return events.getGrade(eventID);
    }

    public List<String> getEventNotes(String eventID) {
        return events.getNotes(eventID);
    }
}
