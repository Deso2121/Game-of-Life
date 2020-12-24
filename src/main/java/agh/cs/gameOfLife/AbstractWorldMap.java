package agh.cs.gameOfLife;

import agh.cs.gameOfLife.animal.Animal;
import agh.cs.gameOfLife.data.DataStorage;
import agh.cs.gameOfLife.interfaces.IPositionChangeObserver;
import agh.cs.gameOfLife.interfaces.IWorldMap;
import agh.cs.gameOfLife.ui.UserInterfaceImplementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public abstract class AbstractWorldMap implements IPositionChangeObserver, IWorldMap {


    protected final DataStorage dataStorage;

    protected int width;
    protected int height;
    protected int mapSize;
    protected int steppeSize;

    protected int jungleWidth;
    protected int jungleHeight;
    protected ArrayList<Vector2d> steppePositions;
    protected ArrayList<Vector2d> noGrassPositions;

    protected final Vector2d lowerLeft;
    protected final Vector2d upperRight;
    protected UserInterfaceImplementation uiImpl;

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public AbstractWorldMap(int width, int height, int jungleWidth, int jungleHeight, DataStorage dataStorage, UserInterfaceImplementation uiImpl) {
        this.uiImpl = uiImpl;
        this.width = width;
        this.height = height;
        this.jungleWidth = jungleWidth;
        this.jungleHeight = jungleHeight;
        this.dataStorage = dataStorage;

        lowerLeft = new Vector2d(0, 0);
        upperRight = new Vector2d(width - 1, height - 1);
    }

    public UserInterfaceImplementation getUiImpl() {
        return uiImpl;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return false;
    }

    @Override
    public void place(Animal animal) {
        if (!dataStorage.getAnimals().containsKey(animal.getPosition())) {
            dataStorage.getAnimals().put(animal.getPosition(), new LinkedList<>(Arrays.asList(animal)));
        }
        else {
            dataStorage.getAnimals().get(animal.getPosition()).add(animal);
        }
        dataStorage.getAnimalsList().add(animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return dataStorage.getAnimals().containsKey(position);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        dataStorage.getAnimals().get(oldPosition).remove(animal);
        if (!dataStorage.getAnimals().containsKey(newPosition))
            dataStorage.getAnimals().put(animal.getPosition(), new LinkedList<>(Arrays.asList(animal)));
        else
            dataStorage.getAnimals().get(newPosition).add(animal);
    }
}
