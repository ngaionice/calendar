package ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ObservableEvent {

    private final StringProperty name;
    private final StringProperty dueDate;
    private final StringProperty id;

    public ObservableEvent(String name, String id, LocalDateTime dueDate) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.dueDate = new SimpleStringProperty(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }
}
