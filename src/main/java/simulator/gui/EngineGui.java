package simulator.gui;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import simulator.animal.Animal;
import simulator.map_elements.Grass;
import simulator.map_elements.Vector2D;
import simulator.simulation.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

public class EngineGui implements ITurnEndObserver, IMagicObserver {
    private final GridPane grid = new GridPane();
    private final Text dayCounter = new Text("Day 0");
    private final AbstractSimulationEngine engine;
    private final Chart animalChart = new Chart("Animals");
    private final Chart grassChart = new Chart("Grass");
    private final Text dominantGenotype = new Text("");
    private final Chart avgEnergy = new Chart("Average Energy");
    private final Chart avgLifetime = new Chart("Average Lifetime");
    private final Chart avgChildren = new Chart("Average children");
    private final GridPane magicAlert;
    private int magicCounter = 0;
    private final Button stopButton = new Button("Stop");
    private final HBox layout;
    private final Thread engineThread;

    //constructor
    public EngineGui(StartMenu startMenu, boolean isFlat) {
        if((!startMenu.getFlatMagic() && isFlat) || (!startMenu.getRoundMagic() && !isFlat)) {
            engine = new NormalSimulationEngine(startMenu.getWidth(), startMenu.getHeight(), startMenu.getStartEnergy(),
                    startMenu.getMoveEnergy(), startMenu.getPlantEnergy(), startMenu.getJungleRatio(),
                    startMenu.getNumberOfAnimals(), startMenu.getEraTime(), isFlat);
        }
        else {
            engine = new MagicSimulationEngine(startMenu.getWidth(), startMenu.getHeight(), startMenu.getStartEnergy(),
                    startMenu.getMoveEnergy(), startMenu.getPlantEnergy(), startMenu.getJungleRatio(),
                    startMenu.getNumberOfAnimals(), startMenu.getEraTime(), isFlat);
            engine.addMagicObserver(this);
        }
        engine.addObserver(this);
        prepareGrid();
        updateValues();
        VBox dominantGenotypeBox = new VBox(new Label("Dominant genotype: "), dominantGenotype);
        dominantGenotypeBox.setAlignment(Pos.CENTER);
        VBox values = new VBox(dayCounter, animalChart.getChart(), grassChart.getChart(), dominantGenotypeBox,
                avgEnergy.getChart(), avgLifetime.getChart(), avgChildren.getChart());
        values.setAlignment(Pos.CENTER);
        dayCounter.setFont(Font.font(30));
        Label typeOfMap;
        if(isFlat) typeOfMap = new Label("Flat Map");
        else typeOfMap = new Label("Round Map");
        typeOfMap.setFont(Font.font(20));
        magicAlert = new GridPane();
        stopButton.setOnAction(e -> {stopSimulation();});
        VBox gridMap = new VBox(typeOfMap, grid, stopButton, magicAlert);
        gridMap.setSpacing(10);
        magicAlert.setAlignment(Pos.CENTER);
        layout = new HBox(values, gridMap);
        gridMap.setAlignment(Pos.CENTER);
        engineThread = new Thread(engine);
    }

    //getters
    public HBox getLayout() {
        return layout;
    }

    public Thread getEngineThread() {
        return engineThread;
    }

    //methods
    protected void prepareGrid() {
        int width = engine.getMap().getWidth();
        int height = engine.getMap().getHeight();
        if(width<=30 && height<=30){
            this.grid.setGridLinesVisible(false);
            this.grid.setGridLinesVisible(true);
        }
        this.grid.getRowConstraints().clear();
        this.grid.getColumnConstraints().clear();

        Label xy = new Label("y/x");
        xy.setFont(Font.font(100/width));
        this.grid.add(xy, 0, 0);
        GridPane.setHalignment(xy, HPos.CENTER);
        this.grid.getColumnConstraints().add(new ColumnConstraints(300/width));
        this.grid.getRowConstraints().add(new RowConstraints(300/height));

        for (int i = 0; i < width; i++) {
            Label label = new Label(Integer.toString(i));
            label.setFont(Font.font(100/width));
            this.grid.add(label, i + 1, 0);
            this.grid.getColumnConstraints().add(new ColumnConstraints(300/width));
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for (int i = 0; i < height; i++) {
            Label label = new Label(Integer.toString(height - 1 - i));
            label.setFont(Font.font(100/height));
            this.grid.add(label, 0, i + 1);
            this.grid.getRowConstraints().add(new RowConstraints(300/height));
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for(int x = engine.getMap().jungleStart.x; x <= engine.getMap().jungleEnd.x; x++) {
            for(int y = engine.getMap().jungleStart.y; y <= engine.getMap().jungleEnd.y; y++) {
                Label label = new Label("");
                label.setStyle("-fx-background-color: #426542");
                label.setPrefSize(300/width, 300/height);
                this.grid.add(label, x + 1, height - y);
            }
        }

        Map<Vector2D, ArrayList<Animal>> animals = engine.getMap().animals;
        Map<Vector2D, Grass> grass = engine.getMap().grassMap;

        for (Map.Entry<Vector2D, ArrayList<Animal>> entry : animals.entrySet()) {
            Animal animal = entry.getValue().get(0);
            VBox verticalBox = null;
            try {
                GuiElementBox guiElementBox = new GuiElementBox(animal);
                guiElementBox.setSize(width, height);
                verticalBox = guiElementBox.getVerticalBox();
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
            verticalBox.setStyle("-fx-background-color: #f8c9c9");
            verticalBox.setPrefSize(300/width, 300/height);
            this.grid.add(verticalBox, animal.getPosition().x + 1, height - animal.getPosition().y);
            GridPane.setHalignment(verticalBox, HPos.CENTER);
        }

        for(Map.Entry<Vector2D, Grass> entry : grass.entrySet()) {
            if(!(animals.containsKey(entry.getKey()))) {
                VBox verticalBox = null;
                try {
                    GuiElementBox guiElementBox = new GuiElementBox(entry.getValue());
                    guiElementBox.setSize(width, height);
                    verticalBox = guiElementBox.getVerticalBox();
                } catch (FileNotFoundException ex) {
                    System.out.println(ex);
                }
                verticalBox.setStyle("-fx-background-color: #e1f8c9");
                verticalBox.setPrefSize(300/width, 300/height);
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
        dominantGenotype.setText(engine.getDominantGenotype());
        avgEnergy.updateChart(day, engine.getAvgEnergy());
        avgLifetime.updateChart(day, engine.getAvgLifeTime());
        avgChildren.updateChart(day, engine.getAvgChildren());
    }

    public void stopSimulation() {
        stopButton.setText("Start");
        engine.pause();
        stopButton.setOnAction(event -> {startSimulation();});
    }

    public void startSimulation() {
        stopButton.setText("Stop");
        engine.resume();
        stopButton.setOnAction(event -> {stopSimulation();});
    }

    @Override
    public void turnEnded() {
        Platform.runLater(() -> {
            grid.getChildren().clear();
            prepareGrid();
            updateValues();
        });
    }

    @Override
    public void magicHappened() {
        Platform.runLater(() -> {
            Text alert = new Text("Magic Happened!");
            alert.setFont(Font.font(20));
            this.magicAlert.addRow(magicCounter, alert);
            GridPane.setHalignment(alert, HPos.CENTER);
            magicCounter++;
        });
    }
}
