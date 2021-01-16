package ui;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.FXCollections;
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
            content.setCenter(getCoursesPane(sc, logic.getCourses(true)));
            content.setTop(getHeader(sc, "Courses"));
        });

        mainButtons.get(2).setOnAction(event -> {
            content.setCenter(getTabPane());
            content.setTop(getHeader(sc, "Upcoming"));
        });

        mainButtons.get(3).setOnAction(event -> {
            content.setCenter(getCourseCreateContent(sc));
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

    JFXTabPane getCoursesPane(Scene sc, ObservableList<ObservableCourse> courses) {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(100);
        tabs.setTabMaxWidth(100);

        addTab(tabs, "Overview", getCourseOverviewContent(sc, courses));
        addTab(tabs, "CSC263", new Label("Blank 263"));
        addTab(tabs, "CSC343", new Label("Blank 343"));
        addTab(tabs, "STA305", new Label("Blank 305"));
        addTab(tabs, "STA355", new Label("Blank 355"));
        addTab(tabs, "PCL201", new Label("Blank 201"));

        return tabs;
    }

//    JFXTabPane getAddPane(Scene sc) {
//        JFXTabPane tabs = new JFXTabPane();
//        tabs.setTabMinWidth(100);
//        tabs.setTabMaxWidth(100);
//
//        addTab(tabs, "Courses", getCourseCreate());
//        addTab(tabs, "Assignments", getEventCreate());
//
//        return tabs;
//    }

    StackPane getCourseOverviewContent(Scene sc, ObservableList<ObservableCourse> courseData) {
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
        TableColumn<ObservableCourse, String> nextEventCol = new TableColumn<>("Next assignment");
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

        courseData.add(new ObservableCourse("Test course 1", "test1",89.0, "Lecture", LocalDateTime.now()));
        courseData.add(new ObservableCourse("Test course 2", "test2",80.0, "Assignment 1", LocalDateTime.of(2019, 10, 12, 10, 30)));

        table.getColumns().addAll(items);
        table.setItems(courseData);

        JFXButton addCourse = new JFXButton("");
        addCourse.setGraphic(new FontIcon());
        addCourse.setId("add");
        addCourse.getStyleClass().add("animated-option-button");

        JFXNodesList fab = new JFXNodesList();
        fab.addAnimatedNode(addCourse);
        setAnchorsCreate(table, fab);
        JFXDepthManager.setDepth(anchor, 1);

        anchor.getChildren().add(table);
        anchor.getChildren().add(fab);
        root.getChildren().add(anchor);
        return root;
    }

    JFXDialog getDialog(Scene sc, StackPane root, Node header, Node content, List<Node> actions, double hBindRatio, double vBindRatio) {
        JFXDialogLayout layout = new JFXDialogLayout();

        layout.setHeading(header);
        layout.setBody(content);
        layout.setActions(actions);

        layout.prefWidthProperty().bind(sc.widthProperty().multiply(hBindRatio));
        layout.prefHeightProperty().bind(sc.heightProperty().multiply(vBindRatio));

        return new JFXDialog(root, layout, JFXDialog.DialogTransition.CENTER);
    }

    BorderPane getCourseCreateContent(Scene sc) {
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
        JFXButton addEvent = new JFXButton();
        AtomicInteger items = new AtomicInteger(0);

        HBox eventsHeader = new HBox(12);
        eventsHeader.getChildren().addAll(Arrays.asList(eventHeaderText, addEvent));
        eventsHeader.setAlignment(Pos.CENTER_LEFT);

        HBox[] rows = new HBox[7];
        String[] eventNames = new String[7];
        String[] marks = new String[7];
        Boolean[] recurrings = new Boolean[7];

        JFXTextField field1 = getTextField(content, "Assignment name", 0.3, false);
        JFXTextField marks1 = getTextField(content, "Weight", 0.04, true);
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
            if (items.get() < 6) {
                int i = items.get();

                JFXTextField nameField = getTextField(content, "Assignment name", 0.3, false);
                nameField.textProperty().addListener((observable, oldValue, newValue) -> eventNames[i] = newValue);
                JFXTextField markField = getTextField(content, "Weight", 0.04, true);
                markField.textProperty().addListener((observable, oldValue, newValue) -> marks[i] = newValue);
                JFXToggleButton buttonField = getToggleButton("Recurring?");
                buttonField.selectedProperty().addListener((observable, oldValue, newValue) -> recurrings[i] = newValue);

                rows[i] = new HBox();
                rows[i].getChildren().addAll(Arrays.asList(nameField, markField, buttonField));
                rows[i].setSpacing(16);
                rows[i].setAlignment(Pos.CENTER_LEFT);

                events.add(rows[i], 0, i + 2);
                events.getRowConstraints().add(new RowConstraints(52));
                items.addAndGet(1);
            }
        });

        events.add(eventsHeader, 0, 0);
        events.add(rows[0], 0, 1);
        events.getRowConstraints().add(new RowConstraints(52));

        // step 3
        AnchorPane step3 = new AnchorPane();
        GridPane grid3 = new GridPane();

        HBox step3Header = new HBox(12);
        step3Header.getChildren().add(getTextNormal("Set due dates", white));
        step3Header.setAlignment(Pos.CENTER_LEFT);
        step3Header.prefHeightProperty().bind(eventsHeader.heightProperty());
        step3Header.prefWidthProperty().bind(step3.widthProperty().multiply(0.64));

        JFXDatePicker[] dates = new JFXDatePicker[7];
        JFXTimePicker[] times = new JFXTimePicker[7];
        JFXDatePicker[] skipDates = new JFXDatePicker[7];
        JFXTextField[] occurrences = new JFXTextField[7];
        JFXTextField[] offsets = new JFXTextField[7];

        JFXButton save = new JFXButton("SAVE");
        save.setId("save");
        save.setOnAction(event -> {
            boolean saved = logic.verifyAndAddCourse(courseName.get(), eventNames, marks, recurrings, dates, times, skipDates, occurrences, offsets, grid3.getRowConstraints().size());
        });

        step3.getChildren().add(grid3);
        step3.getChildren().add(save);

        setAnchorsCreate(grid3, save);

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
            content.getChildren().add(step3);

            grid3.getChildren().clear();
            grid3.getRowConstraints().clear();
            grid3.add(step3Header, 0, 0);
            grid3.getRowConstraints().add(new RowConstraints(52));

            for (int i = 0; i <= items.get(); i++) {
                HBox box = new HBox(16);
                box.setAlignment(Pos.CENTER_LEFT);
                box.prefWidthProperty().bind(step3.widthProperty().multiply(0.1));

                boolean isRecurring = recurrings[i] != null;
                dates[i] = getDatePicker(step3, isRecurring ? "First due date" : "Due date", 0.1);
                times[i] = getTimePicker(step3, "Due time", 0.1);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                box.getChildren().addAll(Arrays.asList(getTextNormal(eventNames[i] != null ? eventNames[i] : "Blank name", white),
                        spacer, dates[i], times[i]));
                if (isRecurring) {
                    skipDates[i] = getDatePicker(step3, "Skip date", 0.1);
                    occurrences[i] = getTextField(step3, "No. of times", 0.05, true);
                    offsets[i] = getTextField(step3, "Offset", 0.05, true);
                    box.getChildren().add(skipDates[i]);
                    box.getChildren().add(occurrences[i]);
                    box.getChildren().add(offsets[i]);
                }

                grid3.add(box, 0, i + 1);
                grid3.getRowConstraints().add(new RowConstraints(52));
            }
        });

        addEvent.setGraphic(new FontIcon());
        addEvent.setId("add-event");

        eventHeaderText.setFill(Paint.valueOf("#FFF"));
        eventsHeader.setAlignment(Pos.CENTER_LEFT);

        pane.setLeft(nav);
        pane.setCenter(content);
        return pane;
    }

