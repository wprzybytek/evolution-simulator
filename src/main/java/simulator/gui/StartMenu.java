package simulator.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StartMenu {
    MenuElementBox width;
    MenuElementBox height;
    MenuElementBox startEnergy;
    MenuElementBox moveEnergy;
    MenuElementBox plantEnergy;
    MenuElementBox jungleRatio;
    MenuElementBox numberOfAnimals;
    RadioButton flat;
    RadioButton round;
    RadioButton magic;
    RadioButton normal;

    private Button startSimulation;
    private Scene menuScene;

    public StartMenu() {
        this.height = new MenuElementBox("Height", "10");
        this.width = new MenuElementBox("Width", "10");
        this.startEnergy = new MenuElementBox("Start Energy", "20");
        this.moveEnergy = new MenuElementBox("Move Energy", "1");
        this.plantEnergy = new MenuElementBox("Plant Energy", "10");
        this.jungleRatio = new MenuElementBox("Jungle Ratio", "0.2f");
        this.numberOfAnimals = new MenuElementBox("Number of animals", "30");
        ToggleGroup isFlat = new ToggleGroup();
        this.flat = new RadioButton("Flat Map");
        this.round = new RadioButton("Round Map");
        round.setToggleGroup(isFlat);
        flat.setToggleGroup(isFlat);
        flat.setSelected(true);
        ToggleGroup evolution = new ToggleGroup();
        this.normal = new RadioButton("Normal Evolution");
        this.magic = new RadioButton("Magic Evolution");
        this.normal.setToggleGroup(evolution);
        this.magic.setToggleGroup(evolution);
        normal.setSelected(true);
        VBox heightBox = this.height.getVbox();
        VBox widthBox = this.width.getVbox();
        VBox seBox = this.startEnergy.getVbox();
        VBox meBox = this.moveEnergy.getVbox();
        VBox peBox = this.plantEnergy.getVbox();
        VBox jrBox = this.jungleRatio.getVbox();
        VBox noaBox = this.numberOfAnimals.getVbox();
        HBox ifBox = new HBox(flat, round);
        HBox magicBox = new HBox(normal, magic);
        startSimulation = new Button("Start");
        GridPane grid = new GridPane();
        grid.add(heightBox, 0, 0);
        grid.add(widthBox, 0, 1);
        grid.add(seBox, 0, 2);
        grid.add(meBox, 0, 3);
        grid.add(peBox, 0, 4);
        grid.add(jrBox, 0, 5);
        grid.add(noaBox, 0, 6);
        grid.add(ifBox, 0, 7);
        grid.add(magicBox, 0, 8);
        grid.add(startSimulation, 0, 9);
        menuScene = new Scene(grid, 600, 600);
    }

    public Button getButton() {
        return this.startSimulation;
    }

    public Scene getMenuScene() {
        return this.menuScene;
    }

    public int getWidth() {
        return Integer.parseInt(this.width.text());
    }

    public int getHeight() {
        return Integer.parseInt(this.height.text());
    }

    public int getStartEnergy() {
        return Integer.parseInt(this.startEnergy.text());
    }

    public int getMoveEnergy() {
        return Integer.parseInt(this.moveEnergy.text());
    }

    public int getPlantEnergy() {
        return Integer.parseInt(this.plantEnergy.text());
    }

    public float getJungleRatio() {
        return Float.parseFloat(this.jungleRatio.text());
    }

    public int getNumberOfAnimals() {
        return Integer.parseInt(this.numberOfAnimals.text());
    }

    public boolean getIsFlat() {
        return flat.isSelected();
    }

    public boolean getMagicEvolution() {
        return magic.isSelected();
    }
}
