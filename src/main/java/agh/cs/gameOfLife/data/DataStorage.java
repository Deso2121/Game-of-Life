package agh.cs.gameOfLife.data;

import agh.cs.gameOfLife.animal.Animal;
import agh.cs.gameOfLife.mapObjects.Grass;
import agh.cs.gameOfLife.Vector2d;
import agh.cs.gameOfLife.constants.GameState;
import agh.cs.gameOfLife.mapObjects.Jungle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DataStorage {
    private Map<Vector2d, LinkedList<Animal>> animals;
    private List<Animal> animalsList;
    private List<Animal> deadAnimalsList;
    private Map<Vector2d, Grass> grass;
    private Map<Vector2d, Jungle> jungle;
    private int mapWidth;
    private int mapHeight;
    private GameState gameState;
    private int day;

    private int animalStartingEnergy;
    private int energyToReproduce;
    private int grassEnergy;
    private int moveEnergy;
    private ArrayList<Vector2d> mapPositions;
    private float speed;
    private int dayWhenDataSaved;

    public int getGrassEnergy() {
        return grassEnergy;
    }

    public int getAnimalStartingEnergy() {
        return animalStartingEnergy;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDay() {
        return day;
    }

    public int getDayWhenDataSaved() {
        return dayWhenDataSaved;
    }

    public void increaseDay() {
        day += 1;
    }
    public List<Animal> getAnimalsList() {
        return animalsList;
    }

    public int getEnergyToReproduce() {
        return energyToReproduce;
    }

    public DataStorage(int width, int height, int grassEnergy, int startEnergy, int moveEnergy, float speed, int dayWhenDataSaved) {
        animals = new ConcurrentHashMap<>();
        animalsList = Collections.synchronizedList(new LinkedList<>());
        deadAnimalsList = Collections.synchronizedList(new LinkedList<>());
        mapPositions = new ArrayList<>();
        grass = new ConcurrentHashMap<>();
        jungle = new HashMap<>();
        mapWidth = width;
        mapHeight = height;
        for (int x = 0; x < mapWidth; x ++)
            for (int y = 0; y < mapHeight; y ++)
                mapPositions.add(new Vector2d(x, y));
        animalStartingEnergy = startEnergy;
        energyToReproduce = animalStartingEnergy / 2;
        this.grassEnergy = grassEnergy;
        this.moveEnergy = moveEnergy;
        this.speed = speed;
        this.dayWhenDataSaved = dayWhenDataSaved;
    }

    public String getMostCommonGenotype() {
        if(animalsList.size() == 0) {
            return "";
        }
        List<String> genotypes = animalsList.stream().map(Animal::getGenotypeString).collect(Collectors.toList());
        Optional<Map.Entry<String, Long>> result = genotypes.stream().collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));
        return result.get().getKey();
    }

    public Map<Vector2d, LinkedList<Animal>> getAnimals() {
        return animals;
    }

    public Map<Vector2d, Grass> getGrass() {
        return grass;
    }

    public List<Animal> getDeadAnimalsList() {
        return deadAnimalsList;
    }

    public Map<Vector2d, Jungle> getJungle() {
        return jungle;
    }
}
