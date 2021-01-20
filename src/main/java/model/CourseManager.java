package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseManager  {

    private Map<String, Course> map;
    private Map<String, Course> archived = new HashMap<>(20);

    // setters

    String addCourse(String name) {
        Course course = new Course(name);
        map.put(course.getID(), course);
        return course.getID();
    }

    void removeCourse(String courseID) {
        map.remove(courseID);
    }

    void setName(String courseID, String name) {
        map.get(courseID).setName(name);
    }

    void setCode(String courseID, String code) {
        map.get(courseID).setCode(code);
    }

    void addOneTime(String courseID, String eventID) {
        map.get(courseID).addOneTime(eventID);
    }

    void removeOneTime(String courseID, String eventID) {
        map.get(courseID).removeOneTime(eventID);
    }

    boolean setTargetGrade(String courseID, double targetGrade) {
        if (targetGrade >= 0) {
            map.get(courseID).setTargetGrade(targetGrade);
            return true;
        }
        return false;
    }

    void addNotes(String courseID, String notes) {
        map.get(courseID).addNotes(notes);
    }

    // getters

    String getName(String courseID) {
        return map.get(courseID).getName();
    }

    String getCode(String courseID) {
        return map.get(courseID).getCode();
    }

    List<String> getOneTime(String courseID) {
        return map.get(courseID).getOneTime();
    }

    double getTargetGrade(String courseID) {
        return map.get(courseID).getTargetGrade();
    }

    Map<String, String> getAllCourseInfo() {
        Map<String, String> info = new HashMap<>(20);
        for (String courseID: map.keySet()) {
            info.put(courseID, map.get(courseID).getCode());
        }
        return info;
    }

    List<String> getNotes(String courseID) {
        return map.get(courseID).getNotes();
    }

    // data import/export

    void importData(Map<String, Course> courses) {
        map = courses;
    }

    Map<String, Course> exportData() {
        return map;
    }
}
