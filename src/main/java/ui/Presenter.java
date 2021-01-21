package ui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Controller;

public class Presenter {

    Controller con = new Controller();
    Scene sc;

    PresenterElements elements;
    PresenterLogic logic;

    public Presenter(Scene scene) {
        con.importData("courses.ser", "events.ser");
        sc = scene;
        logic = new PresenterLogic(con);
        elements = new PresenterElements(sc, logic);
    }

    void setLargeLayout(BorderPane root, BorderPane content) {
        HBox oldHeader = (HBox) content.getTop();
        Text header = (Text) oldHeader.getChildren().get(0);

        content.setTop(elements.getLargeLayoutHeader(header.getText()));
        content.setCenter(content.getCenter());

        VBox nav = elements.getNav(content);
        root.setLeft(nav);
        root.layout();
    }

    void setSmallLayout(BorderPane root, BorderPane content) {
        HBox oldHeader = (HBox) content.getTop();
        Text header = (Text) oldHeader.getChildren().get(0);

        content.setTop(elements.getSmallLayoutHeader(header.getText(), content));
        content.setCenter(content.getCenter());

        root.setLeft(null);
        root.layout();
    }

    BorderPane getInitialLayout() {
        BorderPane root = new BorderPane();

        BorderPane content = new BorderPane();
        VBox nav = elements.getNav(content);

        content.setTop(elements.getLargeLayoutHeader("Calendar"));
        content.setCenter(elements.getCalendarPane());

        root.setLeft(nav);
        root.setCenter(content);

        return root;
    }

    public void exportData() {
        con.exportData("courses.ser", "events.ser");
    }
}
