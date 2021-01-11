package model;

import model.data.CourseManager;
import model.data.EventManager;

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

    public void addCourse(String name) {
        courses.addCourse(name);
    }

    public void addEvent(String name) {
        events.addEvent(name);
    }

    public void setCourseBreakdown(String courseID, Map<String, Integer> breakdown) {
        courses.setBreakdown(courseID, breakdown);
    }
}
