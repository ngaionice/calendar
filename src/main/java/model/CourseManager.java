package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseManager  {

    private Map<String, Course> map;
    private Map<String, Course> archived;

    // setters

    String addCourse(String name) {
        Course course = new Course(name);
        map.put(course.getID(), course);
        return course.getID();
    }

    void removeCourse(String courseID) {
        map.remove(courseID);
        archived.remove(courseID);
    }

    void setName(String courseID, String name) {
        map.get(courseID).setName(name);
    }

    void setCode(String courseID, String code) {
        map.get(courseID).setCode(code);
    }

    void addOneTime(String courseID, String eventID) {
        map.get(courseID).addEvent(eventID);
    }

    void removeOneTime(String courseID, String eventID) {
        map.get(courseID).removeEvent(eventID);
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

    void archiveCourse(String courseID) {
        archived.put(courseID, map.get(courseID));
        map.remove(courseID);
    }

    void unArchiveCourse(String courseID) {
        map.put(courseID, archived.get(courseID));
        archived.remove(courseID);
    }

    // getters

    String getName(String courseID, boolean isArchived) {
        return (!isArchived ? map : archived).get(courseID).getName();
    }

    String getCode(String courseID, boolean isArchived) {
        return (!isArchived ? map : archived).get(courseID).getCode();
    }

    List<String> getEvents(String courseID) {
        if (map.containsKey(courseID)) {
            return map.get(courseID).getEvents();
        }
        return archived.get(courseID).getEvents();
    }

    double getTargetGrade(String courseID) {
        return map.get(courseID).getTargetGrade();
    }

    Map<String, String> getAllCourseInfo(boolean isArchived) {
        Map<String, String> info = new HashMap<>(20);
        for (String courseID: (!isArchived ? map : archived).keySet()) {
            info.put(courseID, (!isArchived ? map : archived).get(courseID).getCode());
        }
        return info;
    }

    List<String> getNotes(String courseID) {
        return map.get(courseID).getNotes();
    }



    // data import/export

    void importData(List<Map<String, Course>> courses) {
        assert (courses.size() == 2) : "Course data has incorrect length.";
        map = courses.get(0);
        archived = courses.get(1);
    }

    Map<String, Course> exportCourses() {
        return map;
    }

    Map<String, Course> exportArchivedCourses() {
        return archived;
    }
}
