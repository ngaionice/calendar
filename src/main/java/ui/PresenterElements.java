package ui;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

public class PresenterElements {

    PresenterLogic logic;

    Background testBackground = new Background(new BackgroundFill(Color.rgb(27, 27, 27), CornerRadii.EMPTY, Insets.EMPTY));
    Background accentBackground =  new Background(new BackgroundFill(Color.rgb(255, 152, 0), CornerRadii.EMPTY, Insets.EMPTY));
    Background accentButtonBackground =  new Background(new BackgroundFill(Color.rgb(255, 152, 0), new CornerRadii(3), Insets.EMPTY));
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
            content.setCenter(getTabPane());
            content.setTop(getHeader(sc, "Calendar"));
        });

        mainButtons.get(1).setOnAction(event -> {
            content.setCenter(getCoursesPane(sc, logic));
            content.setTop(getHeader(sc, "Courses"));
        });

        mainButtons.get(2).setOnAction(event -> {
            content.setCenter(getTabPane());
            content.setTop(getHeader(sc, "Upcoming"));
        });

        mainButtons.get(3).setOnAction(event -> {
            content.setCenter(getCoursesPane(sc, logic));
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
        tabs.setTabMinWidth(100);
        tabs.setTabMaxWidth(100);

        addTab(tabs, "CSC263", new Label("Blank Calendar"));
        addTab(tabs, "CSC343", new Label("Blank Courses"));
        addTab(tabs, "STA305", new Label("Blank Events"));
        addTab(tabs, "STA355", new Label("Blank Add Menu"));
        addTab(tabs, "PCL201", new Label("Blank Settings"));

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
        tabs.setTabMinWidth(100);
        tabs.setTabMaxWidth(100);

        addTab(tabs, "Overview", getCourseOverviewContent(sc, logic));

        ObservableList<ObservableCourse> courses = logic.getCourses(true);
//        courses.add(new ObservableCourse("Test course 1", "test1",89.0, "Lecture", LocalDateTime.now()));
//        courses.add(new ObservableCourse("Test course 2", "test2",80.0, "Assignment 1", LocalDateTime.of(2019, 10, 12, 10, 30)));

        for (ObservableCourse course: courses) {
            String name = course.nameProperty().get();
            addTab(tabs, name, getCourseContent(sc, course, logic));
        }


//        addTab(tabs, "CSC263", new Label("Blank 263"));
//        addTab(tabs, "CSC343", new Label("Blank 343"));
//        addTab(tabs, "STA305", new Label("Blank 305"));
//        addTab(tabs, "STA355", new Label("Blank 355"));
//        addTab(tabs, "PCL201", new Label("Blank 201"));

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

        TableColumn<ObservableEvent, String> nameCol = new TableColumn<>("Past events");
        TableColumn<ObservableEvent, String> dueDateCol = new TableColumn<>("Due date");
        TableColumn<ObservableEvent, Double> markCol = new TableColumn<>("Mark");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        markCol.setCellValueFactory(new PropertyValueFactory<>("mark"));

        TableColumn<ObservableEvent, String> nameColU = new TableColumn<>("Upcoming events");
        TableColumn<ObservableEvent, String> dueDateColU = new TableColumn<>("Due date");

        nameColU.setCellValueFactory(new PropertyValueFactory<>("name"));
        dueDateColU.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        nameCol.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.56));
        dueDateCol.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.32));
        markCol.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.16));

        nameColU.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.56));
        dueDateColU.prefWidthProperty().bind(upcoming.widthProperty().multiply(0.32));

        List<TableColumn<ObservableEvent, ?>> items = Arrays.asList(nameCol, dueDateCol, markCol, nameColU, dueDateColU);
        items.forEach(item -> {
            item.getStyleClass().add("text-col");
            item.setResizable(false);
        });

        upcoming.getColumns().addAll(items.subList(3, 5));
        past.getColumns().addAll(items.subList(0,3));

        upcoming.setItems(logic.getCourseEvents(course.idProperty().get(), false));
        past.setItems(logic.getCourseEvents(course.idProperty().get(), true));

        dueDateCol.setSortType(TableColumn.SortType.DESCENDING);
        dueDateColU.setSortType(TableColumn.SortType.ASCENDING);

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

        TableColumn<ObservableCourse, String> nameCol = new TableColumn<>("Course name");
        TableColumn<ObservableCourse, Double> avgCol = new TableColumn<>("Average");
        TableColumn<ObservableCourse, String> nextEventCol = new TableColumn<>("Next event");
        TableColumn<ObservableCourse, Double> nextDueCol = new TableColumn<>("Due date");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        avgCol.setCellValueFactory(new PropertyValueFactory<>("avg"));
        nextEventCol.setCellValueFactory(new PropertyValueFactory<>("nextEvent"));
        nextDueCol.setCellValueFactory(new PropertyValueFactory<>("nextDue"));

        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.32));
        nextEventCol.prefWidthProperty().bind(table.widthProperty().multiply(0.32));
        nextDueCol.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
        avgCol.prefWidthProperty().bind(table.widthProperty().multiply(0.12));

        List<TableColumn<ObservableCourse, ?>> items = Arrays.asList(nameCol, nextEventCol, nextDueCol, avgCol);
        items.forEach(item -> {
            item.getStyleClass().add("text-col");
            item.setResizable(false);
        });

        ObservableList<ObservableCourse> courseData = logic.getCourses(true);

        courseData.add(new ObservableCourse("Test course 1", "test1",89.0, "Lecture", LocalDateTime.now()));
        courseData.add(new ObservableCourse("Test course 2", "test2",80.0, "Assignment 1", LocalDateTime.of(2019, 10, 12, 10, 30)));

        table.getColumns().addAll(items);
        table.setItems(courseData);

        JFXButton addCourse = new JFXButton("");
        addCourse.setGraphic(new FontIcon());
        addCourse.setId("add");
        addCourse.getStyleClass().add("animated-option-button");


        JFXDialog addDialog = getCourseCreateDialog(root, 0.8, 0.9);
        root.getChildren().add(addDialog);

        addCourse.setOnAction(click -> addDialog.show());

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

        BorderPane pane = new BorderPane();
        StackPane content = new StackPane();
        content.prefWidthProperty().bind(pane.widthProperty());
        content.setPadding(mediumMargin);

        AtomicReference<String> courseName = new AtomicReference<>("");

        VBox nav = new VBox();
        nav.setPadding(margin);
        nav.prefWidthProperty().bind(pane.widthProperty().multiply(0.1));

        List<JFXButton> navButtons = Arrays.asList(new JFXButton("Step 1"), new JFXButton("Step 2"), new JFXButton("Step 3"));
        navButtons.forEach(item -> {
            item.setId("add-nav-button");
            item.setMaxWidth(Double.MAX_VALUE);
        });
        nav.getChildren().addAll(navButtons);

        // step 1
        JFXTextField courseNameField = getTextField(pane, "Course name", 0.16, false);
        courseNameField.maxWidthProperty().bind(pane.widthProperty().multiply(0.32));
        courseNameField.textProperty().addListener((observable, oldValue, newValue) -> courseName.set(newValue));

        // step 2
        GridPane events = new GridPane();
        events.getRowConstraints().add(new RowConstraints(52));
        Text eventHeaderText = new Text("Marks breakdown");
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
        Text step3HeaderText = new Text("Set due dates");
        step3HeaderText.setId("dialog-subheader");
        step3Header.getChildren().add(step3HeaderText);
        step3Header.setAlignment(Pos.CENTER_LEFT);
        step3Header.prefHeightProperty().bind(eventsHeader.heightProperty());
        step3Header.prefWidthProperty().bind(grid3.widthProperty().multiply(0.64));

        JFXDatePicker[] dates = new JFXDatePicker[7];
        JFXTimePicker[] times = new JFXTimePicker[7];
        JFXDatePicker[] skipDates = new JFXDatePicker[7];
        JFXTextField[] occurrences = new JFXTextField[7];
        JFXTextField[] offsets = new JFXTextField[7];

        // nav buttons config
        navButtons.get(0).setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(courseNameField);
        });
        navButtons.get(1).setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(events);
        });
        navButtons.get(2).setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(grid3);

            grid3.getChildren().clear();
            grid3.getRowConstraints().clear();
            grid3.add(step3Header, 0, 0);
            grid3.getRowConstraints().add(new RowConstraints(48));

            for (int i = 0; i < items.get(); i++) {
                HBox box = new HBox(16);
                box.setAlignment(Pos.CENTER_LEFT);
                box.prefWidthProperty().bind(grid3.widthProperty().multiply(0.96));

                boolean isRecurring = recurrings[i] != null && recurrings[i];
                dates[i] = getDatePicker(content, isRecurring ? "First due date" : "Due date", 0.24);
                times[i] = getTimePicker(content, "Due time", 0.20);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                box.getChildren().addAll(Arrays.asList(getTextNormal(eventNames[i] != null ? eventNames[i] : "Blank name", white),
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
        });

        HBox saveBox = new HBox();
        saveBox.setPadding(margin);
        JFXButton save = new JFXButton("SAVE");
        save.setId("save");

        JFXSnackbar confirmation = new JFXSnackbar(root);
        JFXSnackbar failure = new JFXSnackbar(content);

        save.setOnAction(event -> {
            boolean saved = logic.verifyAndAddCourse(courseName.get(), eventNames, marks, recurrings, dates, times, skipDates, occurrences, offsets, grid3.getRowConstraints().size());
            if (saved) {
                System.out.println("saved course");
                dialog.close();
                confirmation.enqueue(getSnackbarEvent(root, "Course added, please refresh the tab to see changes", 0.64, 0.08));
            } else {
                System.out.println("not saved");
                failure.enqueue(getSnackbarEvent(content, "Invalid data, please try again", 0.64, 0.12));
            }
        });
        saveBox.getChildren().add(save);

        Text header = new Text("Add course");
        header.setId("dialog-header");

        addEvent.setGraphic(new FontIcon());
        addEvent.setId("add-event");

        eventHeaderText.setFill(Paint.valueOf("#FFF"));
        eventsHeader.setAlignment(Pos.CENTER_LEFT);

        pane.setLeft(nav);
        pane.setCenter(content);

        layout.setHeading(header);
        layout.setBody(pane);
        layout.setActions(saveBox);

        layout.prefWidthProperty().bind(root.widthProperty().multiply(hBindRatio));
        layout.prefHeightProperty().bind(root.heightProperty().multiply(vBindRatio));

        return dialog;
    }

    // textfields and selectors

    /**
     * Returns a JFXTextField with labelFloat set to true and prompt text set to the input prompt text, with its width bound to the input Pane specified by the input bindRatio.
     * If numericFilter is set to true, a numeric filter is also set such that only numeric values can be entered.
     *
     * Note that the numeric filter is unable to catch a possible input case of having only a . (period), so this edge case should be accounted for when using it as a field for numbers.
     *
     * @param root the Pane to bind the JFXTextField's width to
     * @param promptText prompt text for the JFXTextField
     * @param bindRatio the relative ratio of the width of the JFXTextField relative to the pane
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
            field.textProperty().addListener(((observable, oldValue, newValue) -> {
                // this does not catch a singular . and hitting enter; this is accounted for in PresenterLogic.verifyAndAddCourse, and the value gets set to 0
                if ((!newValue.equals("") && newValue.charAt(0) == '.') || newValue.contains(".") && (newValue.indexOf(".") != newValue.lastIndexOf("."))) {
                    field.setText(oldValue);
                }
            }));
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
                if (addedText.matches("[0-9.]")) {
                    return input;
                }

                int length = addedText.length();
                addedText = addedText.replaceAll("[^0-9.]", "");
                input.setText(addedText);

                // modify caret position if size of text changed:
                int delta = addedText.length() - length ;
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
        text.setFont(Font.font("Roboto Medium",15));
        text.setFill(Paint.valueOf(color));
        return text;
    }

    Text getTextNormal(String string, Paint color) {
        Text text = new Text(string);
        text.setFont(Font.font("Roboto Regular",12));
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
