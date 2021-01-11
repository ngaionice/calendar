package model.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseManager  {

    Map<String, Course> map;

    public void addCourse(String name) {
        Course course = new Course(name);
        map.put(course.getID(), course);
    }

    public void removeCourse(String courseID) {
        map.remove(courseID);
    }

    public void setName(String courseID, String name) {
        map.get(courseID).setName(name);
    }

    public void setBreakdown(String courseID, Map<String, Integer> breakdown) {
        map.get(courseID).setBreakdown(breakdown);
    }

    public void addRecurring(String courseID, String eventID) {
        map.get(courseID).addRecurring(eventID);
    }

    public void removeRecurring(String courseID, String eventID) {
        map.get(courseID).removeRecurring(eventID);
    }

    public void addOneTime(String courseID, String eventID) {
        map.get(courseID).addOneTime(eventID);
    }

    public void removeOneTime(String courseID, String eventID) {
        map.get(courseID).removeOneTime(eventID);
    }

    public String getName(String courseID) {
        return map.get(courseID).getName();
    }

    public Map<String, Integer> getBreakdown(String courseID) {
        return map.get(courseID).getBreakdown();
    }

    public List<String> getRecurring(String courseID) {
        return map.get(courseID).getRecurring();
    }

    public List<String> getOneTime(String courseID) {
        return map.get(courseID).getOneTime();
    }

    public void importData(Map<String, Course> courses) {
        map = courses;
    }

    public Map<String, Course> exportData() {
        return map;
    }
}
