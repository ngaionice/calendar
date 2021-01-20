package ui;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PresenterLogic {

    Controller con;

    public PresenterLogic(Controller con) {
        this.con = con;
    }

    ObservableList<ObservableCourse> getCourses() {
        ObservableList<ObservableCourse> courses = FXCollections.observableArrayList();
        Map<String, String> courseInfo = con.getAllCourseInfo();

        for (String id : courseInfo.keySet()) {
            String code = courseInfo.get(id);
            courses.add(getObservableCourse(id, code));
        }

        return courses;
    }

    ObservableCourse getObservableCourse(String courseID, String courseCode) {
        String earliestEvent = "N/A";
        LocalDateTime earliestDue = LocalDateTime.of(2999, 10, 10, 10, 30);
        List<String> eventIDs = con.getCourseEvents(courseID);

        // find the earliest event
        for (String eventID : eventIDs) {
            LocalDateTime thisDueDate = con.getEventDueDate(eventID);
            if (thisDueDate != null && thisDueDate.isBefore(earliestDue) && thisDueDate.isAfter(LocalDateTime.now())) {
                earliestDue = thisDueDate;
                earliestEvent = con.getEventName(eventID);
            }
        }
        return new ObservableCourse(con.getCourseName(courseID), courseCode, courseID, con.getCourseAverage(courseID), earliestEvent, earliestDue);
    }

    Map<String, String> getAllCourseInfo() {
        return con.getAllCourseInfo();
    }

    ObservableList<ObservableEvent> getCourseEvents(String courseID, boolean isUpcoming) {
        ObservableList<ObservableEvent> events = FXCollections.observableArrayList();
        List<String> eventIDs = con.getCourseEvents(courseID);
        String courseCode = con.getCourseCode(courseID);

        for (String eventID : eventIDs) {
            if (!isUpcoming) {
                if (LocalDateTime.now().isAfter(con.getEventDueDate(eventID))) {
                    events.add(new ObservableEvent(con.getEventName(eventID), eventID, courseCode, courseID, con.getEventDueDate(eventID), con.getEventGrade(eventID), con.getEventWeight(eventID)));
                }
            } else {
                if (LocalDateTime.now().isBefore(con.getEventDueDate(eventID))) {
                    events.add(new ObservableEvent(con.getEventName(eventID), eventID, courseCode, courseID, con.getEventDueDate(eventID), con.getEventGrade(eventID), con.getEventWeight(eventID)));
                }
            }
        }
        return events;
    }

    String verifyAndAddCourse(String courseName, String courseCode, String[] eventNames, String[] weights, Boolean[] recurrings, JFXDatePicker[] dates,
                               JFXTimePicker[] times, JFXDatePicker[] skipDates, JFXTextField[] occurrences, JFXTextField[] offsets, int rows) {
        String courseID;
        if (!courseName.trim().equals("")) {
            courseID = con.addCourse(courseName);
        } else {
            return "";
        }
        con.setCourseCode(courseID, courseCode);
        // create the events and add them into the course
        for (int i = 0; i < rows - 1; i++) {
            boolean eventAdded = addEvent(eventNames[i], weights[i], recurrings[i], dates[i], times[i], skipDates[i], occurrences[i], offsets[i], courseID);
            if (!eventAdded) {
                return "";
            }
        }
        return courseID;
    }

    boolean addEvent(String eventName, String weightString, Boolean recurring, JFXDatePicker date1, JFXTimePicker time1, JFXDatePicker skipDate, JFXTextField occurrence1, JFXTextField offset1, String courseID) {

        // check if this event is empty (i.e. the user pressed the add button too many times)
        String name = eventName;
        if (name == null || name.trim().equals("")) {
            return false;
        }
        name = name.trim();

        // get event weighting in course
        double weight;
        try {
            weight = Double.parseDouble(weightString);
        } catch (NumberFormatException | NullPointerException e) {
            weight = 0;
        }

        // get due date
        LocalDateTime dueDate;
        LocalDate date = date1.getValue();
        LocalTime time = time1.getValue();

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
        if (recurring != null && recurring) {
            int occurrence;
            int offset;
            LocalDate skip = skipDate.getValue() != null ? skipDate.getValue() : LocalDate.of(2099, 12, 31);
            try {
                occurrence = Integer.parseInt(occurrence1.getText());
                if (occurrence == 0) {
                    occurrence = 1;
                }
            } catch (NumberFormatException e) {
                con.removeCourse(courseID);
                return false;
            }
            try {
                offset = Integer.parseInt(offset1.getText());
            } catch (NumberFormatException e) {
                offset = 1;
            }
            double itemWeight = (double) Math.round((weight/occurrence) * 100) / 100;
            List<String> eventIDs = con.addRecurringEvent(name, offset, occurrence, dueDate, skip);
            eventIDs.forEach(item -> {
                con.addEventToCourse(courseID, item);
                con.setEventWeight(item, itemWeight);
            });
        }

        // one-time event
        else {
            String eventID = con.addEvent(name);
            con.setEventDueDate(eventID, dueDate);
            con.addEventToCourse(courseID, eventID);
            con.setEventWeight(eventID, weight);
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
    Map<Integer, ObservableList<ObservableEvent>> getEventsOfMonth(LocalDate date) {
        Map<Integer, ObservableList<ObservableEvent>> eventsMap = new HashMap<>(31);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // get all the events
        ObservableList<ObservableEvent> events = FXCollections.observableArrayList();
        Map<String, String> courseInfo = getAllCourseInfo();
        for (String courseID: courseInfo.keySet()) {
            events.addAll(getCourseEvents(courseID, true));
            events.addAll(getCourseEvents(courseID, false));
        }

        // sort each event by date, extracting only the events in the same year + month
        for (ObservableEvent event: events) {
            LocalDateTime dueDate = LocalDateTime.parse(event.dueDateProperty().get(), df);
            if (dueDate.getMonth().getValue() == date.getMonth().getValue() && dueDate.getYear() == date.getYear()) {
                if (!eventsMap.containsKey(dueDate.getDayOfMonth())) {
                    eventsMap.put(dueDate.getDayOfMonth(), FXCollections.observableArrayList(event));
                } else {
                    eventsMap.get(dueDate.getDayOfMonth()).add(event);
                }
            }
        }

        return eventsMap;
    }

    void updateEvent(ObservableEvent event,String name, LocalDate dueDate, LocalTime dueTime, String grade, String weight) {
        String eventID = event.eventIDProperty().get();
        if (name != null) {
            con.setEventName(eventID, name);
            event.setName(name);
        }

        LocalDate currDate = con.getEventDueDate(eventID).toLocalDate();
        LocalTime currTime = con.getEventDueDate(eventID).toLocalTime();
        if (dueDate != null) {
            currDate = dueDate;
        }
        if (dueTime != null) {
            currTime = dueTime;
        }
        LocalDateTime newDueDate = LocalDateTime.of(currDate, currTime);
        con.setEventDueDate(eventID, newDueDate);
        event.setDueDate(newDueDate);

        if (grade != null) {
            con.setEventGrade(eventID, Double.parseDouble(grade));
            event.setGrade(Double.parseDouble(grade));
        }

        if (weight != null) {
            con.setEventWeight(eventID, Double.parseDouble(weight));
            event.setWeight(Double.parseDouble(weight));
        }
    }

    void deleteEvent(ObservableList<ObservableEvent> events, ObservableEvent event) {
        String eventID = event.eventIDProperty().get();
        System.out.println(eventID);
        events.remove(event);
        con.removeEventFromCourse(event.courseIDProperty().get(), eventID);
        con.removeEvent(eventID);

        assert (!con.getAllEventInfo().containsKey(eventID)) : "Key is still in EventManager after deletion";
    }
}