//    StackPane getCourseCreate() {
//        StackPane root = new StackPane();
//        StackPane layoutRoot = new StackPane();
//        GridPane layout = new GridPane();
//        GridPane assessments = new GridPane();
//        AnchorPane anchor = new AnchorPane();
//
////        JFXTextField name = getTextField(layoutRoot, "Course name", 0.5, false);
//        Text breakdownHeaderText = new Text("Marks breakdown");
//        JFXButton add = new JFXButton();
//        JFXButton save = new JFXButton();
//
//        HBox breakdownHeader = new HBox(12);
//        breakdownHeader.getChildren().addAll(Arrays.asList(breakdownHeaderText, add));
//
//        final List<JFXTextField>[] names = new List[]{new ArrayList<>(Collections.singletonList(getTextField(layoutRoot, "Assignment name", 0.3, false)))};
//        final List<JFXTextField>[] marks = new List[]{new ArrayList<>(Collections.singletonList(getTextField(layoutRoot, "Weight", 0.075, true)))};
//        final List<JFXDatePicker>[] dates = new List[]{new ArrayList<>(Collections.singletonList(getDatePicker(layoutRoot, "Due date", 0.12)))};
//        final List<JFXTimePicker>[] times = new List[]{new ArrayList<>(Collections.singletonList(getTimePicker(layoutRoot, "Due time", 0.12)))};
////        List<JFXToggleButton> recurs = new ArrayList<>();
//
//        AtomicInteger items = new AtomicInteger(1);
//
//        add.setOnAction(e -> {
//            if (items.get() < 7) {
//                items.addAndGet(1);
//
////                names[0].add(getTextField(layoutRoot, "Assignment name", 0.3, false));
////                marks[0].add(getTextField(layoutRoot, "Weight", 0.075, true));
//                dates[0].add(getDatePicker(layoutRoot, "Due date", 0.12));
//                times[0].add(getTimePicker(layoutRoot, "Due time", 0.12));
//
//                assessments.add(names[0].get(items.get() - 1), 0, items.get());
//                assessments.add(marks[0].get(items.get() - 1), 1, items.get());
//                assessments.add(dates[0].get(items.get() - 1), 2, items.get());
//                assessments.add(times[0].get(items.get() - 1), 3, items.get());
//            }
//        });
//
//        assessments.add(breakdownHeader, 0, 0);
//        assessments.add(names[0].get(0), 0, 1);
//        assessments.add(marks[0].get(0), 1, 1);
//        assessments.add(dates[0].get(0), 2, 1);
//        assessments.add(times[0].get(0), 3, 1);
//
//        layout.add(name, 0, 0);
//        layout.add(assessments, 0, 1);
//
//        JFXNodesList fab = new JFXNodesList();
//        fab.addAnimatedNode(save);
//
//        anchor.getChildren().add(layout);
//        anchor.getChildren().add(fab);
//
//        save.setOnAction(event -> {
//            boolean saved = logic.verifyAndAddCourse(name.getText(), names[0], marks[0], dates[0], times[0]);
//            JFXSnackbar popup = new JFXSnackbar(layout);
////            popup.set
//            if (saved) {
//                names[0].forEach(TextInputControl::clear);
//                marks[0].forEach(TextInputControl::clear);
//                assessments.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= 2);
//                names[0] = names[0].subList(0, 1);
//                marks[0] = marks[0].subList(0, 1);
//                dates[0] = dates[0].subList(0, 1);
//                times[0] = times[0].subList(0, 1);
//                items.set(1);
//            }
//            HBox textBox = new HBox();
//            Text popupText = new Text(saved ? "Course added" : "Invalid input(s), try again");
//
//            textBox.getChildren().add(popupText);
//            textBox.setAlignment(Pos.CENTER_LEFT);
//            textBox.prefWidthProperty().bind(layoutRoot.widthProperty().multiply(0.5));
//            textBox.prefHeightProperty().bind(layoutRoot.heightProperty().multiply(0.05));
//            textBox.setPadding(mediumMargin);
//            textBox.setBackground(whiteLightBackground);
//
//            popup.enqueue(new JFXSnackbar.SnackbarEvent(textBox, Duration.seconds(3.33), null));
//        });
//
//        // styling
//        {root.setPadding(largerMargin);
//        root.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
//
//        layout.setBackground(testBackground);
//        layout.setPadding(mediumMargin);
//        layout.setVgap(16);
//        JFXDepthManager.setDepth(layout, 1);
//
//        assessments.setVgap(16);
//        assessments.setHgap(16);
//
//        add.setGraphic(new FontIcon());
//        add.setId("add-button");
//
//        breakdownHeaderText.setFill(Paint.valueOf("#FFF"));
//        breakdownHeader.setAlignment(Pos.CENTER_LEFT);
//
//        save.setGraphic(new FontIcon());
//        save.setId("save");
//        save.getStyleClass().add("animated-option-button");
//
//        setAnchorsCreate(layout, fab);
//
//        layoutRoot.getChildren().add(anchor);
//        root.getChildren().addAll(layoutRoot);}
//
//        return root;
//    }

