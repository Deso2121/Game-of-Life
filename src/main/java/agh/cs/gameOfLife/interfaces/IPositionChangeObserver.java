package agh.cs.gameOfLife.interfaces;

import agh.cs.gameOfLife.Vector2d;
import agh.cs.gameOfLife.animal.Animal;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal);
}
