package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.effects.JFXDepthManager;
import javafx.beans.property.SimpleDoubleProperty;
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
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;

public class PresenterElements {

    Background testBackground = new Background(new BackgroundFill(Color.rgb(27, 27, 27), CornerRadii.EMPTY, Insets.EMPTY));
    Background accentBackground =  new Background(new BackgroundFill(Color.rgb(255, 152, 0), CornerRadii.EMPTY, Insets.EMPTY));

    Insets margin = new Insets(8);
    Insets mediumMargin = new Insets(16);
    Insets largerMargin = new Insets(24);

    Paint white = Paint.valueOf("#FFFFFF");

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
            content.setCenter(getTabPane());
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
        tabs.setTabMinWidth(80);
        tabs.setTabMaxWidth(80);

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
        tabs.setTabMinWidth(80);
        tabs.setTabMaxWidth(80);

        addTab(tabs, "Overview", getCourseOverviewContent(sc, courses));
        addTab(tabs, "CSC343", new Label("Blank Courses"));
        addTab(tabs, "STA305", new Label("Blank Events"));
        addTab(tabs, "STA355", new Label("Blank Add Menu"));
        addTab(tabs, "PCL201", new Label("Blank Settings"));

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
}
