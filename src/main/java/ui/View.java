package ui;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
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
        root.getChildren().add(pr.getLargeLayout());

        NumberBinding currentWidth = primaryStage.widthProperty().add(0);
        IntegerProperty width = new SimpleIntegerProperty();
        width.bind(currentWidth);

        widthTracker = width.greaterThan(750);

        // only change when it's larger than 750
//        widthTracker.addListener((observable, oldValue, newValue) -> {
//            if (!newValue) {
//                // shift to smaller layout
//                root.getChildren().clear();
//                root.getChildren().add(pr.getSmallLayout());
//            } else {
//                // shift to larger layout
//                root.getChildren().clear();
//                root.getChildren().add(pr.getLargeLayout());
//            }
//        });
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
