package agh.cs.gameOfLife.interfaces;

import agh.cs.gameOfLife.mapObjects.Grass;

public interface IGrassChangeObserver {
    void grassAdd(Grass grass);
    void grassRemove(Grass grass);
}
