package ui;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class View extends Application {

    int viewWidth = 1280;
    int viewHeight = 720;

    Presenter pr;
    BooleanBinding widthTracker;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        Scene scene = new Scene(root, viewWidth, viewHeight);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        pr = new Presenter(scene);

        NumberBinding currentWidth = primaryStage.widthProperty().add(0);
        IntegerProperty width = new SimpleIntegerProperty();
        width.bind(currentWidth);

        widthTracker = width.greaterThan(850);

        BorderPane contentRoot = pr.getInitialLayout();
        root.getChildren().add(contentRoot);

        // only change when it's larger than 800
        widthTracker.addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // shift to smaller layout
                pr.setSmallLayout(contentRoot, (BorderPane) contentRoot.getCenter());
            } else {
                // shift to larger layout
                pr.setLargeLayout(contentRoot, (BorderPane) contentRoot.getCenter());
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        System.out.println("Saving data");
        pr.exportData();
    }

}
