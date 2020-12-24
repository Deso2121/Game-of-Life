package agh.cs.gameOfLife;

import agh.cs.gameOfLife.data.DataStorage;
import agh.cs.gameOfLife.data.JSONParser;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class GameOfLifeApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Integer[] settings = JSONParser.readConfig("src/main/java/agh/cs/gameOfLife/parameters.json");
        GameOfLife gameOfLife = new GameOfLife(primaryStage, settings);
    }

    public static void main(String[] args) {
        launch(args);
    }
}