package agh.cs.gameOfLife.engine;


import agh.cs.gameOfLife.GrassField;
import agh.cs.gameOfLife.animal.Animal;
import agh.cs.gameOfLife.animal.DNA;
import agh.cs.gameOfLife.data.DataStorage;
import agh.cs.gameOfLife.constants.MoveDirection;
import agh.cs.gameOfLife.Vector2d;
import agh.cs.gameOfLife.interfaces.IAnimalChangeObserver;
import agh.cs.gameOfLife.interfaces.IEngine;
import agh.cs.gameOfLife.interfaces.IStatisticsChangeObserver;
import agh.cs.gameOfLife.mapObjects.Grass;
import agh.cs.gameOfLife.ui.UserInterfaceImplementation;
import javafx.animation.AnimationTimer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SimulationEngine implements IEngine {

    GrassField map;
    private UserInterfaceImplementation uiImpl;
    private DataStorage dataStorage;

    private final List<IAnimalChangeObserver> animalObservers = new ArrayList<>();

    AnimationTimer timer;
    private double t = 0;
    private int animalIDCounter;
    private ArrayList<Animal> eatingAnimals;
    private final List<IStatisticsChangeObserver> statisticsObservers = new ArrayList<>();

    public SimulationEngine(GrassField map, UserInterfaceImplementation uiImpl, DataStorage dataStorage, int numberOfAnimals) {
        this.dataStorage = dataStorage;
        this.uiImpl = uiImpl;
        this.map = map;
        this.animalObservers.add(uiImpl);
        animalIDCounter = 0;
        eatingAnimals = new ArrayList<>();
        placeAdamEve(numberOfAnimals);
        this.statisticsObservers.add(uiImpl);
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                run();
            }
        };
    }

    public AnimationTimer getTimer() {
        return timer;
    }

    public void startTimer() {
        timer.start();
    }

    public void placeAdamEve(int numberOfAnimals) {
        List<Integer> places = ThreadLocalRandom.current().ints(0,
                map.getWidth()* map.getHeight()).distinct().limit(numberOfAnimals).boxed().collect(Collectors.toList());
        for(int i:places) {
            Vector2d v = new Vector2d(i/ map.getHeight(),i% map.getHeight());
            animalIDCounter += 1;
            Animal animal = new Animal(map, v, animalIDCounter);
            map.place(animal);
        }
    }

    @Override
    public void run() {
        if (t == 0) {
            dataStorage.increaseDay();
            loseEnergyAndDie();
            for (Animal animal : dataStorage.getAnimalsList()) {
                animal.move(MoveDirection.FORWARD);
                animal.increaseDaysAlive();
            }
            uiImpl.updateAnimals();
            eatGrass();
            findParents();
            map.growGrass();
            updateStatistics(dataStorage.getAnimalsList().size(),
                    dataStorage.getGrass().size(),
                    dataStorage.getMostCommonGenotype(),
                    dataStorage.getAnimalsList().stream().mapToInt(Animal::getEnergy).average().orElse(0),
                    dataStorage.getDeadAnimalsList().stream().mapToInt(Animal::getDaysAlive).average().orElse(0),
                    dataStorage.getAnimalsList().stream().mapToInt(Animal::getChildrenCounter).average().orElse(0)
                    );
        }
        t += 0.0167;
        if (t >= 1 / dataStorage.getSpeed()) {
            t = 0;
        }

    }

    private void updateStatistics(int animals, int grass, String genotype, double averageEnergy, double averageLifeLength, double averageChildrenNumber) {
        for (IStatisticsChangeObserver o : this.statisticsObservers) {
            o.updateStatistics(animals, grass, genotype, Math.round(averageEnergy * 100.0) / 100.0, Math.round(averageLifeLength * 100.0) / 100.0, averageChildrenNumber);
        }
        if (dataStorage.getDay() == dataStorage.getDayWhenDataSaved()) {
            saveDataToFile(animals, grass, genotype, averageEnergy, averageLifeLength, averageChildrenNumber);
        }
    }

    private void saveDataToFile(int animals, int grass, String genotype, double averageEnergy, double averageLifeLength, double averageChildrenNumber) {
        try {
            FileWriter myWriter = new FileWriter("statistics after " + dataStorage.getDay() + " days.txt");
            myWriter.write("Number of animals: " + animals +
                    "\nAmount of grass: " + grass +
                    "\nMost common genotype: " + genotype +
                    "\nAverage energy: " + averageEnergy +
                    "\nAverage life length: " + averageLifeLength +
                    "\nAverage number of children: " + averageChildrenNumber);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findParents() {
        List<Animal> parentsList = Collections.synchronizedList(new LinkedList<>());
        List<Animal> bornAnimals = Collections.synchronizedList(new LinkedList<>());
        for (Map.Entry<Vector2d, LinkedList<Animal>> animalList : dataStorage.getAnimals().entrySet()) {
            if (animalList.getValue().size() >= 2) {
                parentsList = animalList.getValue();
                parentsList.sort(new Comparator<Animal>() {
                    @Override
                    public int compare(Animal o1, Animal o2) {
                        return Integer.compare(o1.getEnergy(), o2.getEnergy());
                    }
                }.reversed());

            }
            if (parentsList.size() >= 2)
                if (parentsList.get(0).getEnergy() >= dataStorage.getEnergyToReproduce() && parentsList.get(1).getEnergy() >= dataStorage.getEnergyToReproduce()) {
                    bornAnimals.add(reproduce(parentsList.get(0), parentsList.get(1)));
                    parentsList.get(0).loseEnergy(parentsList.get(0).getEnergy() / 4);
                    parentsList.get(0).increaseChildrenCounter(1);
                    parentsList.get(1).loseEnergy(parentsList.get(1).getEnergy() / 4);
                    parentsList.get(1).increaseChildrenCounter(1);
                }
        }
        for (Animal animal : bornAnimals) {
            map.place(animal);
            animalAdd(animal);
        }
    }

    public Animal reproduce(Animal animal1, Animal animal2) {
        Vector2d initialPosition = animal1.getPosition();
        int tryCounter = 0;
        while ((initialPosition.equals(animal1.getPosition()) || map.isOccupied(initialPosition)) && tryCounter <= 20) {
            initialPosition = new Vector2d(ThreadLocalRandom.current().nextInt(animal1.getPositionX() - 1, animal1.getPositionX() + 2),
                    ThreadLocalRandom.current().nextInt(animal1.getPositionY() - 1, animal1.getPositionY() + 2));
            if (initialPosition.getX() < 0)
                initialPosition = new Vector2d(initialPosition.getX() + map.getWidth(), initialPosition.getY());
            if (initialPosition.getY() < 0)
                initialPosition = new Vector2d(initialPosition.getX(), initialPosition.getY() + map.getHeight());
            if (initialPosition.getX() >= map.getWidth())
                initialPosition = new Vector2d(initialPosition.getX() - map.getWidth(), initialPosition.getY());
            if (initialPosition.getY() >= map.getHeight())
                initialPosition = new Vector2d(initialPosition.getX(), initialPosition.getY() - map.getHeight());
            tryCounter += 1;
        }
        animalIDCounter += 1;
        return new Animal(map, DNA.recombineDNA(animal1.getGenotype(), animal2.getGenotype()), animalIDCounter, initialPosition, animal1.getEnergy() / 4 + animal2.getEnergy() / 4, dataStorage.getAnimalStartingEnergy());
    }


    public void eatGrass() {
        ArrayList<Grass> eatenGrass = new ArrayList<>();
        for (Map.Entry<Vector2d, Grass> grass : dataStorage.getGrass().entrySet()) {
            if (dataStorage.getAnimals().containsKey(grass.getKey())) {
                if (dataStorage.getAnimals().get(grass.getKey()).size() == 1) {
                    dataStorage.getAnimals().get(grass.getKey()).get(0).eatGrass(dataStorage.getGrassEnergy());
                    eatenGrass.add(grass.getValue());
                }
                if (dataStorage.getAnimals().get(grass.getKey()).size() > 1) {
                    List<Animal> strongest = dataStorage.getAnimals().get(grass.getKey()).stream()
                            .collect(Collectors.groupingBy(
                                    Animal::getEnergy,
                                    TreeMap::new,
                                    Collectors.toList()))
                            .lastEntry()
                            .getValue();
                    int energyToAdd = dataStorage.getGrassEnergy() / strongest.size();
                    strongest.forEach(a -> a.eatGrass(energyToAdd));
                    eatenGrass.add(grass.getValue());
                }
            }
        }
        for (Grass grass : eatenGrass)
            map.grassRemove(grass);
    }

    public void loseEnergyAndDie() {
        ArrayList<Animal> killedAnimals = new ArrayList<>();
        for (Animal animal : dataStorage.getAnimalsList()) {
            animal.loseEnergy(dataStorage.getMoveEnergy());
            animal.adjustColor();
            if (animal.getEnergy() <= 0) {
                killedAnimals.add(animal);
            }
        }
        if (killedAnimals.size() > 0) {
            for (Animal animal : killedAnimals) {
                dataStorage
                        .getAnimals().get(animal.getPosition())
                        .remove(animal);
                animalRemove(animal);
                dataStorage.getAnimalsList().remove(animal);
                dataStorage.getDeadAnimalsList().add(animal);
            }
        }
    }

    public void animalAdd(Animal animal) {
        for (IAnimalChangeObserver o : this.animalObservers) {
            o.animalAdd(animal);
        }
    }

    public void animalRemove(Animal animal) {
        for (IAnimalChangeObserver o : this.animalObservers) {
            o.animalRemove(animal);
        }
    }
}
