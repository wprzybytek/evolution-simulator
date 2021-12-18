package simulator;

import com.sun.javafx.collections.MappingChange;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

public class App extends Application implements ITurnEndObserver {

    private GridPane grid;
    private SimulationEngine engine;

    public void init() {
        grid = new GridPane();
        engine = new SimulationEngine(10, 10, 10, 1,
                5, 0.2f, 10, true, false);
        engine.addObserver(this);
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
        this.grid.getColumnConstraints().add(new ColumnConstraints(20));
        this.grid.getRowConstraints().add(new RowConstraints(20));

        for (int i = 0; i < width; i++) {
            Label label = new Label(Integer.toString(i));
            this.grid.add(label, i + 1, 0);
            this.grid.getColumnConstraints().add(new ColumnConstraints(20));
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for (int i = 0; i < height; i++) {
            Label label = new Label(Integer.toString(height - 1 - i));
            this.grid.add(label, 0, i + 1);
            this.grid.getRowConstraints().add(new RowConstraints(20));
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        prepareGrid();
        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        Thread engineThread = new Thread(engine);
        engineThread.start();
    }

    @Override
    public void turnEnded() {
        Platform.runLater(() -> {
            grid.getChildren().clear();
            prepareGrid();
        });
    }
}
