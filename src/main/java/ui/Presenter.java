package ui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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

    BorderPane getLargeLayout() {
        BorderPane root = new BorderPane();

        BorderPane content = new BorderPane();
        VBox nav = elements.getNav(content);

        content.setTop(elements.getLargeLayoutHeader("Upcoming"));
        content.setCenter(elements.getCalendarPane());

        root.setLeft(nav);
        root.setCenter(content);

        return root;
    }

    BorderPane getSmallLayout() {
        BorderPane root = new BorderPane();

        BorderPane content = new BorderPane();
        VBox nav = elements.getNav(content);

        content.setTop(elements.getSmallLayoutHeader("Upcoming", root));
        content.setCenter(elements.getCalendarPane());

//        root.setLeft(nav);
        root.setLeft(null);
        root.setCenter(content);

        return root;
    }

    public void exportData() {
        con.exportData("courses.ser", "events.ser");
    }
}