//    StackPane getEventCreate() {
//        StackPane root = new StackPane();
//        StackPane layoutRoot = new StackPane();
//        AnchorPane anchor = new AnchorPane();
//        GridPane layout = new GridPane();
//
//        ObservableList<ObservableCourse> courses = logic.getCourses(true);
//        courses.add(new ObservableCourse("Test course 1", "test1",89.0, "Lecture", LocalDateTime.now()));
//        courses.add(new ObservableCourse("Test course 2", "test2",80.0, "Assignment 1", LocalDateTime.of(2019, 10, 12, 10, 30)));
//        ObservableList<String> courseNames = FXCollections.observableArrayList();
//        for (ObservableCourse course: courses) {
//            courseNames.add(course.nameProperty().getValue());
//        }
//
//        JFXTextField name = getTextField(layoutRoot, "Event name", 0.5, false);
//        JFXDatePicker date = getDatePicker(layoutRoot, "Due date", 0.5);
//        JFXTimePicker time = getTimePicker(layoutRoot, "Due time", 0.5);
//        JFXTextField notes = getTextField(layoutRoot, "Notes", 0.5, false);
//        JFXComboBox<String> course = getComboBox(layoutRoot, "Associated course", 0.5);
//        JFXToggleButton recurring = getToggleButton("no");
//        course.setItems(courseNames);
//
//        HBox recurringBox = new HBox();
//        recurringBox.getChildren().add(getTextNormal("Recurring?", gray));
//        recurringBox.getChildren().add(recurring);
//
//        layout.add(name, 0, 0);
//        layout.add(date, 0, 1);
//        layout.add(time, 0, 2);
//        layout.add(notes, 0, 3);
//        layout.add(course, 0, 4);
//        layout.add(recurringBox, 0, 5);
//
//        JFXButton save = new JFXButton();
//        JFXNodesList fab = new JFXNodesList();
//        fab.addAnimatedNode(save);
//
//        root.setPadding(largerMargin);
//        root.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
//
//        layout.setBackground(testBackground);
//        layout.setPadding(mediumMargin);
//        layout.setVgap(24);
//        JFXDepthManager.setDepth(layout, 1);
//
//        save.setGraphic(new FontIcon());
//        save.setId("save");
//        save.getStyleClass().add("animated-option-button");
//
//        recurringBox.setAlignment(Pos.CENTER_LEFT);
//
//        setAnchorsCreate(layout, fab);
//
//        anchor.getChildren().add(layout);
//        anchor.getChildren().add(fab);
//        layoutRoot.getChildren().add(anchor);
//        root.getChildren().addAll(layoutRoot);
//
//        return root;
//    }

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

    JFXDatePicker getDatePicker(Pane root, String promptText, double bindRatio) {
        JFXDatePicker date = new JFXDatePicker();
        date.setDefaultColor(focus);
//        date.setOverLay(true);
//        date.setDialogParent(root);
        date.setId("date-picker");
        date.setPromptText(promptText);
        date.prefWidthProperty().bind(root.widthProperty().multiply(bindRatio));

        return date;
    }

    JFXTimePicker getTimePicker(Pane root, String promptText, double bindRatio) {
        JFXTimePicker time = new JFXTimePicker();
        time.setDefaultColor(focus);
        time.set24HourView(true);
//        time.setOverLay(true);
//        time.setDialogParent(root);
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



    void setAnchorsCreate(Node layout, Node fab) {
        AnchorPane.setRightAnchor(layout, 0.0);
        AnchorPane.setLeftAnchor(layout, 0.0);
        AnchorPane.setTopAnchor(layout, 0.0);
        AnchorPane.setBottomAnchor(layout, 0.0);

        AnchorPane.setRightAnchor(fab, 35.0);
        AnchorPane.setBottomAnchor(fab, 35.0);
    }
}
