package simulator.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import simulator.simulation.ITurnEndObserver;
import simulator.simulation.SimulationEngine;
import simulator.animal.Animal;
import simulator.map_elements.Grass;
import simulator.map_elements.Vector2D;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

public class App extends Application implements ITurnEndObserver {

    private final GridPane grid = new GridPane();
    private final Text dayCounter = new Text("Day 0");
    private final Text animalCounter = new Text("Animals: 0");
    private final Text grassCounter = new Text("Grass: 0");
    private SimulationEngine engine;
    private Chart animalChart = new Chart("Animals");
    private Chart grassChart = new Chart("Grass");
    private Chart avgEnergy = new Chart("Average Energy");

    @Override
    public void start(Stage primaryStage) {
        StartMenu startMenu = new StartMenu();
        primaryStage.setScene(startMenu.getMenuScene());
        primaryStage.show();

        startMenu.getButton().setOnAction(click -> {
            engine = new SimulationEngine(startMenu.getWidth(), startMenu.getHeight(), startMenu.getStartEnergy(),
                    startMenu.getMoveEnergy(), startMenu.getPlantEnergy(), startMenu.getJungleRatio(),
                    startMenu.getNumberOfAnimals(), startMenu.getIsFlat(), startMenu.getMagicEvolution());
            engine.addObserver(this);
            prepareGrid();
            updateValues();
            VBox values = new VBox(dayCounter, animalChart.getChart(), grassChart.getChart(), avgEnergy.getChart());
            HBox layout = new HBox(values, grid);
            Scene scene = new Scene(layout, 600, 600);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
            Thread engineThread = new Thread(engine);
            engineThread.start();
        });
    }

    private void prepareGrid() {
        int width = engine.getMap().getWidth();
        int height = engine.getMap().getHeight();
        this.grid.setGridLinesVisible(false);
        this.grid.setGridLinesVisible(true);
        this.grid.getRowConstraints().clear();
        this.grid.getColumnConstraints().clear();

        Label xy = new Label("y/x");
        this.grid.add(xy, 0, 0);
        GridPane.setHalignment(xy, HPos.CENTER);
        this.grid.getColumnConstraints().add(new ColumnConstraints(30));
        this.grid.getRowConstraints().add(new RowConstraints(30));

        for (int i = 0; i < width; i++) {
            Label label = new Label(Integer.toString(i));
            this.grid.add(label, i + 1, 0);
            this.grid.getColumnConstraints().add(new ColumnConstraints(30));
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for (int i = 0; i < height; i++) {
            Label label = new Label(Integer.toString(height - 1 - i));
            this.grid.add(label, 0, i + 1);
            this.grid.getRowConstraints().add(new RowConstraints(30));
            GridPane.setHalignment(label, HPos.CENTER);
        }

        Map<Vector2D, ArrayList<Animal>> animals = engine.getMap().animals;
        Map<Vector2D, Grass> grass = engine.getMap().grassMap;

        for (Map.Entry<Vector2D, ArrayList<Animal>> entry : animals.entrySet()) {
            Animal animal = entry.getValue().get(0);
            VBox verticalBox = null;
            try {
                verticalBox = (new GuiElementBox(animal).getVerticalBox());
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
            this.grid.add(verticalBox, animal.getPosition().x + 1, height - animal.getPosition().y);
            GridPane.setHalignment(verticalBox, HPos.CENTER);
        }

        for(Map.Entry<Vector2D, Grass> entry : grass.entrySet()) {
            if(!(animals.containsKey(entry.getKey()))) {
                VBox verticalBox = null;
                try {
                    verticalBox = (new GuiElementBox(entry.getValue()).getVerticalBox());
                } catch (FileNotFoundException ex) {
                    System.out.println(ex);
                }
                this.grid.add(verticalBox, entry.getValue().getPosition().x + 1, height - entry.getValue().getPosition().y);
                GridPane.setHalignment(verticalBox, HPos.CENTER);
            }
        }
    }

    public void updateValues() {
        int day = engine.getDay();
        dayCounter.setText("Day " + day);
        animalChart.updateChart(day, engine.getAnimalsNumber());
        grassChart.updateChart(day, engine.getGrassNumber());
        avgEnergy.updateChart(day, engine.getAvgEnergy());
    }

    @Override
    public void turnEnded() {
        Platform.runLater(() -> {
            grid.getChildren().clear();
            prepareGrid();
            updateValues();
        });
    }
}
