package simulator.gui;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StartMenu {
    final MenuElementBox width;
    final MenuElementBox height;
    final MenuElementBox startEnergy;
    final MenuElementBox moveEnergy;
    final MenuElementBox plantEnergy;
    final MenuElementBox jungleRatio;
    final MenuElementBox numberOfAnimals;
    final MenuElementBox eraTime;
    final RadioButton flatMagic;
    final RadioButton flatNormal;
    final RadioButton roundMagic;
    final RadioButton roundNormal;

    private final Button startSimulation;
    private final Scene menuScene;

    //constructor
    public StartMenu() {
        this.height = new MenuElementBox("Height", "10");
        this.width = new MenuElementBox("Width", "10");
        this.startEnergy = new MenuElementBox("Start Energy", "20");
        this.moveEnergy = new MenuElementBox("Move Energy", "1");
        this.plantEnergy = new MenuElementBox("Plant Energy", "10");
        this.jungleRatio = new MenuElementBox("Jungle Ratio", "0.2");
        this.numberOfAnimals = new MenuElementBox("Number of animals", "30");
        this.eraTime = new MenuElementBox("Time of an era [ms]: ", "1000");
        Label flatMap = new Label("Evolution type on flat map: ");
        ToggleGroup flatEvolution = new ToggleGroup();
        this.flatNormal = new RadioButton("Normal Evolution");
        this.flatMagic = new RadioButton("Magic Evolution");
        this.flatNormal.setToggleGroup(flatEvolution);
        this.flatMagic.setToggleGroup(flatEvolution);
        flatNormal.setSelected(true);
        Label roundMap = new Label("Evolution type on round map: ");
        ToggleGroup roundEvolution = new ToggleGroup();
        this.roundNormal = new RadioButton("Normal Evolution");
        this.roundMagic = new RadioButton("Magic Evolution");
        this.roundNormal.setToggleGroup(roundEvolution);
        this.roundMagic.setToggleGroup(roundEvolution);
        roundNormal.setSelected(true);
        VBox heightBox = this.height.getVbox();
        VBox widthBox = this.width.getVbox();
        VBox seBox = this.startEnergy.getVbox();
        VBox meBox = this.moveEnergy.getVbox();
        VBox peBox = this.plantEnergy.getVbox();
        VBox jrBox = this.jungleRatio.getVbox();
        VBox noaBox = this.numberOfAnimals.getVbox();
        VBox etBox = this.eraTime.getVbox();
        HBox flatMagicBox = new HBox(flatMap, flatNormal, flatMagic);
        HBox roundMagicBox = new HBox(roundMap, roundNormal, roundMagic);
        startSimulation = new Button("Start");
        GridPane grid = new GridPane();
        grid.add(heightBox, 0, 0);
        grid.add(widthBox, 0, 1);
        grid.add(seBox, 0, 2);
        grid.add(meBox, 0, 3);
        grid.add(peBox, 0, 4);
        grid.add(jrBox, 0, 5);
        grid.add(noaBox, 0, 6);
        grid.add(etBox, 0, 7);
        grid.add(flatMagicBox, 0, 8);
        grid.add(roundMagicBox, 0, 9);
        grid.add(startSimulation, 0, 10);
        GridPane.setHalignment(startSimulation, HPos.CENTER);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        menuScene = new Scene(grid, 600, 600);
    }

    //getters
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
        return Float.parseFloat(this.jungleRatio.text() + "f");
    }

    public int getNumberOfAnimals() {
        return Integer.parseInt(this.numberOfAnimals.text());
    }

    public int getEraTime() {
        return Integer.parseInt(this.eraTime.text());
    }

    public boolean getFlatMagic() {
        return flatMagic.isSelected();
    }

    public boolean getRoundMagic() {
        return roundMagic.isSelected();
    }
}
