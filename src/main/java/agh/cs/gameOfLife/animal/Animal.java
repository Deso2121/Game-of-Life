package agh.cs.gameOfLife.animal;

import agh.cs.gameOfLife.AbstractWorldMap;
import agh.cs.gameOfLife.Vector2d;
import agh.cs.gameOfLife.constants.MapDirection;
import agh.cs.gameOfLife.constants.MoveDirection;
import agh.cs.gameOfLife.interfaces.IAnimalChangeObserver;
import agh.cs.gameOfLife.interfaces.IMapElement;
import agh.cs.gameOfLife.interfaces.IPositionChangeObserver;
import agh.cs.gameOfLife.interfaces.IWorldMap;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements IMapElement, Comparable {
    private MapDirection orientation;
    private Vector2d position;
    private Vector2d oldPosition;
    private Vector2d newPosition;
    private Vector2d checkedPosition;
    private final Vector2d initialPosition;
    private final IWorldMap map;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    private final List<IAnimalChangeObserver> colorObservers = new ArrayList<>();
    private Color color;
    private Color newColor;

    private Random random;
    private int energy;
    private int startingEnergy;
    private int daysAlive;
    private int childrenCounter;
    private final List<Integer> genotype;
    private final int animalID;

    public Animal(AbstractWorldMap map, int animalID) {
        this.animalID = animalID;
        this.position = new Vector2d(2, 2);
        this.initialPosition = new Vector2d(2, 2);
        this.map = map;
        random = new Random();
        genotype = DNA.drawDNA();
        int random_number = ThreadLocalRandom.current().nextInt(0, 32);
        this.orientation = MapDirection.getDirectionByNumber(genotype.get(random_number));
        this.observers.add(map);
        startingEnergy = map.getDataStorage().getAnimalStartingEnergy();
        energy = startingEnergy;
    }

    public Animal(AbstractWorldMap map, Vector2d initialPosition, int animalID) {
        this.animalID = animalID;
        this.initialPosition = initialPosition;
        this.position = initialPosition;
        this.map = map;
        random = new Random();
        genotype = DNA.drawDNA();
        int random_number = ThreadLocalRandom.current().nextInt(0, 32);
        this.orientation = MapDirection.getDirectionByNumber(genotype.get(random_number));
        this.observers.add(map);
        this.colorObservers.add(map.getUiImpl());
        startingEnergy = map.getDataStorage().getAnimalStartingEnergy();
        energy = startingEnergy;
    }

    public Animal(AbstractWorldMap map, List<Integer> genotype, int animalID, Vector2d initialPosition, int energy, int startingEnergy) {
        this.animalID = animalID;
        this.map = map;
        // INITIAL POSITION, POSITION
        this.genotype = genotype;
        int random_number = ThreadLocalRandom.current().nextInt(0, 32);
        orientation = MapDirection.getDirectionByNumber(genotype.get(random_number));
        this.observers.add(map);
        this.colorObservers.add(map.getUiImpl());
        this.initialPosition = initialPosition;
        position = initialPosition;
        this.startingEnergy = startingEnergy;
        this.energy = energy;
        if (energy >= 0.75 * startingEnergy)
            color = Color.rgb(153, 255, 255);
        else if (energy >= 0.5 * startingEnergy)
            color = Color.rgb(102, 153, 255);
        else if (energy >= 0.25 * startingEnergy)
            color = Color.rgb(0, 102, 204);
        else
            color = Color.rgb(0, 51, 102);
    }

    public String getGenotypeString() {
        return genotype.toString();
    }

    public List<Integer> getGenotype() {
        return genotype;
    }

    public Color getColor() {
        return color;
    }

    public int getEnergy() {
        return energy;
    }

    public void eatGrass(int energy) {
        this.energy += energy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Animal animal = (Animal) o;

        if (animalID != animal.animalID) return false;
        return map != null ? map.equals(animal.map) : animal.map == null;
    }

    @Override
    public int hashCode() {
        int result = map != null ? map.hashCode() : 0;
        result = 31 * result + animalID;
        return result;
    }


    @Override
    public String toString() {
        switch (orientation) {
            case NORTH:
                return "N";
            case EAST:
                return "E";
            case SOUTH:
                return "S";
            case NORTHEAST:
                return "NE";
            case NORTHWEST:
                return "NW";
            case SOUTHEAST:
                return "SE";
            case SOUTHWEST:
                return "SE";
            default:
                return "W";
        }
    }

    public void move(MoveDirection direction) {
        int random_number = ThreadLocalRandom.current().nextInt(0, 32);
        orientation = MapDirection.getDirectionByNumber(genotype.get(random_number));
        oldPosition = position;
        Vector2d movement = orientation.toUnitVector();
        newPosition = oldPosition.add(movement);
        if (newPosition.getX() >= map.getWidth())
            newPosition = new Vector2d(0, newPosition.getY());
        if (newPosition.getY() >= map.getHeight())
            newPosition = new Vector2d(newPosition.getX(), 0);
        if (newPosition.getX() < 0)
            newPosition = new Vector2d(map.getWidth() - 1, newPosition.getY());
        if (newPosition.getY() < 0)
            newPosition = new Vector2d(newPosition.getX(), map.getHeight() - 1);
        position = newPosition;
        positionChanged(oldPosition, newPosition);
    }


    public Vector2d getPosition() {
        return position;
    }

    public int getPositionX() {
        return position.getX();
    }

    public int getPositionY() {
        return position.getY();
    }

    void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver o : this.observers) {
            o.positionChanged(oldPosition, newPosition, this);
        }
    }

    public void loseEnergy(int energy) {
        this.energy -= energy;
    }

    public int getDaysAlive() {
        return daysAlive;
    }

    public void increaseDaysAlive() {
        daysAlive += 1;
    }

    public int getChildrenCounter() {
        return childrenCounter;
    }

    public void increaseChildrenCounter(int childrenCounter) {
        this.childrenCounter += childrenCounter;
    }

    public void adjustColor() {
        if (energy >= 0.75 * startingEnergy) {
            newColor = Color.rgb(153, 255, 255);
            if (!newColor.equals(color)) {
                color = newColor;
                colorChanged(color);
            }
        }
        else if (energy >= 0.5 * startingEnergy) {
            newColor = Color.rgb(102, 153, 255);
            if (!newColor.equals(color)) {
                color = newColor;
                colorChanged(color);
            }
        }
        else if (energy >= 0.25 * startingEnergy) {
            newColor = Color.rgb(0, 102, 204);
            if (!newColor.equals(color)) {
                color = newColor;
                colorChanged(color);
            }
        }
        else {
            newColor = Color.rgb(0, 51, 102);
            if (!newColor.equals(color)) {
                color = newColor;
                colorChanged(color);
            }
        }
    }

    private void colorChanged(Color color) {
        for (IAnimalChangeObserver o : this.colorObservers) {
            o.colorChanged(this, color);
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
