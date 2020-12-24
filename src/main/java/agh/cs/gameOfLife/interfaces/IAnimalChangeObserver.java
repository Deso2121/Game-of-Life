package agh.cs.gameOfLife.interfaces;

import agh.cs.gameOfLife.animal.Animal;
import javafx.scene.paint.Color;

public interface IAnimalChangeObserver {
    void animalAdd(Animal animal);
    void animalRemove(Animal animal);
    void colorChanged(Animal animal, Color color);
}
