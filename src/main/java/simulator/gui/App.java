package simulator.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.EventHandler;

public class App extends Application {

    private EngineGui flatMap;
    private EngineGui roundMap;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Evolution Simulator");
        StartMenu startMenu = new StartMenu();
        primaryStage.setScene(startMenu.getMenuScene());
        primaryStage.show();

        startMenu.getButton().setOnAction(click -> {
            this.flatMap = new EngineGui(startMenu, true);
            this.roundMap = new EngineGui(startMenu, false);

            HBox mainLayout = new HBox(flatMap.getLayout(), roundMap.getLayout());

            Scene scene = new Scene(mainLayout, 600, 600);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

            flatMap.getEngineThread().start();
            roundMap.getEngineThread().start();
        });
    }
}
