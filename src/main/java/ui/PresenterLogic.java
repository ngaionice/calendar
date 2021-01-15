package ui;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import model.Controller;
import model.Course;

import java.time.LocalDateTime;
import java.util.*;

public class PresenterLogic {

    Controller con;

    public PresenterLogic(Controller con) {
        this.con = con;
    }

    ObservableList<ObservableCourse> getCourses(boolean includeRecurring) {
        ObservableList<ObservableCourse> courses = FXCollections.observableArrayList();
        Map<String, String> courseInfo = con.getAllCourseInfo();

        for (String id: courseInfo.keySet()) {
            String name = courseInfo.get(id);

            List<String> eventIDs = new ArrayList<>();
            String earliestEvent = "N/A";
            LocalDateTime earliestDue = LocalDateTime.of(2999, 10, 10, 10, 30);
            if (includeRecurring) {
                eventIDs.addAll(con.getRecurringCourseEvents(id));
            }
            eventIDs.addAll(con.getOneTimeCourseEvents(id));

            // find the earliest event
            for (String eventID: eventIDs) {
                LocalDateTime thisDueDate = con.getEventDueDate(eventID);
                if (thisDueDate.isBefore(earliestDue)) {
                    earliestDue = thisDueDate;
                    earliestEvent = con.getEventName(eventID);
                }
            }
            courses.add(new ObservableCourse(name, id, con.getCourseAverage(id), earliestEvent, earliestDue));
        }

        return courses;
    }

    boolean verifyAndAddCourse(String name, List<JFXTextField> fields, List<JFXTextField> marks, List<JFXDatePicker> dates, List<JFXTimePicker> times) {
        double count = 0;
        for (TextField field: marks) {
            count += Double.parseDouble(field.getText());
        }
        if (Math.abs(count - 100) > 0.01) {
            return false;
        }
        Map<String, Double> map = new HashMap<>(10);
        String courseID = con.addCourse(name);
        for (int i = 0; i < fields.size(); i++) {
            String eventID = con.addEvent(fields.get(i).getText());
            con.setEventDueDate(eventID, LocalDateTime.of(dates.get(i).getValue(), times.get(i).getValue()));
            double mark = marks.get(i).getText().equals("") || marks.get(i).getText().equals(".") ? 0 : Double.parseDouble(marks.get(i).getText());
            map.put(eventID, mark);

            // TODO: account for recurring events
            con.addEventToCourse(courseID, eventID, false);
        }
        con.setCourseBreakdown(courseID, map);
        return true;
    }
}
