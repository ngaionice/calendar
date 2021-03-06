package ui;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class PresenterElements {

    Scene sc;
    PresenterLogic logic;

    Background whiteLightBackground = new Background(new BackgroundFill(Color.rgb(238, 238, 238), new CornerRadii(3), Insets.EMPTY));

    Insets margin = new Insets(8);
    Insets mediumMargin = new Insets(16);
    Insets largerMargin = new Insets(24);

    enum ButtonType {
        FLOATING,
        FLOATING_SUB
    }

    public PresenterElements(Scene sc, PresenterLogic logic) {
        this.sc = sc;
        this.logic = logic;
    }

    VBox getNav(BorderPane content) {
        VBox box = new VBox();
        box.setId("nav-box");

        List<JFXButton> mainButtons = Arrays.asList(new JFXButton("Calendar"), new JFXButton("Courses"),
                new JFXButton("Upcoming"), new JFXButton("Archive"), new JFXButton("Settings"));
        List<String> buttonIDs = Arrays.asList("nav-calendar", "nav-courses", "nav-upcoming", "nav-archive", "nav-settings");

        for (int i = 0; i < mainButtons.size(); i++) {
            JFXButton item = mainButtons.get(i);
            item.setGraphic(new FontIcon());
            item.setId(buttonIDs.get(i));
        }

        Region spacer = new Region();
        spacer.prefHeightProperty().bind(sc.heightProperty().multiply(0.125));

        mainButtons.get(0).setOnAction(event -> {
            content.setCenter(getCalendarPane());
            content.setTop(getLargeLayoutHeader("Calendar"));
        });
        mainButtons.get(1).setOnAction(event -> {
            content.setCenter(getCoursesPane());
            content.setTop(getLargeLayoutHeader("Courses"));
        });
        mainButtons.get(2).setOnAction(event -> {
            content.setCenter(getEventsPane());
            content.setTop(getLargeLayoutHeader("Upcoming"));
        });
        mainButtons.get(3).setOnAction(event -> {
            content.setCenter(getArchivePane());
            content.setTop(getLargeLayoutHeader("Archive"));
        });
        mainButtons.get(4).setOnAction(event -> {
            content.setCenter(getTabPane());
            content.setTop(getLargeLayoutHeader("Settings"));
        });

        box.getChildren().add(spacer);
        box.getChildren().addAll(mainButtons);
        box.prefWidthProperty().bind(sc.widthProperty().multiply(0.15));

        return box;
    }

    VBox getNav(BorderPane content, JFXPopup popup) {
        VBox box = new VBox();
        box.setId("nav-box-popup");

        List<JFXButton> mainButtons = Arrays.asList(new JFXButton("Calendar"), new JFXButton("Courses"),
                new JFXButton("Upcoming"), new JFXButton("Archive"), new JFXButton("Settings"));
        List<String> buttonIDs = Arrays.asList("nav-calendar", "nav-courses", "nav-upcoming", "nav-archive", "nav-settings");

        for (int i = 0; i < mainButtons.size(); i++) {
            JFXButton item = mainButtons.get(i);
            item.setGraphic(new FontIcon());
            item.setId(buttonIDs.get(i));
        }

        Region spacer = new Region();
        spacer.prefHeightProperty().bind(sc.heightProperty().multiply(0.125));

        mainButtons.get(0).setOnAction(event -> {
            content.setCenter(getCalendarPane());
            content.setTop(getSmallLayoutHeader("Calendar", content));
            popup.hide();
        });
        mainButtons.get(1).setOnAction(event -> {
            content.setCenter(getCoursesPane());
            content.setTop(getSmallLayoutHeader("Courses", content));
            popup.hide();
        });
        mainButtons.get(2).setOnAction(event -> {
            content.setCenter(getEventsPane());
            content.setTop(getSmallLayoutHeader("Upcoming", content));
            popup.hide();
        });
        mainButtons.get(3).setOnAction(event -> {
            content.setCenter(getArchivePane());
            content.setTop(getSmallLayoutHeader("Archive", content));
            popup.hide();
        });
        mainButtons.get(4).setOnAction(event -> {
            content.setCenter(getTabPane());
            content.setTop(getSmallLayoutHeader("Settings", content));
            popup.hide();
        });

        box.getChildren().add(spacer);
        box.getChildren().addAll(mainButtons);
        box.prefWidthProperty().bind(content.widthProperty().multiply(0.24));
        box.prefHeightProperty().bind(content.heightProperty());

        return box;
    }

    HBox getLargeLayoutHeader(String headerText) {
        HBox root = new HBox();
        root.setId("header-box-large");
        root.prefHeightProperty().bind(sc.heightProperty().multiply(0.07));
        root.getChildren().add(getTextH2(headerText));
        return root;
    }

    HBox getSmallLayoutHeader(String headerText, BorderPane content) {

        HBox root = new HBox();
        root.setId("header-box-small");
        root.prefHeightProperty().bind(sc.heightProperty().multiply(0.028));

        JFXButton navButton = new JFXButton();
        navButton.setGraphic(new FontIcon());
        navButton.setId("nav-button");

        JFXPopup navPopup = new JFXPopup();
        StackPane stack = new StackPane();
        VBox nav = getNav(content, navPopup);

        stack.getChildren().add(nav);
        navPopup.setPopupContent(stack);

        navButton.setOnAction(event -> navPopup.show(content));

        root.getChildren().add(navButton);
        root.getChildren().add(getTextH2(headerText));

        return root;
    }

    JFXTabPane getTabPane() {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(104);
        tabs.setTabMaxWidth(104);

        addTab(tabs, "N/A", new Label("Not implemented yet"));

        return tabs;
    }

    void addTab(JFXTabPane tabs, String tabName, Node content) {
        Tab tab = new Tab(tabName);

        tab.setContent(content);

        tabs.getTabs().add(tab);
    }

    // COURSES
    JFXTabPane getCoursesPane() {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(104);
        tabs.setTabMaxWidth(104);
        tabs.setTabMinHeight(24);

        addTab(tabs, "Overview", getCourseOverviewContent(false));

        ObservableList<ObservableCourse> courses = logic.getCourses(false);
        courses.sort(Comparator.comparing(o -> o.codeProperty().get()));

        for (ObservableCourse course : courses) {
            String code = course.codeProperty().get();
            addTab(tabs, code, getCourseContent(course));
        }
        return tabs;
    }

    StackPane getCourseContent(ObservableCourse course) {
        StackPane root = new StackPane();
        root.setPadding(largerMargin);

        AnchorPane anchor = new AnchorPane();
        GridPane grid = new GridPane();
        grid.setId("course-content-grid");

        HBox infoHeader = new HBox();
        infoHeader.setAlignment(Pos.BOTTOM_LEFT);
        infoHeader.prefWidthProperty().bind(grid.widthProperty());
        infoHeader.setPadding(new Insets(0, 52, 0, 4));

        VBox courseInfo = new VBox(16);
        Text courseName = new Text(course.nameProperty().get());
        courseName.setId("content-header");
        Text nextAssignment = new Text(course.nextEventProperty().get() + " due at " + course.nextDueProperty().get());
        nextAssignment.setId("content-subheader");
        courseInfo.getChildren().addAll(Arrays.asList(courseName, nextAssignment));

        VBox averageInfo = new VBox(8);
        Text courseAverage = new Text(String.valueOf(course.avgProperty().get()));
        courseAverage.setId("content-supersize");
        Text courseAverageText = new Text("Current Average");
        courseAverageText.setId("content-subheader");
        averageInfo.setAlignment(Pos.CENTER);
        averageInfo.getChildren().addAll(Arrays.asList(courseAverage, courseAverageText));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        infoHeader.getChildren().addAll(Arrays.asList(courseInfo, spacer, averageInfo));

        TableView<ObservableEvent> table = new TableView<>();
        table.prefWidthProperty().bind(grid.widthProperty());
        table.prefHeightProperty().bind(sc.heightProperty().multiply(0.5));
        table.setFixedCellSize(48);

        TableColumn<ObservableEvent, String> nameCol = new TableColumn<>("Events");
        TableColumn<ObservableEvent, String> dueDateCol = new TableColumn<>("Due date");
        TableColumn<ObservableEvent, Double> gradeCol = new TableColumn<>("Grade");
        TableColumn<ObservableEvent, Double> weightCol = new TableColumn<>("Weight");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.56));
        dueDateCol.prefWidthProperty().bind(table.widthProperty().multiply(0.24));
        weightCol.prefWidthProperty().bind(table.widthProperty().multiply(0.12));
        gradeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.04));

        List<TableColumn<ObservableEvent, ?>> items = Arrays.asList(nameCol, dueDateCol, weightCol, gradeCol);
        items.forEach(item -> {
            item.getStyleClass().add("text-col");
            item.setResizable(false);
        });

        table.getColumns().addAll(items);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ObservableList<ObservableEvent> eventData = logic.getCourseEvents(course.idProperty().get());
        FilteredList<ObservableEvent> upcomingData = new FilteredList<>(eventData, p -> true);
        FilteredList<ObservableEvent> pastData = new FilteredList<>(eventData, p -> true);
        upcomingData.setPredicate(item -> LocalDateTime.now().isBefore(LocalDateTime.parse(item.dueDateProperty().get(), df)));
        pastData.setPredicate(item -> LocalDateTime.now().isAfter(LocalDateTime.parse(item.dueDateProperty().get(), df)));

        HBox eventSelectionBox = new HBox();
        eventSelectionBox.setSpacing(12);
        eventSelectionBox.setAlignment(Pos.BASELINE_LEFT);
        eventSelectionBox.setPadding(new Insets(0, 52, 0, 4));

        Text eventSelectionText = new Text("Showing events:");
        eventSelectionText.setId("content-content");

        JFXComboBox<String> eventSelection = new JFXComboBox<>();
        eventSelection.setItems(FXCollections.observableArrayList("Upcoming", "Past"));
        eventSelection.setValue("Upcoming");
        eventSelection.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals("Upcoming")) {
                table.getColumns().setAll(items.subList(0, 3));
                table.setItems(upcomingData);
            } else {
                table.getColumns().setAll(items);
                table.setItems(pastData);
            }
        }));

        eventSelectionBox.getChildren().addAll(eventSelectionText, eventSelection);

        table.setRowFactory(view -> {
            TableRow<ObservableEvent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ObservableEvent rowData = row.getItem();
                    JFXDialog editDialog = getEventEditDialog(root, eventData, upcomingData, pastData, rowData, eventSelection.valueProperty().get().equals("Upcoming"));
                    root.getChildren().add(editDialog);
                    editDialog.show();
                }
            });
            return row;
        });
        table.setItems(upcomingData);
        table.getSortOrder().add(dueDateCol);

        JFXButton addEvent = new JFXButton();
        addEvent.setGraphic(new FontIcon());
        addEvent.setId("add");
        addEvent.getStyleClass().add("animated-option-button");

        addEvent.setOnAction(click -> {
            JFXDialog addDialog = getEventCreateDialog(root, eventData, upcomingData, pastData, course.idProperty().get());
            root.getChildren().add(addDialog);
            addDialog.show();
        });

        grid.add(infoHeader, 0, 0);
        grid.add(eventSelectionBox, 0, 1);
        grid.add(table, 0, 2);

        JFXNodesList fab = new JFXNodesList();
        fab.addAnimatedNode(addEvent);
        setAnchorsCreate(grid, fab);
        JFXDepthManager.setDepth(anchor, 1);

        anchor.getChildren().add(grid);
        anchor.getChildren().add(fab);
        root.getChildren().add(anchor);

        return root;
    }

    StackPane getCourseOverviewContent(boolean isArchived) {
        StackPane root = new StackPane();
        root.setPadding(largerMargin);

        AnchorPane anchor = new AnchorPane();
        TableView<ObservableCourse> table = new TableView<>();

        table.prefWidthProperty().bind(sc.widthProperty().multiply(0.8));
        table.prefHeightProperty().bind(sc.heightProperty().multiply(0.8));
        table.setFixedCellSize(48);
        table.setPadding(mediumMargin);
        table.setEditable(true);

        ObservableMap<ObservableCourse, BooleanProperty> checkedRows = FXCollections.observableHashMap();

        TableColumn<ObservableCourse, Void> checkCol = getTableColumn("", "", table, 0.06);
        TableColumn<ObservableCourse, String> codeCol = getTableColumn("Course code", "code", table, 0.12);
        TableColumn<ObservableCourse, String> nameCol = getTableColumn("Course name", "name", table, 0.28);
        TableColumn<ObservableCourse, Double> avgCol = getTableColumn("Average", "avg", table, 0.10);
        TableColumn<ObservableCourse, String> nextEventCol = getTableColumn("Next event", "nextEvent", table, 0.28);
        TableColumn<ObservableCourse, Double> nextDueCol = getTableColumn("Due date", "nextDue", table, 0.16);

        List<TableColumn<ObservableCourse, ?>> items = new ArrayList<>(isArchived ?
                Arrays.asList(codeCol, nameCol, avgCol) :
                Arrays.asList(codeCol, nameCol, nextEventCol, nextDueCol, avgCol));
        items.forEach(item -> {
            item.getStyleClass().add("text-col");
            item.setResizable(false);
        });
        items.add(0, checkCol);

        CheckBox checkAll = new CheckBox();
        checkAll.setOnAction(e -> {
            if (checkAll.isSelected()) {
                table.getItems().forEach(p ->
                        checkedRows.computeIfAbsent(p, ObservableCourse -> new SimpleBooleanProperty()).set(true));
            } else {
                checkedRows.values().forEach(checked -> checked.set(false));
            }
        });

        checkCol.setGraphic(checkAll);
        checkCol.setEditable(true);
        checkCol.getStyleClass().add("checkbox-col");
        checkCol.setSortable(false);

        checkCol.setCellFactory(CheckBoxTableCell.forTableColumn(i ->
                checkedRows.computeIfAbsent(table.getItems().get(i), p -> new SimpleBooleanProperty())));

        ObservableList<ObservableCourse> courseData = logic.getCourses(isArchived);

        table.getColumns().addAll(items);
        table.setItems(courseData);
        table.getSortOrder().add(codeCol);
        table.getItems().addListener((ListChangeListener.Change<? extends ObservableCourse> c) -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(checkedRows::remove);
                }
            }
        });

        JFXButton editCourse = getJFXButton(new FontIcon(), "edit", ButtonType.FLOATING);
        JFXButton addCourse = getJFXButton(new FontIcon(), "add", ButtonType.FLOATING_SUB);
        JFXButton archiveCourse = getJFXButton(new FontIcon(), "archive-action", ButtonType.FLOATING_SUB);
        JFXButton unArchiveCourse = getJFXButton(new FontIcon(), "un-archive-action", ButtonType.FLOATING_SUB);
        JFXButton deleteCourse = getJFXButton(new FontIcon(), "delete", ButtonType.FLOATING_SUB);

        addCourse.setOnAction(action -> {
            JFXDialog addDialog = getCourseCreateDialog(courseData, root, 0.8, 0.9);
            root.getChildren().add(addDialog);
            addDialog.show();
        });
        archiveCourse.setOnAction(action -> {
            List<ObservableCourse> archiving = checkedRows.entrySet().stream()
                    .filter(e -> e.getValue().get())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            archiving.forEach(item -> logic.archiveCourse(item, courseData));
        });
        deleteCourse.setOnAction(action -> {
            List<ObservableCourse> deleting = checkedRows.entrySet().stream()
                    .filter(e -> e.getValue().get())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            deleting.forEach(item -> logic.deleteCourse(item, courseData));
        });
        unArchiveCourse.setOnAction(action -> {
            List<ObservableCourse> unArchiving = checkedRows.entrySet().stream()
                    .filter(e -> e.getValue().get())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            unArchiving.forEach(item -> logic.unArchiveCourse(item, courseData));
        });

        JFXNodesList fab = new JFXNodesList();
        JFXNodesList changes = new JFXNodesList();
        fab.addAnimatedNode(changes);
        setAnchorsCreate(table, fab);

        changes.addAnimatedNode(editCourse);
        if (!isArchived) {
            changes.addAnimatedNode(addCourse);
            changes.addAnimatedNode(archiveCourse);
        } else {
            changes.addAnimatedNode(unArchiveCourse);
        }
        changes.addAnimatedNode(deleteCourse);
        changes.setRotate(180);
        changes.setSpacing(12);

        JFXDepthManager.setDepth(anchor, 1);

        anchor.getChildren().add(table);
        anchor.getChildren().add(fab);
        root.getChildren().add(anchor);
        return root;
    }

    JFXDialog getCourseCreateDialog(ObservableList<ObservableCourse> courseData, StackPane root, double hBindRatio, double vBindRatio) {
        JFXDialogLayout layout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, layout, JFXDialog.DialogTransition.CENTER);
        layout.setId("course-create-dialog");

        StackPane content = new StackPane();
        content.setPadding(mediumMargin);

        AtomicReference<String> courseName = new AtomicReference<>("");
        AtomicReference<String> courseCode = new AtomicReference<>("");

        List<JFXButton> nextButtons = Arrays.asList(new JFXButton("NEXT"), new JFXButton("NEXT"), new JFXButton("SAVE"));
        List<JFXButton> backButtons = Arrays.asList(new JFXButton("BACK"), new JFXButton("BACK"));
        nextButtons.forEach(item -> item.setId("raised-button"));
        backButtons.forEach(item -> item.setId("flat-button"));

        // step 1
        VBox step1 = new VBox(24);
        step1.setAlignment(Pos.CENTER);

        JFXTextField courseCodeField = getTextField(content, "Course code", 0.16, false);
        JFXTextField courseNameField = getTextField(content, "Course name", 0.16, false);
        courseNameField.maxWidthProperty().bind(content.widthProperty().multiply(0.32));
        courseCodeField.maxWidthProperty().bind(content.widthProperty().multiply(0.32));
        courseNameField.textProperty().addListener((observable, oldValue, newValue) -> courseName.set(newValue));
        courseCodeField.textProperty().addListener((observable, oldValue, newValue) -> courseCode.set(newValue));

        step1.getChildren().addAll(Arrays.asList(courseCodeField, courseNameField));

        // step 2
        GridPane events = new GridPane();
        events.getRowConstraints().add(new RowConstraints(52));
        Text eventHeaderText = new Text("Set course events");
        eventHeaderText.setId("dialog-subheader");
        JFXButton addEvent = new JFXButton();
        AtomicInteger items = new AtomicInteger(1);

        HBox eventsHeader = new HBox(12);
        eventsHeader.getChildren().addAll(Arrays.asList(eventHeaderText, addEvent));
        eventsHeader.setAlignment(Pos.CENTER_LEFT);

        HBox[] rows = new HBox[7];
        String[] eventNames = new String[7];
        String[] weights = new String[7];
        Boolean[] recurrings = new Boolean[7];

        JFXTextField field1 = getTextField(content, "Event name", 0.3, false);
        JFXTextField weight = getTextField(content, "Weight", 0.08, true);
        JFXToggleButton button1 = getToggleButton("Recurring?");

        field1.textProperty().addListener((observable, oldValue, newValue) -> eventNames[0] = newValue);
        weight.textProperty().addListener((observable, oldValue, newValue) -> weights[0] = newValue);
        button1.selectedProperty().addListener((observable, oldValue, newValue) -> recurrings[0] = newValue);

        HBox first = new HBox();
        first.getChildren().addAll(Arrays.asList(field1, weight, button1));
        first.setSpacing(16);
        first.setAlignment(Pos.CENTER_LEFT);

        rows[0] = first;

        addEvent.setOnAction(e -> {
            if (items.get() < 7) {
                int i = items.get();

                JFXTextField nameField = getTextField(content, "Event name", 0.3, false);
                nameField.textProperty().addListener((observable, oldValue, newValue) -> eventNames[i] = newValue);
                JFXTextField markField = getTextField(content, "Weight", 0.08, true);
                markField.textProperty().addListener((observable, oldValue, newValue) -> weights[i] = newValue);
                JFXToggleButton buttonField = getToggleButton("Recurring?");
                buttonField.selectedProperty().addListener((observable, oldValue, newValue) -> recurrings[i] = newValue);

                rows[i] = new HBox();
                rows[i].getChildren().addAll(Arrays.asList(nameField, markField, buttonField));
                rows[i].setSpacing(16);
                rows[i].setAlignment(Pos.CENTER_LEFT);

                events.add(rows[i], 0, i + 1);
                events.getRowConstraints().add(new RowConstraints(48));
                items.addAndGet(1);
            }
        });

        events.add(eventsHeader, 0, 0);
        events.add(rows[0], 0, 1);
        events.getRowConstraints().add(new RowConstraints(48));

        // step 3
        GridPane grid3 = new GridPane();

        HBox step3Header = new HBox(12);
        step3Header.setAlignment(Pos.CENTER_LEFT);

        Text step3HeaderText = new Text("Set due dates");
        step3HeaderText.setId("dialog-subheader");

        step3Header.getChildren().add(step3HeaderText);
        step3Header.prefHeightProperty().bind(eventsHeader.heightProperty());
        step3Header.prefWidthProperty().bind(grid3.widthProperty().multiply(0.64));

        JFXDatePicker[] dates = new JFXDatePicker[7];
        JFXTimePicker[] times = new JFXTimePicker[7];
        JFXDatePicker[] skipDates = new JFXDatePicker[7];
        JFXTextField[] occurrences = new JFXTextField[7];
        JFXTextField[] offsets = new JFXTextField[7];

        JFXSnackbar confirmation = new JFXSnackbar(root);
        JFXSnackbar failure = new JFXSnackbar(content);

        HBox actions = new HBox(12);
        actions.setPadding(margin);

        // nav buttons config
        backButtons.get(0).setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(step1);

            actions.getChildren().clear();
            actions.getChildren().add(nextButtons.get(0));
        });
        Arrays.asList(backButtons.get(1), nextButtons.get(0)).forEach(item ->
                item.setOnAction(event -> {
                    content.getChildren().clear();
                    content.getChildren().add(events);

                    actions.getChildren().clear();
                    actions.getChildren().addAll(Arrays.asList(backButtons.get(0), nextButtons.get(1)));
                }));

        nextButtons.get(1).setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(grid3);

            grid3.getChildren().clear();
            grid3.getRowConstraints().clear();
            grid3.add(step3Header, 0, 0);
            grid3.getRowConstraints().add(new RowConstraints(48));

            for (int i = 0; i < items.get(); i++) {
                if (eventNames[i] == null || eventNames[i].trim().equals("")) {
                    continue;
                }
                HBox box = new HBox(16);
                box.setAlignment(Pos.CENTER_LEFT);
                box.prefWidthProperty().bind(grid3.widthProperty().multiply(0.96));

                boolean isRecurring = recurrings[i] != null && recurrings[i];
                dates[i] = getDatePicker(content, isRecurring ? "First due date" : "Due date", 0.24);
                times[i] = getTimePicker(content, "Due time", 0.20);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                box.getChildren().addAll(Arrays.asList(getTextNormal(eventNames[i]),
                        spacer, dates[i], times[i]));
                if (isRecurring) {
                    skipDates[i] = getDatePicker(content, "Skip date (optional)", 0.28);
                    occurrences[i] = getTextField(grid3, "#", 0.12, true);
                    offsets[i] = getTextField(grid3, "Offset", 0.12, true);
                    box.getChildren().add(occurrences[i]);
                    box.getChildren().add(offsets[i]);
                    box.getChildren().add(skipDates[i]);
                }

                grid3.add(box, 0, i + 1);
                grid3.getRowConstraints().add(new RowConstraints(48));
            }

            actions.getChildren().clear();
            actions.getChildren().addAll(Arrays.asList(backButtons.get(1), nextButtons.get(2)));
        });
        nextButtons.get(2).setOnAction(event -> {
            String courseID = logic.verifyAndAddCourse(courseName.get(), courseCode.get(), eventNames, weights, recurrings,
                    dates, times, skipDates, occurrences, offsets, grid3.getRowConstraints().size());
            if (!courseID.equals("")) {
                dialog.close();
                root.getChildren().remove(dialog);
                confirmation.enqueue(getSnackbarEvent(root, "Course added, refresh to view the new course tab", 0.64, 0.08));
                courseData.add(logic.getObservableCourse(courseID, courseCode.get(), false));
            } else {
                System.out.println("not saved");
                failure.enqueue(getSnackbarEvent(content, "Invalid data, please try again", 0.64, 0.12));
            }
        });


        Text header = new Text("Add course");
        header.setId("dialog-header");

        addEvent.setGraphic(new FontIcon());
        addEvent.setId("add-event");

        eventHeaderText.setFill(Paint.valueOf("#FFF"));
        eventsHeader.setAlignment(Pos.CENTER_LEFT);

        layout.setHeading(header);
        layout.setBody(content);
        layout.setActions(actions);

        layout.prefWidthProperty().bind(root.widthProperty().multiply(hBindRatio));
        layout.prefHeightProperty().bind(root.heightProperty().multiply(vBindRatio));

        // set up start part
        content.getChildren().clear();
        content.getChildren().add(step1);

        actions.getChildren().clear();
        actions.getChildren().add(nextButtons.get(0));

        return dialog;
    }

    // EVENTS
    JFXTabPane getEventsPane() {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(104);
        tabs.setTabMaxWidth(104);
        tabs.setTabMinHeight(24);

        addTab(tabs, "View", getEventOverviewContent(true));
        return tabs;
    }

    StackPane getEventOverviewContent(boolean isUpcoming) {
        StackPane root = new StackPane();
        root.setPadding(largerMargin);

        TableView<ObservableEvent> table = new TableView<>();

        table.prefWidthProperty().bind(sc.widthProperty().multiply(0.8));
        table.prefHeightProperty().bind(sc.heightProperty().multiply(0.8));
        table.setFixedCellSize(48);
        table.setPadding(mediumMargin);

        TableColumn<ObservableEvent, String> codeCol = new TableColumn<>("Course code");
        TableColumn<ObservableEvent, String> nameCol = new TableColumn<>("Event name");
        TableColumn<ObservableEvent, Double> dueDateCol = new TableColumn<>("Due date");

        TableColumn<ObservableEvent, Double> weightCol = new TableColumn<>("Weight");

        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

        codeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.12));
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.28));
        dueDateCol.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
        weightCol.prefWidthProperty().bind(table.widthProperty().multiply(0.08));

        List<TableColumn<ObservableEvent, ?>> columns = new ArrayList<>(Arrays.asList(dueDateCol, codeCol, nameCol, weightCol));
        if (!isUpcoming) {
            TableColumn<ObservableEvent, Double> markCol = new TableColumn<>("Mark");
            markCol.setCellValueFactory(new PropertyValueFactory<>("mark"));
            weightCol.prefWidthProperty().bind(table.widthProperty().multiply(0.12));
            markCol.prefWidthProperty().bind(table.widthProperty().multiply(0.12));
            columns.add(markCol);
        } else {
            TableColumn<ObservableEvent, Integer> remainingDaysCol = new TableColumn<>("Days left");
            remainingDaysCol.setCellValueFactory(new PropertyValueFactory<>("remainingDays"));
            remainingDaysCol.prefWidthProperty().bind(table.widthProperty().multiply(0.08));
            columns.add(0, remainingDaysCol);
        }
        columns.forEach(item -> {
            item.getStyleClass().add("text-col");
            item.setResizable(false);
        });

        ObservableList<ObservableEvent> events = FXCollections.observableArrayList();
        Map<String, String> courseInfo = logic.getAllCourseInfo(false);
        for (String courseID : courseInfo.keySet()) {
            events.addAll(logic.getCourseEvents(courseID, isUpcoming));
        }

        table.getColumns().addAll(columns);
        table.setItems(events);
        table.getSortOrder().add(dueDateCol);

        JFXDepthManager.setDepth(table, 1);

        root.getChildren().add(table);
        return root;
    }

    JFXTabPane getArchivePane() {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(104);
        tabs.setTabMaxWidth(104);
        tabs.setTabMinHeight(24);

        addTab(tabs, "Courses", getCourseOverviewContent(true));
        addTab(tabs, "Events", getEventOverviewContent(false));

        return tabs;
    }

    JFXDialog getEventEditDialog(StackPane root, ObservableList<ObservableEvent> events, FilteredList<ObservableEvent> upcomingEvents, FilteredList<ObservableEvent> pastEvents, ObservableEvent event, boolean isUpcoming) {
        JFXDialogLayout layout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, layout, JFXDialog.DialogTransition.CENTER);
        layout.setId("event-edit-dialog");

        Text headerText = new Text("Editing " + event.nameProperty().get());
        headerText.setId("dialog-header");

        StackPane content = new StackPane();
        content.setPadding(mediumMargin);

        GridPane grid = new GridPane();
        grid.getRowConstraints().addAll(Arrays.asList(new RowConstraints(52), new RowConstraints(52), new RowConstraints(52), new RowConstraints(52)));
        content.getChildren().add(grid);

        JFXTextField nameField = getTextField(content, "Name", 0.56, false);
        JFXTextField weightField = getTextField(content, "Weight", 0.56, true);
        JFXDatePicker datePicker = getDatePicker(content, "Due date", 0.28);
        JFXTimePicker timePicker = getTimePicker(content, "Due time", 0.28);

        grid.add(nameField, 0, 0, 2, 1);
        grid.add(weightField, 0, 2, 2, 1);
        grid.add(datePicker, 0, 1);
        grid.add(timePicker, 1, 1);

        JFXTextField gradeField = null;
        if (!isUpcoming) {
            gradeField = getTextField(content, "Grade", 0.56, true);
            grid.add(gradeField, 0, 3, 2, 1);
        }

        HBox actions = new HBox(12);
        actions.setPadding(margin);

        JFXButton saveModify = new JFXButton("SAVE");
        saveModify.setId("raised-button");
        JFXButton delete = new JFXButton("DELETE");
        delete.setId("flat-button");

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        JFXTextField finalGradeField = gradeField;
        saveModify.setOnAction(click -> {
            logic.updateEvent(event, nameField.getText(), datePicker.getValue(), timePicker.getValue(), !isUpcoming ? finalGradeField.getText() : null, weightField.getText());
            upcomingEvents.setPredicate(item -> LocalDateTime.now().isBefore(LocalDateTime.parse(item.dueDateProperty().get(), df)));
            pastEvents.setPredicate(item -> LocalDateTime.now().isAfter(LocalDateTime.parse(item.dueDateProperty().get(), df)));
            dialog.close();
        });
        delete.setOnAction(click -> {
            logic.deleteEvent(events, event);
            dialog.close();
        });

        actions.getChildren().addAll(Arrays.asList(delete, saveModify));

        layout.setHeading(headerText);
        layout.setBody(content);
        layout.setActions(actions);

        layout.prefWidthProperty().bind(root.widthProperty().multiply(0.5));
        layout.prefHeightProperty().bind(root.heightProperty().multiply(0.8));

        return dialog;
    }

    JFXDialog getEventCreateDialog(StackPane root, ObservableList<ObservableEvent> events, FilteredList<ObservableEvent> upcomingEvents, FilteredList<ObservableEvent> pastEvents, String courseID) {
        JFXDialogLayout layout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, layout, JFXDialog.DialogTransition.CENTER);
        layout.setId("event-edit-dialog");

        Text headerText = new Text("Add event");
        headerText.setId("dialog-header");

        StackPane content = new StackPane();
        content.setPadding(mediumMargin);

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.getRowConstraints().addAll(Arrays.asList(new RowConstraints(52), new RowConstraints(52), new RowConstraints(52), new RowConstraints(52), new RowConstraints(52)));
        content.getChildren().add(grid);

        JFXTextField nameField = getTextField(content, "Name", 0.56, false);
        JFXTextField weightField = getTextField(content, "Weight", 0.16, true);
        JFXDatePicker datePicker = getDatePicker(content, "(First) Due date", 0.36);
        JFXTimePicker timePicker = getTimePicker(content, "Due time", 0.28);
        JFXTextField offset = getTextField(content, "Offset", 0.12, true);
        JFXTextField occurrence = getTextField(content, "# of events", 0.12, true);
        JFXDatePicker skipDate = getDatePicker(content, "Date to skip (optional)", 0.36);

        grid.add(nameField, 0, 0, 2, 1);
        JFXToggleButton recurring = getToggleButton("Recurring?");
        grid.add(weightField, 0, 2);
        grid.add(recurring, 1, 2);
        grid.add(datePicker, 0, 1);
        grid.add(timePicker, 1, 1);
        grid.add(occurrence, 0, 3);
        grid.add(offset, 1, 3);
        grid.add(skipDate, 0, 4);

        HBox actions = new HBox(12);
        actions.setPadding(margin);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        JFXSnackbar confirmation = new JFXSnackbar(root);
        JFXSnackbar failure = new JFXSnackbar(content);

        JFXButton saveAdd = new JFXButton("SAVE");
        saveAdd.setId("raised-button");
        saveAdd.setOnAction(e -> {
            boolean eventAdded = logic.addEvent(nameField.getText(), weightField.getText(), recurring.selectedProperty().get(), datePicker, timePicker, skipDate, occurrence, offset, courseID);
            if (eventAdded) {
                upcomingEvents.setPredicate(item -> LocalDateTime.now().isBefore(LocalDateTime.parse(item.dueDateProperty().get(), df)));
                pastEvents.setPredicate(item -> LocalDateTime.now().isAfter(LocalDateTime.parse(item.dueDateProperty().get(), df)));
                dialog.close();
                confirmation.enqueue(getSnackbarEvent(root, "Event added, refresh to view the new event", 0.64, 0.08));
            } else {
                failure.enqueue(getSnackbarEvent(content, "Invalid data, please try again", 0.64, 0.12));
            }
        });

        actions.getChildren().add(saveAdd);

        layout.setHeading(headerText);
        layout.setBody(content);
        layout.setActions(actions);

        layout.prefWidthProperty().bind(root.widthProperty().multiply(0.5));
        layout.prefHeightProperty().bind(root.heightProperty().multiply(0.8));

        return dialog;
    }

    // CALENDAR
    JFXTabPane getCalendarPane() {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(104);
        tabs.setTabMaxWidth(104);
        tabs.setTabMinHeight(24);

        addTab(tabs, "Month", getCalendarMonthContent());
        addTab(tabs, "Week", new Label("Not implemented yet."));
        return tabs;
    }

    StackPane getCalendarMonthContent() {
        StackPane root = new StackPane();
        root.setPadding(largerMargin);
        root.setAlignment(Pos.TOP_CENTER);

        LocalDate today = LocalDateTime.now().toLocalDate();
        AtomicInteger year = new AtomicInteger(today.getYear());
        AtomicInteger month = new AtomicInteger(today.getMonth().getValue());
        AtomicReference<LocalDate> firstDay = new AtomicReference<>(LocalDate.of(year.get(), month.get(), 1).with(TemporalAdjusters.firstDayOfMonth()));
        AtomicReference<LocalDate> lastDay = new AtomicReference<>(LocalDate.of(year.get(), month.get(), 1).with(TemporalAdjusters.lastDayOfMonth()));

        AtomicInteger firstWeek = new AtomicInteger(firstDay.get().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        AtomicInteger lastWeek = new AtomicInteger(lastDay.get().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        AtomicInteger numberOfRows = new AtomicInteger(lastWeek.get() - firstWeek.get() + 1);
        assert (numberOfRows.get() > 0) : "Row number <= 0.";

        GridPane grid = new GridPane();
        grid.setId("calendar-grid");
        grid.setAlignment(Pos.CENTER);

        HBox header = new HBox();
        header.setSpacing(12);
        header.setPadding(new Insets(0, 24, 0, 24));
        header.setAlignment(Pos.CENTER_LEFT);

        Text headerName = new Text(Month.of(month.get()).getDisplayName(TextStyle.FULL, new Locale("en")) + " " + year);
        headerName.setId("content-supersize");

        JFXButton leftButton = new JFXButton();
        leftButton.setGraphic(new FontIcon());
        leftButton.setId("left-button");

        JFXButton rightButton = new JFXButton();
        rightButton.setGraphic(new FontIcon());
        rightButton.setId("right-button");

        header.getChildren().addAll(Arrays.asList(leftButton, rightButton, headerName));
        grid.add(header, 0, 0, 7, 1);
        grid.getRowConstraints().addAll(Arrays.asList(new RowConstraints(52), new RowConstraints(24)));

        setCalendarMonthWeekHeader(grid);

        AtomicInteger gridRowNumber = new AtomicInteger(2);
        AtomicInteger gridColumnNumber = new AtomicInteger();
        if (firstDay.get().getDayOfWeek().getValue() == 7) {
            gridColumnNumber.set(0);
        } else {
            gridColumnNumber.set(firstDay.get().getDayOfWeek().getValue());
        }

        AtomicReference<Map<Integer, ObservableList<ObservableEvent>>> events = new AtomicReference<>(logic.getEventsOfMonth(LocalDate.of(year.get(), month.get(), 1)));
        getCalendarMonthContentHelper(grid, LocalDate.now(), events.get(), lastDay.get().getDayOfMonth(), gridColumnNumber.get(), gridRowNumber.get());

        leftButton.setOnAction(click -> {
            if (month.get() == 1) {
                year.getAndDecrement();
                month.set(12);
            } else {
                month.set(month.get() - 1);
            }

            calendarMonthGridUpdate(year, month, firstDay, lastDay, firstWeek, lastWeek, numberOfRows, grid, header, headerName, gridRowNumber, gridColumnNumber, events);
        });

        rightButton.setOnAction(click -> {
            if (month.get() == 12) {
                year.getAndIncrement();
                month.set(1);
            } else {
                month.set(month.get() + 1);
            }

            calendarMonthGridUpdate(year, month, firstDay, lastDay, firstWeek, lastWeek, numberOfRows, grid, header, headerName, gridRowNumber, gridColumnNumber, events);
        });


        JFXDepthManager.setDepth(grid, 1);
        root.getChildren().add(grid);

        return root;
    }

    private void calendarMonthGridUpdate(AtomicInteger year, AtomicInteger month, AtomicReference<LocalDate> firstDay, AtomicReference<LocalDate> lastDay, AtomicInteger firstWeek, AtomicInteger lastWeek, AtomicInteger numberOfRows, GridPane grid, HBox header, Text headerName, AtomicInteger gridRowNumber, AtomicInteger gridColumnNumber, AtomicReference<Map<Integer, ObservableList<ObservableEvent>>> events) {
        firstDay.set(LocalDate.of(year.get(), month.get(), 1).with(TemporalAdjusters.firstDayOfMonth()));
        lastDay.set(LocalDate.of(year.get(), month.get(), 1).with(TemporalAdjusters.lastDayOfMonth()));
        firstWeek.set(firstDay.get().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        lastWeek.set(lastDay.get().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        numberOfRows.set(lastWeek.get() - firstWeek.get() + 1);

        events.set(logic.getEventsOfMonth(LocalDate.of(year.get(), month.get(), 1)));

        gridRowNumber.set(2);
        if (firstDay.get().getDayOfWeek().getValue() == 7) {
            gridColumnNumber.set(0);
        } else {
            gridColumnNumber.set(firstDay.get().getDayOfWeek().getValue());
        }

        grid.getChildren().clear();
        headerName.setText(Month.of(month.get()).getDisplayName(TextStyle.FULL, new Locale("en")) + " " + year);

        grid.add(header, 0, 0, 7, 1);
        setCalendarMonthWeekHeader(grid);

        getCalendarMonthContentHelper(grid, firstDay.get(), events.get(), lastDay.get().getDayOfMonth(), gridColumnNumber.get(), gridRowNumber.get());
    }

    private void getCalendarMonthContentHelper(GridPane grid, LocalDate date, Map<Integer, ObservableList<ObservableEvent>> events, int lastDay, int gridColumnNumber, int gridRowNumber) {
        for (int i = 1; i <= lastDay; i++) {

            VBox container = new VBox();
            container.setId("calendar-date");
            container.prefWidthProperty().bind(sc.widthProperty().multiply(0.12));
            container.prefHeightProperty().bind(sc.heightProperty().multiply(0.20));

            StackPane base = new StackPane();
            Circle circle = new Circle(12);
            Text text = getTextNormal(String.valueOf(i));
            text.setBoundsType(TextBoundsType.VISUAL);
            if (LocalDate.of(date.getYear(), date.getMonth().getValue(), i).equals(LocalDate.now())) {
                circle.setId("calendar-circle-today");
            } else {
                circle.setId("calendar-circle-regular");
            }
            base.getChildren().addAll(Arrays.asList(circle, text));
            container.getChildren().add(base);

            // fill in the VBox
            if (events.containsKey(i)) {
                events.get(i).sort(Comparator.comparing(o -> o.dueDateProperty().get()));
                for (ObservableEvent event : events.get(i)) {
                    HBox item = new HBox();
                    item.setId("calendar-item");

                    Label eventText = new Label(event.courseCodeProperty().get() + " " + event.nameProperty().get());
                    eventText.setId("calendar-event");

                    JFXPopup popup = new JFXPopup();
                    StackPane stack = new StackPane();
                    stack.setPadding(margin);
                    stack.setAlignment(Pos.CENTER_LEFT);
                    stack.minWidthProperty().bind(sc.widthProperty().multiply(0.12));
                    stack.getChildren().add(new Text(event.courseCodeProperty().get() + " " + event.nameProperty().get() + " @ " + event.dueDateProperty().get().split(" ")[1]));
                    popup.setPopupContent(stack);

                    eventText.hoverProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal) {
                            popup.show(eventText);
                        } else {
                            popup.hide();
                        }
                    });
                    item.getChildren().add(eventText);
                    container.getChildren().add(item);
                }
            }

            grid.add(container, gridColumnNumber, gridRowNumber);
            gridColumnNumber++;
            if (gridColumnNumber == 7) {
                gridColumnNumber = 0;
                gridRowNumber++;
            }
        }
    }

    private void setCalendarMonthWeekHeader(GridPane grid) {
        List<String> daysOfWeek = Arrays.asList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT");
        for (int i = 0; i < daysOfWeek.size(); i++) {
            Text dayOfWeek = new Text(daysOfWeek.get(i));
            dayOfWeek.setId("calendar-week-header");
            HBox header = new HBox();
            header.getChildren().add(dayOfWeek);
            header.setAlignment(Pos.CENTER);
            grid.add(header, i, 1);
        }
    }
    // textfields and selectors

    /**
     * Returns a JFXTextField with labelFloat set to true and prompt text set to the input prompt text, with its width bound to the input Pane specified by the input bindRatio.
     * If numericFilter is set to true, a numeric filter is also set such that only numeric values can be entered.
     * <p>
     * Note that the numeric filter is unable to catch input cases of ending with a . (period), so this edge case should be accounted for when using it as a field for numbers.
     *
     * @param root          the Pane to bind the JFXTextField's width to
     * @param promptText    prompt text for the JFXTextField
     * @param bindRatio     the relative ratio of the width of the JFXTextField relative to the pane
     * @param numericFilter whether a numeric filter should be set to this JFXTextField
     * @return a JFXTextField
     */
    JFXTextField getTextField(Pane root, String promptText, double bindRatio, boolean numericFilter) {
        JFXTextField field = new JFXTextField();
        field.setPromptText(promptText);
        field.prefWidthProperty().bind(root.widthProperty().multiply(bindRatio));
        field.setLabelFloat(true);
        if (numericFilter) {
            field.setTextFormatter(new TextFormatter<>(getNumericFilter()));
        }
        return field;
    }

    /**
     * Returns a TextFormatter filter that prevents non-numeric-related values from being entered.
     *
     * @return a filter for use with TextFormatter
     */
    UnaryOperator<TextFormatter.Change> getNumericFilter() {
        return input -> {
            if (input.isAdded()) {
                String addedText = input.getText();
                String newText = input.getControlNewText();
                if (newText.matches("\\d{1,3}[.]?(\\d{1,2})?")) {
                    return input;
                }

                int length = addedText.length();
                addedText = "";
                input.setText(addedText);

                // modify caret position if size of text changed:
                int delta = -length;
                input.setCaretPosition(input.getCaretPosition() + delta);
                input.setAnchor(input.getAnchor() + delta);
            }
            return input;
        };
    }

    JFXDatePicker getDatePicker(StackPane root, String promptText, double bindRatio) {
        JFXDatePicker date = new JFXDatePicker();
        date.setDialogParent(root);
        date.setId("date-picker");
        date.setPromptText(promptText);
        date.prefWidthProperty().bind(root.widthProperty().multiply(bindRatio));

        return date;
    }

    JFXTimePicker getTimePicker(StackPane root, String promptText, double bindRatio) {
        JFXTimePicker time = new JFXTimePicker();
        time.setOverLay(true);
        time.setDialogParent(root);
        time.setId("time-picker");
        time.setPromptText(promptText);
        time.prefWidthProperty().bind(root.widthProperty().multiply(bindRatio));

        return time;
    }

    JFXComboBox<String> getComboBox(StackPane root, String promptText, double bindRatio) {
        JFXComboBox<String> combos = new JFXComboBox<>();
        combos.setPromptText(promptText);
        combos.prefWidthProperty().bind(root.widthProperty().multiply(bindRatio));

        return combos;
    }

    JFXToggleButton getToggleButton(String text) {
        JFXToggleButton button = new JFXToggleButton();
        button.setSize(9);
        button.setText(text);

        return button;
    }

    // misc and text

    Text getTextH2(String string) {
        Text text = new Text(string);
        text.setId("text-h2");
        return text;
    }

    Text getTextNormal(String string) {
        Text text = new Text(string);
        text.setId("text-normal");
        return text;
    }

    void setAnchorsCreate(Node layout, Node fab) {
        AnchorPane.setRightAnchor(layout, 0.0);
        AnchorPane.setLeftAnchor(layout, 0.0);
        AnchorPane.setTopAnchor(layout, 0.0);
        AnchorPane.setBottomAnchor(layout, 0.0);

        if (fab != null) {
            AnchorPane.setRightAnchor(fab, 35.0);
            AnchorPane.setBottomAnchor(fab, 35.0);
        }
    }

    JFXSnackbar.SnackbarEvent getSnackbarEvent(Pane root, String text, double hBindRatio, double vBindRatio) {
        HBox content = new HBox();
        content.prefWidthProperty().bind(root.widthProperty().multiply(hBindRatio));
        content.prefHeightProperty().bind(root.heightProperty().multiply(vBindRatio));
        content.setPadding(margin);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().add(new Text(text));
        content.setId("snackbar");

        return new JFXSnackbar.SnackbarEvent(content, Duration.seconds(3.33), null);
    }

    JFXButton getJFXButton(Node graphic, String cssID, ButtonType buttonType) {
        JFXButton button = new JFXButton("");
        button.setGraphic(graphic);
        button.setId(cssID);
        if (buttonType.equals(ButtonType.FLOATING)) {
            button.getStyleClass().add("animated-option-button");
        } else if (buttonType.equals(ButtonType.FLOATING_SUB)) {
            button.getStyleClass().add("animated-option-button-sub");
        }
        return button;
    }

    <S, T> TableColumn<S, T> getTableColumn(String headerText, String propertyValue, Region widthBinder, double widthBoundRatio) {
        TableColumn<S, T> column = headerText.equals("") ? new TableColumn<>() : new TableColumn<>(headerText);
        if (!propertyValue.equals("")) {
            column.setCellValueFactory(new PropertyValueFactory<>(propertyValue));
        }
        column.prefWidthProperty().bind(widthBinder.widthProperty().multiply(widthBoundRatio));
        return column;
    }
}
