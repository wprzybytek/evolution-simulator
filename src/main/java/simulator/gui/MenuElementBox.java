package simulator.gui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MenuElementBox {

    private Label label;
    private TextField field;
    private VBox vbox;

    public MenuElementBox(String desc, String def) {
        this.label = new Label(desc);
        this.field = new TextField(def);
        this.vbox = new VBox(label, field);
    }

    public VBox getVbox() {
        return this.vbox;
    }

    public String text() {
        return this.field.getText();
    }
}
