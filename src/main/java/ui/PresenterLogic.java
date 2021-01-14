package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Controller;
import model.Course;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

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
            String earliestEvent = "$N/A";
            LocalDateTime earliestDue = LocalDateTime.now();
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

            courses.add(new ObservableCourse(name, con.getCourseAverage(id), earliestEvent, earliestDue));
        }

        return courses;
    }
}
