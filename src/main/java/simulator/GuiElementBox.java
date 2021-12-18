package simulator;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private Image image;
    private Label label;
    private VBox verticalBox;

    public GuiElementBox(Object o) throws FileNotFoundException {
        if(o.getClass() == Animal.class) {
            this.image = new Image(new FileInputStream("src/main/resources/south.png"));
            this.label = new Label(Integer.toString(((Animal) o).getEnergy()));
        }
        if(o.getClass() == Grass.class) {
            this.image = new Image(new FileInputStream("src/main/resources/grass.png"));
            this.label = new Label("");
        }
        this.label.setFont(new Font(5));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(10);
        imageView.setFitWidth(10);

        this.verticalBox = new VBox(imageView, this.label);
        this.verticalBox.setAlignment(Pos.CENTER);
    }

    public VBox getVerticalBox() {
        return verticalBox;
    }
}
