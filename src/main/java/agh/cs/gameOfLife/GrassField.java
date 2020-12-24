package agh.cs.gameOfLife;

import agh.cs.gameOfLife.data.DataStorage;
import agh.cs.gameOfLife.interfaces.IGrassChangeObserver;
import agh.cs.gameOfLife.mapObjects.Grass;
import agh.cs.gameOfLife.mapObjects.Jungle;
import agh.cs.gameOfLife.ui.UserInterfaceImplementation;

import java.util.ArrayList;
import java.util.List;

public class GrassField extends AbstractWorldMap {
    private final int numberOfGrassFields;
    private RandomIntPositionGenerator randomIntPositionGenerator;
    private ArrayList<Vector2d> grassPositions;
    private ArrayList<Vector2d> junglePositions;
    private final int[] xVariables;
    private final int[] yVariables;

    private final List<IGrassChangeObserver> grassObservers = new ArrayList<>();

    private int jungleLowerLeftCornerX;
    private int jungleLowerLeftCornerY;
    private int jungleSize;

    private final int jungleWidth;
    private final int jungleHeight;

    private int growingLoopCounter;

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public GrassField(int numberOfGrassFields, int width, int height, int jungleWidth, int jungleHeight, DataStorage dataStorage, UserInterfaceImplementation uiImpl) {
        super(width, height, jungleWidth, jungleHeight, dataStorage, uiImpl);
        this.numberOfGrassFields = numberOfGrassFields;
        this.jungleWidth = jungleWidth;
        this.jungleHeight = jungleHeight;
        this.grassObservers.add(uiImpl);
        randomIntPositionGenerator = new RandomIntPositionGenerator();
        xVariables = new int[width];
        yVariables = new int[height];
        jungleSize = jungleWidth * jungleHeight;
        mapSize = height * width;
        jungleLowerLeftCornerX = ((width - jungleWidth) / 2);
        jungleLowerLeftCornerY = ((height - jungleHeight) / 2);
        steppeSize = mapSize - jungleSize;
        steppePositions = new ArrayList<Vector2d>();
        junglePositions = new ArrayList<Vector2d>();

        generateJungle();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d steppe = new Vector2d(x, y);
                if (!junglePositions.contains(steppe)) {
                    steppePositions.add(steppe);
                }
            }
        }

//        Lista pozycji (wektorów) kępek trawy
        generateGrassPositionVectors();
        for (Vector2d vector : grassPositions) {
            dataStorage.getGrass().put(vector, new Grass(vector));
        }
    }

    public void growGrass() {
        growingLoopCounter = 0;
        while (growingLoopCounter < 2 * jungleSize) {

            //random position in jungle
            Vector2d newGrassJungle = randomIntPositionGenerator.getRandomVectorFromArray(junglePositions);
            if (!dataStorage.getGrass().containsKey(newGrassJungle)) {
                if (!dataStorage.getAnimals().containsKey(newGrassJungle) || dataStorage.getAnimals().get(newGrassJungle).size() == 0) {
                    Grass jungleGrass = new Grass(newGrassJungle);
                    dataStorage.getGrass().put(newGrassJungle, jungleGrass);
                    grassAdd(jungleGrass);
                    break;
                }
            }
            growingLoopCounter++;
        }
        //For Steppe
        growingLoopCounter = 0;
        while (growingLoopCounter < 2 * steppeSize) {
            Vector2d newGrassSteppe = randomIntPositionGenerator.getRandomVectorFromArray(steppePositions);
            if (!dataStorage.getGrass().containsKey(newGrassSteppe))
                if (!dataStorage.getAnimals().containsKey(newGrassSteppe) || dataStorage.getAnimals().get(newGrassSteppe).size() == 0) {
                    Grass steppeGrass = new Grass(newGrassSteppe);
                    dataStorage.getGrass().put(newGrassSteppe, steppeGrass);
                    grassAdd(steppeGrass);
                    break;
            }
            growingLoopCounter++;
        }
    }

    void grassAdd(Grass grass) {
        for (IGrassChangeObserver o : this.grassObservers) {
            o.grassAdd(grass);
        }
    }

    public void grassRemove(Grass grass) {
        dataStorage.getGrass().remove(grass.getPosition());
        for (IGrassChangeObserver o : this.grassObservers) {
            o.grassRemove(grass);
        }
    }

    private void generateJungle() {
        for (int x = jungleLowerLeftCornerX; x < jungleLowerLeftCornerX + jungleWidth; x++) {
            for (int y = jungleLowerLeftCornerY; y < jungleLowerLeftCornerY + jungleHeight; y++) {
                Vector2d junglePosition = new Vector2d(x, y);
                dataStorage.getJungle().put(junglePosition, new Jungle(junglePosition));
                junglePositions.add(junglePosition);
            }
        }
    }

    public void generateGrassPositionVectors() {
        for (int x = 0; x < width; x++) {
            xVariables[x] = x;
        }
        for (int y = 0; y < height; y++) {
            yVariables[y] = y;
        }
        grassPositions = randomIntPositionGenerator.generateVectors(xVariables, yVariables, numberOfGrassFields);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (dataStorage.getAnimals().containsKey(position) && dataStorage.getAnimals().get(position).size() >= 1)
            return true;
        return dataStorage.getGrass().containsKey(position);
    }
}
