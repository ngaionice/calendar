package ui;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
                if (thisDueDate != null && thisDueDate.isBefore(earliestDue)) {
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
            if (!field.getText().equals("") && !field.getText().equals(".")) {
                count += Double.parseDouble(field.getText());
            }
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

    boolean verifyAndAddCourse(String courseName, String[] eventNames, String[] marks, Boolean[] recurrings, JFXDatePicker[] dates, JFXTimePicker[] times, JFXDatePicker[] skipDates, JFXTextField[] occurrences, JFXTextField[] offsets, int rows) {
        List<Double> marksNumeric = new ArrayList<>();
        for (String mark : marks) {
            if (mark != null) {
                try {
                    marksNumeric.add(Double.parseDouble(mark));
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        if (Math.abs(marksNumeric.stream().mapToDouble(f -> f).sum() - 100) <= 0.1) {
            String courseID = con.addCourse(courseName);
            List<String> events = new ArrayList<>();

            // create the events and add them into the course
            for (int i = 1; i < rows; i++) {
                String name = eventNames[i - 1];
                LocalDateTime dueDate = null;
                if (dates[i - 1].getValue() != null && times[i - 1].getValue() != null) {
                    dueDate = LocalDateTime.of(dates[i - 1].getValue(), times[i - 1].getValue());
                }
                if (recurrings[i - 1] != null && recurrings[i - 1]) {
                    int occurrence;
                    int offset;
                    try {
                        occurrence = Integer.parseInt(occurrences[i - 1].getText());
                        offset = Integer.parseInt(offsets[i - 1].getText());
                    } catch (NumberFormatException e) {
                        return false;
                    }
                    String recurringID = con.addRecurringEvent(name, occurrence, offset, dueDate, skipDates[i - 1].getValue());
                    events.add(recurringID);
                    con.addEventToCourse(courseID, recurringID, true);
                } else {
                    String eventID = con.addEvent(name);
                    events.add(eventID);
                    con.setEventDueDate(eventID, dueDate);
                    con.addEventToCourse(courseID, eventID, false);
                }
            }

            // create the mark breakdown
            Map<String, Double> breakdown = new HashMap<>(10);
            for (int i = 0; i < events.size(); i++) {
                breakdown.put(events.get(i), Double.parseDouble(marks[i]));
            }
            con.setCourseBreakdown(courseID, breakdown);

            return true;
        }
        return false;
    }
}
