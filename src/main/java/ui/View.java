package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class View extends Application {

    int viewWidth = 1408;
    int viewHeight = 792;

    Presenter pr;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        Scene scene = new Scene(root, viewWidth, viewHeight);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        pr = new Presenter(scene);
        root.getChildren().add(pr.getLargeLayout());

        // only change when it's larger than 600
        primaryStage.widthProperty().greaterThan(800).addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // shift to smaller layout
                root.getChildren().clear();
                root.getChildren().add(pr.getLargeLayout());
            } else {
                // shift to larger layout
                root.getChildren().clear();
                root.getChildren().add(pr.getLargeLayout());
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
