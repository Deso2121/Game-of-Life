package agh.cs.gameOfLife;

import agh.cs.gameOfLife.data.DataStorage;
import agh.cs.gameOfLife.engine.SimulationEngine;
import agh.cs.gameOfLife.constants.GameState;
import agh.cs.gameOfLife.ui.UserInterfaceImplementation;
import javafx.stage.Stage;

public class GameOfLife {
    private SimulationEngine engine;
    private GrassField map;
    private DataStorage dataStorage;
    private DataStorage dataStorage2;
    private GrassField map2;
    private SimulationEngine engine2;

    public SimulationEngine getEngine() {
        return engine;
    }

    public GrassField getMap() {
        return map;
    }

    public GameOfLife(Stage primaryStage, Integer[] settings) {
        if (settings[9] == 1) {
            this.dataStorage = new DataStorage(settings[0], settings[1], settings[3], settings[4], settings[7], settings[8], settings[10]);
            try {
                UserInterfaceImplementation uiImpl = new UserInterfaceImplementation(primaryStage, dataStorage);
                map = new GrassField(settings[2], settings[0], settings[1], settings[0] / settings[5], settings[1] / settings[5], dataStorage, uiImpl);
                dataStorage.setGameState(GameState.ACTIVE);
                engine = new SimulationEngine(map, uiImpl, dataStorage, settings[6]);
                uiImpl.initializeUserInterface();
                uiImpl.setEngine(engine);
                engine.startTimer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else if (settings[9] == 2) {
//            this.dataStorage = new DataStorage(settings[0], settings[1], settings[3], settings[4], settings[7], settings[8]);
//            this.dataStorage2 = new DataStorage(settings[0], settings[1], settings[3], settings[4], settings[7], settings[8]);
//            try {
//                UserInterfaceImplementation uiImpl = new UserInterfaceImplementation(primaryStage, dataStorage, dataStorage2);
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
        }
    }

}