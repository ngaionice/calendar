package ui;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

public class PresenterElements {

    PresenterLogic logic;

    Background testBackground = new Background(new BackgroundFill(Color.rgb(27, 27, 27), CornerRadii.EMPTY, Insets.EMPTY));
    Background accentBackground = new Background(new BackgroundFill(Color.rgb(255, 152, 0), CornerRadii.EMPTY, Insets.EMPTY));
    Background accentButtonBackground = new Background(new BackgroundFill(Color.rgb(255, 152, 0), new CornerRadii(3), Insets.EMPTY));
    Background whiteLightBackground = new Background(new BackgroundFill(Color.rgb(238, 238, 238), new CornerRadii(3), Insets.EMPTY));

    Insets margin = new Insets(8);
    Insets mediumMargin = new Insets(16);
    Insets largerMargin = new Insets(24);

    Paint white = Paint.valueOf("#FFFFFF");
    Paint focus = Paint.valueOf("#FF9800");
    Paint focusLight = Paint.valueOf("#FFB74D");
    Paint whiteLight = Paint.valueOf("#EEEEEE");
    Paint gray = Paint.valueOf("#4D4D4D");

    public PresenterElements(PresenterLogic logic) {
        this.logic = logic;
    }

    VBox getNav(Scene sc, BorderPane content) {
        VBox box = new VBox();

        List<JFXButton> mainButtons = Arrays.asList(new JFXButton("Calendar"), new JFXButton("Courses"),
                new JFXButton("Upcoming"), new JFXButton("Archive"), new JFXButton("Settings"));
        List<String> buttonIDs = Arrays.asList("calendar", "courses", "upcoming", "archive", "settings");

        for (int i = 0; i < mainButtons.size(); i++) {
            JFXButton item = mainButtons.get(i);

            item.setMaxSize(Double.MAX_VALUE, 48);
            item.setPrefHeight(36);
            item.setPadding(new Insets(0, 8, 0, 8));
            item.setAlignment(Pos.CENTER_LEFT);
            item.setGraphic(new FontIcon());
            item.setGraphicTextGap(16);
            item.setTextFill(white);

            item.setId(buttonIDs.get(i));
        }

        Region spacer = new Region();
        spacer.prefHeightProperty().bind(sc.heightProperty().multiply(0.125));

        mainButtons.get(0).setOnAction(event -> {
            content.setCenter(getCalendarPane(sc, logic));
            content.setTop(getHeader(sc, "Calendar"));
        });

        mainButtons.get(1).setOnAction(event -> {
            content.setCenter(getCoursesPane(sc, logic));
            content.setTop(getHeader(sc, "Courses"));
        });

        mainButtons.get(2).setOnAction(event -> {
            content.setCenter(getEventsPane(sc, logic));
            content.setTop(getHeader(sc, "Upcoming"));
        });

        mainButtons.get(3).setOnAction(event -> {
            content.setCenter(getArchivePane(sc, logic));
            content.setTop(getHeader(sc, "Archive"));
        });

        mainButtons.get(4).setOnAction(event -> {
            content.setCenter(getTabPane());
            content.setTop(getHeader(sc, "Settings"));
        });

        box.getChildren().add(spacer);
        box.getChildren().addAll(mainButtons);
        box.setMaxHeight(Double.MAX_VALUE);
        box.setPadding(margin);
        box.prefWidthProperty().bind(sc.widthProperty().multiply(0.15));
        box.setBackground(testBackground);
        box.setSpacing(8);

        return box;
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

    HBox getHeader(Scene sc, String headerText) {
        HBox root = new HBox();
        root.setBackground(accentBackground);
        root.prefHeightProperty().bind(sc.heightProperty().multiply(0.07));
        root.setPadding(largerMargin);
        root.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().add(getTextH2(headerText, "#FFFFFF"));

        return root;
    }

    JFXTabPane getCoursesPane(Scene sc, PresenterLogic logic) {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(104);
        tabs.setTabMaxWidth(104);
        tabs.setTabMinHeight(24);

        addTab(tabs, "Overview", getCourseOverviewContent(sc, logic));

        ObservableList<ObservableCourse> courses = logic.getCourses(true);

        for (ObservableCourse course : courses) {
            String code = course.codeProperty().get();
            addTab(tabs, code, getCourseContent(sc, course, logic));
        }
        return tabs;
    }

    StackPane getCourseContent(Scene sc, ObservableCourse course, PresenterLogic logic) {
        StackPane root = new StackPane();
        root.setPadding(largerMargin);

        GridPane grid = new GridPane();
        grid.setPadding(mediumMargin);
        grid.setBackground(testBackground);
        grid.setVgap(12);

        root.getChildren().add(grid);
        JFXDepthManager.setDepth(grid, 1);

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

        TableView<ObservableEvent> upcoming = new TableView<>();
        upcoming.prefWidthProperty().bind(grid.widthProperty());
        upcoming.prefHeightProperty().bind(sc.heightProperty().multiply(0.5));
        upcoming.setFixedCellSize(48);

        TableView<ObservableEvent> past = new TableView<>();
        past.prefWidthProperty().bind(grid.widthProperty());
        past.prefHeightProperty().bind(sc.heightProperty().multiply(0.3));
        past.setFixedCellSize(48);

        TableColumn<ObservableEvent, String> nameColU = new TableColumn<>("Upcoming events");
        TableColumn<ObservableEvent, String> dueDateColU = new TableColumn<>("Due date");
        TableColumn<ObservableEvent, Double> weightColU = new TableColumn<>("Weight");

        TableColumn<ObservableEvent, String> nameCol = new TableColumn<>("Past events");
        TableColumn<ObservableEvent, String> dueDateCol = new TableColumn<>("Due date");
        TableColumn<ObservableEvent, Double> markCol = new TableColumn<>("Mark");
        TableColumn<ObservableEvent, Double> weightCol = new TableColumn<>("Weight");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        markCol.setCellValueFactory(new PropertyValueFactory<>("mark"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

        nameColU.setCellValueFactory(new PropertyValueFactory<>("name"));
        dueDateColU.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        weightColU.setCellValueFactory(new PropertyValueFactory<>("weight"));

        nameCol.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.56));
        dueDateCol.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.24));
        weightCol.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.12));
        markCol.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.04));

        nameColU.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.56));
        dueDateColU.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.24));
        weightColU.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.16));

        List<TableColumn<ObservableEvent, ?>> items = Arrays.asList(nameCol, dueDateCol, weightCol, markCol, nameColU, dueDateColU, weightColU);
        items.forEach(item -> {
            item.getStyleClass().add("text-col");
            item.setResizable(false);
        });

        upcoming.getColumns().addAll(items.subList(4, 7));
        past.getColumns().addAll(items.subList(0, 4));

        upcoming.setItems(logic.getCourseEvents(course.idProperty().get(), true));
        past.setItems(logic.getCourseEvents(course.idProperty().get(), false));

        upcoming.getSortOrder().add(dueDateColU);
        past.getSortOrder().add(dueDateCol);

        grid.add(infoHeader, 0, 0);
        grid.add(upcoming, 0, 1);
        grid.add(past, 0, 2);

        return root;
    }

    StackPane getCourseOverviewContent(Scene sc, PresenterLogic logic) {
        StackPane root = new StackPane();
        root.setPadding(largerMargin);

        AnchorPane anchor = new AnchorPane();
        TableView<ObservableCourse> table = new TableView<>();

        table.prefWidthProperty().bind(sc.widthProperty().multiply(0.8));
        table.prefHeightProperty().bind(sc.heightProperty().multiply(0.8));
        table.setFixedCellSize(48);
        table.setPadding(mediumMargin);

        TableColumn<ObservableCourse, String> codeCol = new TableColumn<>("Course code");
        TableColumn<ObservableCourse, String> nameCol = new TableColumn<>("Course name");
        TableColumn<ObservableCourse, Double> avgCol = new TableColumn<>("Average");
        TableColumn<ObservableCourse, String> nextEventCol = new TableColumn<>("Next event");
        TableColumn<ObservableCourse, Double> nextDueCol = new TableColumn<>("Due date");

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        avgCol.setCellValueFactory(new PropertyValueFactory<>("avg"));
        nextEventCol.setCellValueFactory(new PropertyValueFactory<>("nextEvent"));
        nextDueCol.setCellValueFactory(new PropertyValueFactory<>("nextDue"));

        codeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.12));
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.28));
        nextEventCol.prefWidthProperty().bind(table.widthProperty().multiply(0.28));
        nextDueCol.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
        avgCol.prefWidthProperty().bind(table.widthProperty().multiply(0.12));

        List<TableColumn<ObservableCourse, ?>> items = Arrays.asList(codeCol, nameCol, nextEventCol, nextDueCol, avgCol);
        items.forEach(item -> {
            item.getStyleClass().add("text-col");
            item.setResizable(false);
        });

        ObservableList<ObservableCourse> courseData = logic.getCourses(true);

        table.getColumns().addAll(items);
        table.setItems(courseData);

        table.getSortOrder().add(codeCol);

        JFXButton addCourse = new JFXButton("");
        addCourse.setGraphic(new FontIcon());
        addCourse.setId("add");
        addCourse.getStyleClass().add("animated-option-button");

        addCourse.setOnAction(click -> {
            JFXDialog addDialog = getCourseCreateDialog(root, 0.8, 0.9);
            root.getChildren().add(addDialog);
            addDialog.show();
        });

        JFXNodesList fab = new JFXNodesList();
        fab.addAnimatedNode(addCourse);
        setAnchorsCreate(table, fab);
        JFXDepthManager.setDepth(anchor, 1);

        anchor.getChildren().add(table);
        anchor.getChildren().add(fab);
        root.getChildren().add(anchor);
        return root;
    }

    JFXDialog getCourseCreateDialog(StackPane root, double hBindRatio, double vBindRatio) {
        JFXDialogLayout layout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, layout, JFXDialog.DialogTransition.CENTER);
        layout.setId("dialog");

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
        String[] marks = new String[7];
        Boolean[] recurrings = new Boolean[7];

        JFXTextField field1 = getTextField(content, "Event name", 0.3, false);
        JFXTextField marks1 = getTextField(content, "Weight", 0.08, true);
        JFXToggleButton button1 = getToggleButton("Recurring?");

        field1.textProperty().addListener((observable, oldValue, newValue) -> eventNames[0] = newValue);
        marks1.textProperty().addListener((observable, oldValue, newValue) -> marks[0] = newValue);
        button1.selectedProperty().addListener((observable, oldValue, newValue) -> recurrings[0] = newValue);

        HBox first = new HBox();
        first.getChildren().addAll(Arrays.asList(field1, marks1, button1));
        first.setSpacing(16);
        first.setAlignment(Pos.CENTER_LEFT);

        rows[0] = first;

        addEvent.setOnAction(e -> {
            if (items.get() < 7) {
                int i = items.get();

                JFXTextField nameField = getTextField(content, "Event name", 0.3, false);
                nameField.textProperty().addListener((observable, oldValue, newValue) -> eventNames[i] = newValue);
                JFXTextField markField = getTextField(content, "Weight", 0.08, true);
                markField.textProperty().addListener((observable, oldValue, newValue) -> marks[i] = newValue);
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

                box.getChildren().addAll(Arrays.asList(getTextNormal(eventNames[i], white),
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
            boolean saved = logic.verifyAndAddCourse(courseName.get(), courseCode.get(), eventNames, marks, recurrings,
                    dates, times, skipDates, occurrences, offsets, grid3.getRowConstraints().size());
            if (saved) {
                dialog.close();
                root.getChildren().remove(dialog);
                confirmation.enqueue(getSnackbarEvent(root, "Course added, please refresh the tab to see changes", 0.64, 0.08));
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

    JFXTabPane getEventsPane(Scene sc, PresenterLogic logic) {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(104);
        tabs.setTabMaxWidth(104);
        tabs.setTabMinHeight(24);

        addTab(tabs, "View", getEventOverviewContent(sc, logic, true));
        return tabs;
    }

    StackPane getEventOverviewContent(Scene sc, PresenterLogic logic, boolean isUpcoming) {
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
        weightCol.prefWidthProperty().bind(table.widthProperty().multiply(0.24));

        List<TableColumn<ObservableEvent, ?>> columns = new ArrayList<>(Arrays.asList(dueDateCol, codeCol, nameCol, weightCol));
        if (!isUpcoming) {
            TableColumn<ObservableEvent, Double> markCol = new TableColumn<>("Mark");
            markCol.setCellValueFactory(new PropertyValueFactory<>("mark"));
            weightCol.prefWidthProperty().bind(table.widthProperty().multiply(0.12));
            markCol.prefWidthProperty().bind(table.widthProperty().multiply(0.12));
            columns.add(markCol);
        }
        columns.forEach(item -> {
            item.getStyleClass().add("text-col");
            item.setResizable(false);
        });

        ObservableList<ObservableEvent> events = FXCollections.observableArrayList();
        Map<String, String> courseInfo = logic.getAllCourseInfo();
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

    JFXTabPane getArchivePane(Scene sc, PresenterLogic logic) {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(104);
        tabs.setTabMaxWidth(104);
        tabs.setTabMinHeight(24);

        addTab(tabs, "Events", getEventOverviewContent(sc, logic, false));
        addTab(tabs, "Courses", new Label("Not implemented yet."));
        return tabs;
    }

    JFXTabPane getCalendarPane(Scene sc, PresenterLogic logic) {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(104);
        tabs.setTabMaxWidth(104);
        tabs.setTabMinHeight(24);

        addTab(tabs, "Month", getCalendarMonthContent(sc, logic));
        addTab(tabs, "Week", new Label("Not implemented yet."));
        return tabs;
    }

    StackPane getCalendarMonthContent(Scene sc, PresenterLogic logic) {
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
        grid.setPadding(mediumMargin);
        grid.setBackground(testBackground);
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
        getCalendarMonthContentHelper(sc, grid, LocalDate.now(), events.get(), lastDay.get().getDayOfMonth(), gridColumnNumber.get(), gridRowNumber.get());

        leftButton.setOnAction(click -> {
            if (month.get() == 1) {
                year.getAndDecrement();
                month.set(12);
            } else {
                month.set(month.get() - 1);
            }

            calendarMonthGridUpdate(sc, logic, year, month, firstDay, lastDay, firstWeek, lastWeek, numberOfRows, grid, header, headerName, gridRowNumber, gridColumnNumber, events);
        });

        rightButton.setOnAction(click -> {
            if (month.get() == 12) {
                year.getAndIncrement();
                month.set(1);
            } else {
                month.set(month.get() + 1);
            }

            calendarMonthGridUpdate(sc, logic, year, month, firstDay, lastDay, firstWeek, lastWeek, numberOfRows, grid, header, headerName, gridRowNumber, gridColumnNumber, events);
        });


        JFXDepthManager.setDepth(grid, 1);
        root.getChildren().add(grid);

        return root;
    }

    private void calendarMonthGridUpdate(Scene sc, PresenterLogic logic, AtomicInteger year, AtomicInteger month, AtomicReference<LocalDate> firstDay, AtomicReference<LocalDate> lastDay, AtomicInteger firstWeek, AtomicInteger lastWeek, AtomicInteger numberOfRows, GridPane grid, HBox header, Text headerName, AtomicInteger gridRowNumber, AtomicInteger gridColumnNumber, AtomicReference<Map<Integer, ObservableList<ObservableEvent>>> events) {
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

        getCalendarMonthContentHelper(sc, grid, firstDay.get(), events.get(), lastDay.get().getDayOfMonth(), gridColumnNumber.get(), gridRowNumber.get());
    }

    private void getCalendarMonthContentHelper(Scene sc, GridPane grid, LocalDate date, Map<Integer, ObservableList<ObservableEvent>> events, int lastDay, int gridColumnNumber, int gridRowNumber) {
        for (int i = 1; i <= lastDay; i++) {

            VBox container = new VBox();
            container.setId("calendar-date");
            container.prefWidthProperty().bind(sc.widthProperty().multiply(0.12));
            container.prefHeightProperty().bind(sc.heightProperty().multiply(0.16));
            container.maxHeightProperty().bind(sc.heightProperty().multiply(0.16));

            StackPane base = new StackPane();
            Circle circle = new Circle(12);
            Text text = getTextNormal(String.valueOf(i), white);
            text.setBoundsType(TextBoundsType.VISUAL);
            if (LocalDate.of(date.getYear(), date.getMonth().getValue(), i).equals(LocalDate.now())) {
                circle.setFill(focus);
            } else {
                circle.setFill(Paint.valueOf("#1b1b1b"));
            }
            base.getChildren().addAll(Arrays.asList(circle, text));
            container.getChildren().add(base);

            // fill in the VBox
            if (events.containsKey(i)) {
                for (ObservableEvent event : events.get(i)) {
                    HBox item = new HBox();
                    item.setId("calendar-item");

                    Label eventText = new Label(event.courseCodeProperty().get() + " " + event.nameProperty().get());
                    eventText.setId("calendar-event");
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
        date.setDefaultColor(focus);
        date.setOverLay(true);
        date.setDialogParent(root);
        date.setId("date-picker");
        date.setPromptText(promptText);
        date.prefWidthProperty().bind(root.widthProperty().multiply(bindRatio));

        return date;
    }

    JFXTimePicker getTimePicker(StackPane root, String promptText, double bindRatio) {
        JFXTimePicker time = new JFXTimePicker();
        time.setDefaultColor(focus);
        time.set24HourView(true);
        time.setOverLay(true);
        time.setDialogParent(root);
        time.setId("time-picker");
        time.setPromptText(promptText);
        time.prefWidthProperty().bind(root.widthProperty().multiply(bindRatio));

        return time;
    }

    JFXComboBox<String> getComboBox(StackPane root, String promptText, double bindRatio) {
        JFXComboBox<String> combos = new JFXComboBox<>();
        combos.setFocusColor(focus);
        combos.setPromptText(promptText);
        combos.prefWidthProperty().bind(root.widthProperty().multiply(bindRatio));

        return combos;
    }

    JFXToggleButton getToggleButton(String text) {
        JFXToggleButton button = new JFXToggleButton();
        button.setToggleColor(focus);
        button.setUnToggleColor(white);
        button.setToggleLineColor(focusLight);
        button.setUnToggleLineColor(whiteLight);
        button.setSize(9);
        button.setText(text);

        return button;
    }

    // misc and text

    Text getTextH2(String string, String color) {
        Text text = new Text(string);
        text.setFont(Font.font("Roboto Medium", 15));
        text.setFill(Paint.valueOf(color));
        return text;
    }

    Text getTextNormal(String string, Paint color) {
        Text text = new Text(string);
        text.setFont(Font.font("Roboto Regular", 12));
        text.setFill(color);

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
}
