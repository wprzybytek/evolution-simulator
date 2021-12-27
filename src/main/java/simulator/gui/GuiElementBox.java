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
    private final VBox verticalBox;
    private final ImageView imageView;

    //constructor
    public GuiElementBox(Object o) throws FileNotFoundException {
        if(o.getClass() == Animal.class) {
            switch (((Animal) o).getDirection()){
                case NORTH -> this.image = new Image(new FileInputStream("src/main/resources/north.png"));
                case NORTH_EAST -> this.image = new Image(new FileInputStream("src/main/resources/north-east.png"));
                case EAST -> this.image = new Image(new FileInputStream("src/main/resources/east.png"));
                case SOUTH_EAST -> this.image = new Image(new FileInputStream("src/main/resources/south-east.png"));
                case SOUTH -> this.image = new Image(new FileInputStream("src/main/resources/south.png"));
                case SOUTH_WEST -> this.image = new Image(new FileInputStream("src/main/resources/south-west.png"));
                case WEST -> this.image = new Image(new FileInputStream("src/main/resources/west.png"));
                case NORTH_WEST -> this.image = new Image(new FileInputStream("src/main/resources/north-west.png"));
                default -> throw new FileNotFoundException("No image for direction");
            }
            this.label = new Label(Integer.toString(((Animal) o).getEnergy()));
        }
        if(o.getClass() == Grass.class) {
            this.image = new Image(new FileInputStream("src/main/resources/grass.png"));
            this.label = new Label("");
        }
        this.imageView = new ImageView(image);
        this.verticalBox = new VBox(this.imageView, this.label);
        this.verticalBox.setAlignment(Pos.CENTER);
    }

    //getters
    public VBox getVerticalBox() {
        return verticalBox;
    }

    //setters
    public void setSize(int width, int height) {
        this.imageView.setFitWidth(150/width);
        this.imageView.setFitHeight(150/height);
        this.label.setFont(Font.font(100/width));
    }
}
