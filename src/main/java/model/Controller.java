package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Controller {

    CourseManager courses = new CourseManager();
    EventManager events = new EventManager();
    LocalGateway gateway = new LocalGateway();

    public void importData(String coursePath, String archivedCoursesPath, String eventPath) {
        this.courses.importData(gateway.importCourseData(coursePath, archivedCoursesPath));
        this.events.importData(gateway.importEventData(eventPath));
    }

    public void exportData(String coursePath, String archivedCoursesPath, String eventPath) {
        gateway.exportData(coursePath, courses.exportCourses());
        gateway.exportData(archivedCoursesPath, courses.exportArchivedCourses());
        gateway.exportData(eventPath, events.exportData());
    }

    // setters - courses

    public String addCourse(String name) {
        return courses.addCourse(name);
    }

    public void archiveCourse(String courseID) {
        courses.archiveCourse(courseID);
    }

    public void unArchiveCourse(String courseID) {
        courses.unArchiveCourse(courseID);
    }

    public void removeCourse(String courseID) {
        courses.removeCourse(courseID);
    }

    public void setCourseName(String courseID, String name) {
        courses.setName(courseID, name);
    }

    public void setCourseCode(String courseID, String courseCode) {
        courses.setCode(courseID, courseCode);
    }

    public void addEventToCourse(String courseID, String eventID) {
        courses.addOneTime(courseID, eventID);
    }

    public void removeEventFromCourse(String courseID, String eventID) {
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

    public List<String> addRecurringEvent(String name, int suffixStart, int occurrences, LocalDateTime startDate, LocalDate skipDate) {
        return events.addRecurringEvent(name, suffixStart, occurrences, startDate, skipDate);
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

    public boolean setEventGrade(String eventID, double grade) {
        return events.setGrade(eventID, grade);
    }

    public boolean setEventWeight(String eventID, double grade) {
        return events.setWeight(eventID, grade);
    }

    public void addEventNotes(String eventID, String notes) {
        events.addNotes(eventID, notes);
    }
    // getters - courses

    /**
     * Returns the IDs and codes of all courses.
     *
     * @return a map containing all stored courses; key is course ID, value is course code
     */
    public Map<String, String> getAllCourseInfo(boolean isArchived) {
        return courses.getAllCourseInfo(isArchived);
    }

    public String getCourseName(String courseID, boolean isArchived) {
        return courses.getName(courseID, isArchived);
    }

    public String getCourseCode(String courseID, boolean isArchived) {
        return courses.getCode(courseID, isArchived);
    }

    /**
     * Returns a list of one time events' IDs of the specified course.
     *
     * @param courseID ID of the course
     * @return list of IDs of one time events that are of the course
     */
    public List<String> getCourseEvents(String courseID) {
        return courses.getEvents(courseID);
    }

    public double getCourseTargetGrade(String courseID) {
        return courses.getTargetGrade(courseID);
    }

    public List<String> getCourseNotes(String courseID) {
        return courses.getNotes(courseID);
    }

    public double getCourseAverage(String courseID) {
        List<String> eventIDs = courses.getEvents(courseID);
        double assessed = 0;
        double assessedMax = 0;
        for (String eventID : eventIDs) {
            double grade = events.getGrade(eventID);
            double weight = events.getWeight(eventID);
            if (grade != -1 && weight != -1) {
                assessed += grade * weight / 100;
                assessedMax += events.getWeight(eventID);
            }
        }
        if (assessedMax != 0) {
            return (assessed / assessedMax) * 100;
        }
        return -1;
    }

    // getters - events

    /**
     * Returns a map containing all events' name, event ID, due date, grade and weight. Note that grades and weights default to -1 if they weren't set.
     *
     * @return a map with event ID as key, and a string array of format [event name, event due date, grade, weight]
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

    public double getEventGrade(String eventID) {
        return events.getGrade(eventID);
    }

    public double getEventWeight(String eventID) {
        return events.getWeight(eventID);
    }

    public List<String> getEventNotes(String eventID) {
        return events.getNotes(eventID);
    }
}
