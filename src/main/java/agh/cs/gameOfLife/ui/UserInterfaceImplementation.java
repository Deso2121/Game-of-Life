package agh.cs.gameOfLife.ui;

import agh.cs.gameOfLife.animal.Animal;
import agh.cs.gameOfLife.constants.GameState;
import agh.cs.gameOfLife.data.DataStorage;
import agh.cs.gameOfLife.GameOfLife;
import agh.cs.gameOfLife.interfaces.IAnimalChangeObserver;
import agh.cs.gameOfLife.interfaces.IGrassChangeObserver;
import agh.cs.gameOfLife.interfaces.IStatisticsChangeObserver;
import agh.cs.gameOfLife.mapObjects.Grass;
import agh.cs.gameOfLife.Vector2d;
import agh.cs.gameOfLife.engine.SimulationEngine;
import agh.cs.gameOfLife.interfaces.IUserInterfaceContract;
import agh.cs.gameOfLife.mapObjects.Jungle;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class UserInterfaceImplementation implements IUserInterfaceContract.View,
        EventHandler<KeyEvent>, IGrassChangeObserver, IAnimalChangeObserver, IStatisticsChangeObserver {

    private final Stage stage;
    private final Group root;
    private int mapWidth;
    private int mapHeight;

    private Rectangle map;
    private double mapX;
    private double mapY;
    private double mapRectangleWidth;
    private double mapRectangleHeight;
    private double mapTileWidth;
    private double mapTileHeight;
    private double rectangleDrawX;
    private double rectangleDrawY;

    private Text animalNumber;
    private Text grassNumber;
    private Text mostCommonGenotype;
    private Text averageEnergy;
    private Text averageLifeLength;
    private Text averageChildren;

    private SimulationEngine engine;

    public void setEngine(SimulationEngine engine) {
        this.engine = engine;
    }

    private HashMap<Animal, Ellipse> mapAnimalFields;
    private HashMap<Jungle, Rectangle> mapJungleFields;
    private HashMap<Grass, Rectangle> mapGrassFields;

    private IUserInterfaceContract.EventListener listener;

    private static final double WINDOW_Y = 900;
    private static final double WINDOW_X = 1600;

    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(242,242,241);
    private static final Color BOARD_BACKGROUND_COLOR = Color.WHITESMOKE;
    private static final String TITLE = "Game of Life";

    private DataStorage dataStorage;

    public UserInterfaceImplementation(Stage stage, DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.stage = stage;
        this.root = new Group();
        this.mapAnimalFields = new HashMap<>();
        this.mapJungleFields = new HashMap<>();
        this.mapGrassFields = new HashMap<>();
        mapWidth = dataStorage.getMapWidth();
        mapHeight = dataStorage.getMapHeight();
    }

    public void initializeUserInterface() {
        mapRectangleWidth = WINDOW_X * 0.5;
        mapRectangleHeight = WINDOW_Y;
        mapX = WINDOW_X * 0.5;
        mapY = 0;

        mapTileWidth = mapRectangleWidth / mapWidth;
        mapTileHeight = mapRectangleHeight / mapHeight;

        rectangleDrawX = mapX;
        rectangleDrawY = mapY + WINDOW_Y - mapTileHeight;

        drawBackground(root);
        drawGameOfLifeBoard(root);
        drawMap(root);
        drawLegend(root);
        drawStatistics(root);
        drawMapTiles(root);
        drawAnimals(root);
        drawGridLines(root);
        root.requestFocus();
        stage.show();
    }

    private void drawMap(Group root) {
        map = new Rectangle();
        map.setX(mapX);
        map.setY(mapY);
        map.setWidth(mapRectangleWidth);
        map.setHeight(mapRectangleHeight);
        map.setFill(Color.rgb(237, 201, 175));
        map.setViewOrder(5);
        root.getChildren().addAll(map);
    }

    private void drawMapTiles(Group root) {
        for (Map.Entry<Vector2d, Jungle> jungle : dataStorage.getJungle().entrySet()) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(rectangleDrawX + jungle.getKey().getX() * mapTileWidth);
            rectangle.setY(rectangleDrawY - jungle.getKey().getY() * mapTileHeight);
            rectangle.setWidth(mapTileWidth);
            rectangle.setHeight(mapTileHeight);
            rectangle.setFill(Color.DARKGREEN);
            mapJungleFields.put(jungle.getValue(), rectangle);
            rectangle.setViewOrder(3);
            root.getChildren().addAll(rectangle);
        }
        for (Map.Entry<Vector2d, Grass> grass : dataStorage.getGrass().entrySet()) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(rectangleDrawX + grass.getKey().getX() * mapTileWidth);
            rectangle.setY(rectangleDrawY - grass.getKey().getY() * mapTileHeight);
            rectangle.setWidth(mapTileWidth);
            rectangle.setHeight(mapTileHeight);
            rectangle.setFill(Color.LIGHTGREEN);
            mapGrassFields.put(grass.getValue(), rectangle);
            rectangle.setViewOrder(1);
            root.getChildren().addAll(rectangle);
        }
    }

    @Override
    public void grassAdd(Grass grass) {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(rectangleDrawX + grass.getPosition().getX() * mapTileWidth);
        rectangle.setY(rectangleDrawY - grass.getPosition().getY() * mapTileHeight);
        rectangle.setWidth(mapTileWidth);
        rectangle.setHeight(mapTileHeight);
        rectangle.setFill(Color.LIGHTGREEN);
        mapGrassFields.put(grass, rectangle);
        rectangle.setViewOrder(1);
        root.getChildren().addAll(rectangle);
    }

    @Override
    public void grassRemove(Grass grass) {
        root.getChildren().remove(mapGrassFields.get(grass));
    }

    @Override
    public void animalAdd(Animal animal) {
        Ellipse ellipse = new Ellipse();
        ellipse.setCenterX(rectangleDrawX + animal.getPositionX() * mapTileWidth + mapTileWidth / 2);
        ellipse.setCenterY(rectangleDrawY - animal.getPositionY() * mapTileHeight + mapTileHeight / 2);
        ellipse.setRadiusX(mapTileWidth / 2);
        ellipse.setRadiusY(mapTileHeight / 2);
        ellipse.setFill(animal.getColor());
        mapAnimalFields.put(animal, ellipse);
        ellipse.setViewOrder(0);
        root.getChildren().addAll(ellipse);
    }

    @Override
    public void animalRemove(Animal animal) {
        root.getChildren().remove(mapAnimalFields.get(animal));
    }

    @Override
    public void colorChanged(Animal animal, Color color) {
        mapAnimalFields.get(animal).setFill(color);
    }


    private void drawAnimals(Group root) {
        for (Map.Entry<Vector2d, LinkedList<Animal>> animalList : dataStorage.getAnimals().entrySet()) {
            if (animalList.getValue().size() > 0) {
                for (Animal animal : animalList.getValue()) {
                    Ellipse ellipse = new Ellipse();
                    ellipse.setCenterX(rectangleDrawX + animal.getPositionX() * mapTileWidth + mapTileWidth / 2);
                    ellipse.setCenterY(rectangleDrawY - animal.getPositionY() * mapTileHeight + mapTileHeight / 2);
                    ellipse.setRadiusX(mapTileWidth / 2);
                    ellipse.setRadiusY(mapTileHeight / 2);
                    ellipse.setFill(animal.getColor());
                    mapAnimalFields.put(animal, ellipse);
                    ellipse.setViewOrder(0);
                    root.getChildren().addAll(ellipse);
                }
            }
        }
    }

    private void drawLegend(Group root) {
        double startX = WINDOW_X * 0.01;
        double startY = WINDOW_Y / 3;

        //Steppe legend
        Rectangle steppeLegend = new Rectangle();
        steppeLegend.setX(startX);
        steppeLegend.setY(startY);
        steppeLegend.setWidth(40);
        steppeLegend.setHeight(40);
        steppeLegend.setFill(Color.rgb(237, 201, 175));
        steppeLegend.setViewOrder(5);
        root.getChildren().addAll(steppeLegend);

        Text steppeText = new Text(startX + 40 + WINDOW_X * 0.01, startY + 40 * 0.70, "- Steppe field");
        steppeText.setFill(Color.BLACK);
        steppeText.setFont(new Font(30));
        steppeText.setViewOrder(5);
        root.getChildren().add(steppeText);

        //Jungle legend
        Rectangle jungleLegend = new Rectangle();
        jungleLegend.setX(startX);
        jungleLegend.setY(startY + 40 + WINDOW_X * 0.01);
        jungleLegend.setWidth(40);
        jungleLegend.setHeight(40);
        jungleLegend.setFill(Color.DARKGREEN);
        jungleLegend.setViewOrder(5);
        root.getChildren().addAll(jungleLegend);

        Text jungleText = new Text(startX + 40 + WINDOW_X * 0.01, jungleLegend.getY() + 40 * 0.70, "- Jungle field");
        jungleText.setFill(Color.BLACK);
        jungleText.setFont(new Font(30));
        jungleText.setViewOrder(5);
        root.getChildren().add(jungleText);

        //Grass legend
        Rectangle grassLegend = new Rectangle();
        grassLegend.setX(startX);
        grassLegend.setY(jungleLegend.getY() + 40 + WINDOW_X * 0.01);
        grassLegend.setWidth(40);
        grassLegend.setHeight(40);
        grassLegend.setFill(Color.LIGHTGREEN);
        grassLegend.setViewOrder(5);
        root.getChildren().addAll(grassLegend);

        Text grassText = new Text(jungleText.getX(), grassLegend.getY() + 40 * 0.70, "- Grass field");
        grassText.setFill(Color.BLACK);
        grassText.setFont(new Font(30));
        grassText.setViewOrder(5);
        root.getChildren().add(grassText);

        //Animal legend
        Ellipse ellipse = new Ellipse();
        ellipse.setCenterX(startX + mapTileWidth / 2);
        ellipse.setCenterY(grassLegend.getY() + 40 + 40 / 2 + WINDOW_X * 0.01);
        ellipse.setRadiusX(40 / 2.0);
        ellipse.setRadiusY(40 / 2.0);
        ellipse.setFill(Color.rgb(153, 255, 255));
        ellipse.setViewOrder(5);
        root.getChildren().addAll(ellipse);

        Text animalText = new Text(grassText.getX(), ellipse.getCenterY() - 40 / 2.0 + 40 * 0.70, "- Animal (darker when loses energy)");
        animalText.setFill(Color.BLACK);
        animalText.setFont(new Font(30));
        animalText.setViewOrder(5);
        root.getChildren().add(animalText);
    }

    private void drawStatistics(Group root) {
        double startX = WINDOW_X * 0.01;
        double startY = WINDOW_Y * 0.01;
        animalNumber = new Text(startX + WINDOW_X * 0.01, startY * 5, "Number of animals: " + 0);
        animalNumber.setFill(Color.BLACK);
        animalNumber.setFont(new Font(30));
        animalNumber.setViewOrder(5);
        root.getChildren().add(animalNumber);

        grassNumber = new Text(startX + WINDOW_X * 0.01, startY * 10, "Amount of grass: " + 0);
        grassNumber.setFill(Color.BLACK);
        grassNumber.setFont(new Font(30));
        grassNumber.setViewOrder(5);
        root.getChildren().add(grassNumber);


        Text mostCommonGenotypeText = new Text(startX + WINDOW_X * 0.01, startY * 15, "Most common genotype: ");
        mostCommonGenotypeText.setFill(Color.BLACK);
        mostCommonGenotypeText.setFont(new Font(30));
        mostCommonGenotypeText.setViewOrder(5);
        root.getChildren().add(mostCommonGenotypeText);

        mostCommonGenotype = new Text(startX + 345 + WINDOW_X * 0.01, startY * 14.5, "");
        mostCommonGenotype.setFill(Color.BLACK);
        mostCommonGenotype.setFont(new Font(12));
        mostCommonGenotype.setViewOrder(5);
        root.getChildren().add(mostCommonGenotype);

        averageEnergy = new Text(startX + WINDOW_X * 0.01, startY * 20, "Average energy: " + 0);
        averageEnergy.setFill(Color.BLACK);
        averageEnergy.setFont(new Font(30));
        averageEnergy.setViewOrder(5);
        root.getChildren().add(averageEnergy);

        averageLifeLength = new Text(startX + WINDOW_X * 0.01, startY * 25, "Average life length: ");
        averageLifeLength.setFill(Color.BLACK);
        averageLifeLength.setFont(new Font(30));
        averageLifeLength.setViewOrder(5);
        root.getChildren().add(averageLifeLength);

        averageChildren = new Text(startX + WINDOW_X * 0.01, startY * 30, "Average number of children: " + 0);
        averageChildren.setFill(Color.BLACK);
        averageChildren.setFont(new Font(30));
        averageChildren.setViewOrder(5);
        root.getChildren().add(averageChildren);
    }

    @Override
    public void updateStatistics(int animals, int grass, String genotype, double averageEnergy, double averageLifeLength, double averageChildrenNumber) {
        animalNumber.setText("Number of animals: " + animals);
        grassNumber.setText("Amount of grass: " + grass);
        mostCommonGenotype.setText(genotype);
        this.averageEnergy.setText("Average energy: " + averageEnergy);
        this.averageLifeLength.setText("Average life length: " + averageLifeLength);
        this.averageChildren.setText("Average number of children: " + Math.round(averageChildrenNumber));
    }

    private void drawGridLines(Group root) {
        Line verticalLine = getLine(WINDOW_X / 2, 0, WINDOW_X / 2, WINDOW_Y);
        root.getChildren().addAll(verticalLine);
    }


    private void drawGameOfLifeBoard(Group root) {
        Rectangle boardBackground = new Rectangle();

        boardBackground.setX(0);
        boardBackground.setY(0);

        boardBackground.setWidth(WINDOW_X);
        boardBackground.setHeight(WINDOW_Y);

        boardBackground.setFill(BOARD_BACKGROUND_COLOR);
        boardBackground.setViewOrder(10);

        root.getChildren().addAll(boardBackground);
    }

    private void drawBackground(Group root) {
        Scene scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.setOnKeyPressed(this);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }

    private Line getLine(double startX, double startY, double endX, double endY) {
        Line line = new Line();

        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);

        line.setFill(Color.BLACK);
        return line;
    }

    public void updateAnimals() {
        for (Map.Entry<Animal, Ellipse> animal : mapAnimalFields.entrySet()) {
            animal.getValue().setCenterX(rectangleDrawX + animal.getKey().getPositionX() * mapTileWidth + mapTileWidth / 2);
            animal.getValue().setCenterY(rectangleDrawY - animal.getKey().getPositionY() * mapTileHeight + mapTileHeight / 2);
        }

    }

    @Override
    public void updateGame(GameOfLife game) {

    }

    @Override
    public void updateGraph(GameOfLife game) {

    }

    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void showDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK)
            listener.onDialogClick();
    }

    @Override
    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();
    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (event.getCode() == KeyCode.SPACE) {
                if (dataStorage.getGameState() == GameState.ACTIVE) {
                    dataStorage.setGameState(GameState.PAUSED);
                    engine.getTimer().stop();
                    System.out.println("Game paused!");
                }
                else if (dataStorage.getGameState() == GameState.PAUSED) {
                    dataStorage.setGameState(GameState.ACTIVE);
                    engine.getTimer().start();
                    System.out.println("Game unpaused!");
                }

            }
        }
    }
}
