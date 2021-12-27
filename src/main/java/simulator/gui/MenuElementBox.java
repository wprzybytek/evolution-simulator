package simulator.gui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MenuElementBox {

    private final TextField field;
    private final VBox vbox;

    //constructor
    public MenuElementBox(String desc, String def) {
        Label label = new Label(desc);
        this.field = new TextField(def);
        this.vbox = new VBox(label, field);
    }

    //getters
    public VBox getVbox() {
        return this.vbox;
    }

    public String text() {
        return this.field.getText();
    }
}
