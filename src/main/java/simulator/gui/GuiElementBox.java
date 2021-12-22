package simulator.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import simulator.animal.Animal;
import simulator.map_elements.Grass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private Image image;
    private Label label;
    private VBox verticalBox;

    public GuiElementBox(Object o) throws FileNotFoundException {
        if(o.getClass() == Animal.class) {
            switch (((Animal) o).getDirection()){
                case NORTH -> {
                    this.image = new Image(new FileInputStream("src/main/resources/north.png"));
                }
                case NORTH_EAST -> {
                    this.image = new Image(new FileInputStream("src/main/resources/north-east.png"));
                }
                case EAST -> {
                    this.image = new Image(new FileInputStream("src/main/resources/east.png"));
                }
                case SOUTH_EAST -> {
                    this.image = new Image(new FileInputStream("src/main/resources/south-east.png"));
                }
                case SOUTH -> {
                    this.image = new Image(new FileInputStream("src/main/resources/south.png"));
                }
                case SOUTH_WEST -> {
                    this.image = new Image(new FileInputStream("src/main/resources/south-west.png"));
                }
                case WEST -> {
                    this.image = new Image(new FileInputStream("src/main/resources/west.png"));
                }
                case NORTH_WEST -> {
                    this.image = new Image(new FileInputStream("src/main/resources/north-west.png"));
                }
                default -> {throw new FileNotFoundException("No image for direction");}
            }
            this.label = new Label(Integer.toString(((Animal) o).getEnergy()));
        }
        if(o.getClass() == Grass.class) {
            this.image = new Image(new FileInputStream("src/main/resources/grass.png"));
            this.label = new Label("");
        }
        this.label.setFont(new Font(10));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        this.verticalBox = new VBox(imageView, this.label);
        this.verticalBox.setAlignment(Pos.CENTER);
    }

    public VBox getVerticalBox() {
        return verticalBox;
    }
}
