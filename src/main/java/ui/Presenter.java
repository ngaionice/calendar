package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.effects.JFXDepthManager;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.Controller;

public class Presenter {

    Controller con = new Controller();
    Scene sc;

    PresenterElements elements = new PresenterElements();
    PresenterLogic logic;

    public Presenter(Scene scene) {
        con.importData("courses.ser", "events.ser");
        sc = scene;
        logic = new PresenterLogic(con);
    }

    BorderPane getLargeLayout() {
        BorderPane root = new BorderPane();

        BorderPane content = new BorderPane();
        VBox nav = elements.getNav(sc, content, logic);

        content.setTop(elements.getHeader(sc, "Courses"));
        content.setCenter(elements.getTabPane());

        root.setLeft(nav);
        root.setCenter(content);

        return root;
    }



}
