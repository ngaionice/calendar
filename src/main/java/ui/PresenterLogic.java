package ui;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class PresenterLogic {

    Controller con;

    public PresenterLogic(Controller con) {
        this.con = con;
    }

    ObservableList<ObservableCourse> getCourses(boolean includeRecurring) {
        ObservableList<ObservableCourse> courses = FXCollections.observableArrayList();
        Map<String, String> courseInfo = con.getAllCourseInfo();

        for (String id : courseInfo.keySet()) {
            String code = courseInfo.get(id);

            List<String> eventIDs = new ArrayList<>();
            String earliestEvent = "N/A";
            LocalDateTime earliestDue = LocalDateTime.of(2999, 10, 10, 10, 30);
            if (includeRecurring) {
                for (String recurringID : con.getRecurringCourseEvents(id)) {
                    eventIDs.addAll(con.getRecurringEventInstances(recurringID));
                }
            }
            eventIDs.addAll(con.getOneTimeCourseEvents(id));

            // find the earliest event
            for (String eventID : eventIDs) {
                LocalDateTime thisDueDate = con.getEventDueDate(eventID);
                if (thisDueDate != null && thisDueDate.isBefore(earliestDue) && thisDueDate.isAfter(LocalDateTime.now())) {
                    earliestDue = thisDueDate;
                    earliestEvent = con.getEventName(eventID);
                }
            }
            courses.add(new ObservableCourse(con.getCourseName(id), code, id, con.getCourseAverage(id), earliestEvent, earliestDue));
        }

        return courses;
    }

    Map<String, String> getAllCourseInfo() {
        return con.getAllCourseInfo();
    }

    ObservableList<ObservableEvent> getCourseEvents(String courseID, boolean isUpcoming) {
        ObservableList<ObservableEvent> events = FXCollections.observableArrayList();
        List<String> eventIDs = con.getOneTimeCourseEvents(courseID);
        List<String> recurringIDs = con.getRecurringCourseEvents(courseID);
        String courseCode = con.getCourseCode(courseID);
        for (String recurringID : recurringIDs) {
            eventIDs.addAll(con.getRecurringEventInstances(recurringID));
        }

        // TODO: figure out why one-time course events list gets mutated to contain duplicates eventually after reloading
        eventIDs = eventIDs.stream().distinct().collect(Collectors.toList());
        for (String eventID : eventIDs) {
            if (!isUpcoming) {
                if (LocalDateTime.now().isAfter(con.getEventDueDate(eventID))) {
                    events.add(new ObservableEvent(con.getEventName(eventID), eventID, courseCode, con.getEventDueDate(eventID), con.getEventGrade(eventID), con.getEventWeight(eventID)));
                }
            } else {
                if (LocalDateTime.now().isBefore(con.getEventDueDate(eventID))) {
                    events.add(new ObservableEvent(con.getEventName(eventID), eventID, courseCode, con.getEventDueDate(eventID), con.getEventGrade(eventID), con.getEventWeight(eventID)));
                }
            }
        }
        return events;
    }

    boolean verifyAndAddCourse(String courseName, String courseCode, String[] eventNames, String[] marks, Boolean[] recurrings, JFXDatePicker[] dates,
                               JFXTimePicker[] times, JFXDatePicker[] skipDates, JFXTextField[] occurrences, JFXTextField[] offsets, int rows) {
        String courseID = con.addCourse(courseName);
        con.setCourseCode(courseID, courseCode);
        System.out.println("Processing courses");
        // create the events and add them into the course
        for (int i = 0; i < rows - 1; i++) {

            // check if this course is empty (i.e. the user pressed the add button too many times)
            String name = eventNames[i];
            if (name == null || name.trim().equals("")) {
                continue;
            }

            // get event weighting in course
            double weight;
            try {
                weight = Double.parseDouble(marks[i]);
            } catch (NumberFormatException | NullPointerException e) {
                weight = 0;
            }

            // get due date
            LocalDateTime dueDate;
            LocalDate date = dates[i].getValue();
            LocalTime time = times[i].getValue();

            try {
                dueDate = LocalDateTime.of(date, time);
            } catch (NullPointerException e) {
                if (date != null) {
                    dueDate = LocalDateTime.of(date, LocalTime.of(0, 0));
                } else if (time != null){
                    dueDate = LocalDateTime.of(LocalDate.of(2099, 12, 31), time);
                } else {
                    dueDate = LocalDateTime.of(2099, 12, 31, 23, 59);
                }
            }

            // recurring event
            if (recurrings[i] != null && recurrings[i]) {
                int occurrence;
                int offset;
                LocalDate skip = skipDates[i].getValue() != null ? skipDates[i].getValue() : LocalDate.of(2099, 12, 31);
                try {
                    occurrence = Integer.parseInt(occurrences[i].getText());
                    if (occurrence == 0) {
                        occurrence = 1;
                    }
                } catch (NumberFormatException e) {
                    con.removeCourse(courseID);
                    return false;
                }
                try {
                    offset = Integer.parseInt(offsets[i].getText());
                } catch (NumberFormatException e) {
                    offset = 1;
                }
                double itemWeight = (double) Math.round((weight/occurrence) * 100) / 100;
                String recurringID = con.addRecurringEvent(name, offset, occurrence, dueDate, skip);
                con.addEventToCourse(courseID, recurringID, true);
                con.getRecurringEventInstances(recurringID).forEach(item -> con.setEventWeight(item, itemWeight));
            }

            // one-time event
            else {
                String eventID = con.addEvent(name);
                con.setEventDueDate(eventID, dueDate);
                con.addEventToCourse(courseID, eventID, false);
                con.setEventWeight(eventID, weight);
            }
        }
        return true;
    }

    /**
     * Returns a map of lists of ObservableEvents occurring in the current month.
     * Key: day of month;
     * Value: ObservableList of ObservableEvent
     *
     * @return a map of ObservableList of ObservableEvents mapped by day of month
     */
    Map<Integer, ObservableList<ObservableEvent>> getEventsOfMonth() {
        Map<Integer, ObservableList<ObservableEvent>> eventsMap = new HashMap<>(31);

        Month month = LocalDateTime.now().getMonth();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // get all the events
        ObservableList<ObservableEvent> events = FXCollections.observableArrayList();
        Map<String, String> courseInfo = getAllCourseInfo();
        for (String courseID: courseInfo.keySet()) {
            events.addAll(getCourseEvents(courseID, true));
            events.addAll(getCourseEvents(courseID, false));
        }

        // sort each event by date, extracting only the events in the same month
        for (ObservableEvent event: events) {
            LocalDateTime dueDate = LocalDateTime.parse(event.dueDateProperty().get(), df);
            if (dueDate.getMonth().getValue() == month.getValue()) {
                if (!eventsMap.containsKey(dueDate.getDayOfMonth())) {
                    eventsMap.put(dueDate.getDayOfMonth(), FXCollections.observableArrayList(event));
                } else {
                    eventsMap.get(dueDate.getDayOfMonth()).add(event);
                }
            }
        }

        return eventsMap;
    }
}
