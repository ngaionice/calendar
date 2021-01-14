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
import model.Controller;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

public class PresenterElements {

    Background testBackground = new Background(new BackgroundFill(Color.rgb(27, 27, 27), CornerRadii.EMPTY, Insets.EMPTY));
    Background accentBackground =  new Background(new BackgroundFill(Color.rgb(255, 152, 0), CornerRadii.EMPTY, Insets.EMPTY));
    Background whiteLightBackground = new Background(new BackgroundFill(Color.rgb(238, 238, 238), new CornerRadii(3), Insets.EMPTY));

    Insets margin = new Insets(8);
    Insets mediumMargin = new Insets(16);
    Insets largerMargin = new Insets(24);

    Paint white = Paint.valueOf("#FFFFFF");
    Paint focus = Paint.valueOf("#FF9800");
    Paint focusLight = Paint.valueOf("#FFB74D");
    Paint whiteLight = Paint.valueOf("#EEEEEE");

    VBox getNav(Scene sc, BorderPane content, PresenterLogic logic) {
        VBox box = new VBox();

        List<JFXButton> mainButtons = Arrays.asList(new JFXButton("Calendar"), new JFXButton("Courses"),
                new JFXButton("Upcoming"), new JFXButton("Add"), new JFXButton("Settings"));
        List<String> buttonIDs = Arrays.asList("calendar", "courses", "upcoming", "add", "settings");

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
            content.setCenter(getAddPane(sc, logic));
            content.setTop(getHeader(sc, "Add"));
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
        text.setFont(Font.font(15));
        text.setFill(Paint.valueOf(color));
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

    JFXTabPane getAddPane(Scene sc, PresenterLogic logic) {
        JFXTabPane tabs = new JFXTabPane();
        tabs.setTabMinWidth(100);
        tabs.setTabMaxWidth(100);

        addTab(tabs, "Course", getCourseCreate(sc, logic));
        addTab(tabs, "Assignment", new Label("Blank"));

        return tabs;
    }

    StackPane getCourseOverviewContent(Scene sc, ObservableList<ObservableCourse> courseData) {
        StackPane root = new StackPane();
        root.setPadding(largerMargin);

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

        courseData.add(new ObservableCourse("Test course 1", 89.0, "Lecture", LocalDateTime.now()));
        courseData.add(new ObservableCourse("Test course 2", 80.0, "Assignment 1", LocalDateTime.of(2019, 10, 12, 10, 30)));

        table.getColumns().addAll(items);
        table.setItems(courseData);

        JFXDepthManager.setDepth(table, 1);

        root.getChildren().add(table);

        return root;
    }

    StackPane getCourseCreate(Scene sc, PresenterLogic logic) {
        StackPane root = new StackPane();
        root.setPadding(mediumMargin);
        root.prefHeightProperty().bind(sc.heightProperty().multiply(0.8));
        root.prefWidthProperty().bind(sc.widthProperty().multiply(0.8));

        StackPane layoutRoot = new StackPane();
        GridPane layout = new GridPane();
        layout.setBackground(testBackground);
        JFXDepthManager.setDepth(layout, 1);
        layout.setPadding(largerMargin);
        layout.setVgap(16);

        JFXTextField name = getTextField(layoutRoot, "Course name", 0.5, false);

        GridPane assessments = new GridPane();
        assessments.setVgap(16);
        assessments.setHgap(16);

        Text breakdownHeaderText = new Text("Marks breakdown");
        breakdownHeaderText.setFill(Paint.valueOf("#FFF"));

        JFXButton add = new JFXButton();
        add.setGraphic(new FontIcon());
        add.setId("add-button");

        HBox breakdownHeader = new HBox(12);
        breakdownHeader.getChildren().addAll(Arrays.asList(breakdownHeaderText, add));
        breakdownHeader.setAlignment(Pos.CENTER_LEFT);

        final List<JFXTextField>[] names = new List[]{new ArrayList<>()};
        final List<JFXTextField>[] marks = new List[]{new ArrayList<>()};
        final List<JFXDatePicker>[] dates = new List[]{new ArrayList<>()};
        final List<JFXTimePicker>[] times = new List[]{new ArrayList<>()};
//        List<JFXToggleButton> recurs = new ArrayList<>();

        names[0].add(getTextField(layoutRoot, "Assignment name", 0.3, false));
        marks[0].add(getTextField(layoutRoot, "Weight", 0.075, true));
        dates[0].add(getDatePicker(layoutRoot, "Due date", 0.12));
        times[0].add(getTimePicker(layoutRoot, "Due time", 0.12));

        AtomicInteger items = new AtomicInteger(1);

        add.setOnAction(e -> {
            if (items.get() < 7) {
                items.addAndGet(1);

                names[0].add(getTextField(layoutRoot, "Assignment name", 0.3, false));
                marks[0].add(getTextField(layoutRoot, "Weight", 0.075, true));
                dates[0].add(getDatePicker(layoutRoot, "Due date", 0.12));
                times[0].add(getTimePicker(layoutRoot, "Due time", 0.12));

                assessments.add(names[0].get(items.get() - 1), 0, items.get());
                assessments.add(marks[0].get(items.get() - 1), 1, items.get());
                assessments.add(dates[0].get(items.get() - 1), 2, items.get());
                assessments.add(times[0].get(items.get() - 1), 3, items.get());
            }
        });

        assessments.add(breakdownHeader, 0, 0);
        assessments.add(names[0].get(0), 0, 1);
        assessments.add(marks[0].get(0), 1, 1);
        assessments.add(dates[0].get(0), 2, 1);
        assessments.add(times[0].get(0), 3, 1);

        layout.add(name, 0, 0);
        layout.add(assessments, 0, 1);

        JFXButton save = new JFXButton();
        save.setGraphic(new FontIcon());
        save.setId("save");

        AnchorPane anchor = new AnchorPane();

        save.getStyleClass().add("animated-option-button");
        JFXNodesList fab = new JFXNodesList();
        fab.addAnimatedNode(save);

        anchor.getChildren().add(layout);
        anchor.getChildren().add(fab);

        save.setOnAction(event -> {
            boolean saved = logic.verifyAndAddCourse(name.getText(), names[0], marks[0], dates[0], times[0]);
            JFXSnackbar popup = new JFXSnackbar(layout);
            if (saved) {
                names[0].forEach(TextInputControl::clear);
                marks[0].forEach(TextInputControl::clear);
                assessments.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= 2);
                names[0] = names[0].subList(0, 1);
                marks[0] = marks[0].subList(0, 1);
                dates[0] = dates[0].subList(0, 1);
                times[0] = times[0].subList(0, 1);
                items.set(1);
            }
            HBox textBox = new HBox();
            Text popupText = new Text(saved ? "Course added" : "Invalid input(s), try again");

            textBox.getChildren().add(popupText);
            textBox.setAlignment(Pos.CENTER_LEFT);
            textBox.prefWidthProperty().bind(layoutRoot.widthProperty().multiply(0.5));
            textBox.prefHeightProperty().bind(layoutRoot.heightProperty().multiply(0.05));
            textBox.setPadding(mediumMargin);
            textBox.setBackground(whiteLightBackground);

            popup.enqueue(new JFXSnackbar.SnackbarEvent(textBox, Duration.seconds(3.33), null));
        });

        AnchorPane.setRightAnchor(layout, 0.0);
        AnchorPane.setLeftAnchor(layout, 0.0);
        AnchorPane.setTopAnchor(layout, 0.0);
        AnchorPane.setBottomAnchor(layout, 0.0);

        AnchorPane.setRightAnchor(fab, 35.0);
        AnchorPane.setBottomAnchor(fab, 35.0);

        layoutRoot.getChildren().add(anchor);
        root.getChildren().addAll(layoutRoot);

        return root;
    }

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

    UnaryOperator<TextFormatter.Change> getNumericFilter() {
        return input -> {
            if (input.isAdded()) {
                String addedText = input.getText();
                if (addedText.matches("[0-9]{1,2}(.\\d)?")) {
                    return input;
                }

                // TODO: fix this issue where things aren't getting replaced properly
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

    JFXToggleButton getToggleButton() {
        JFXToggleButton button = new JFXToggleButton();
        button.setToggleColor(focus);
        button.setUnToggleColor(white);
        button.setToggleLineColor(focusLight);
        button.setUnToggleLineColor(whiteLight);
        button.setSize(9);

        return button;
    }
}
